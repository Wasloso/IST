#include "Arduino.h";
#include <LiquidCrystal_I2C.h>
#include <util/atomic.h>
#include "string.h"
#define LED_RED 6
#define LED_GREEN 5
#define LED_BLUE 3

#define BUTTON_RED 2
#define BUTTON_GREEN 4

#define ENCODER_A_PIN A2
#define ENCODER_B_PIN A3

const unsigned long debouncingPeriod = 30UL;
unsigned long lastPressedRed = 0UL;
unsigned long lastPressedGreen = 0UL;
int lastStateGreen = HIGH;
int lastStateRed = HIGH;
int debouncedStateGreen = HIGH;
int debouncedStateRed = HIGH;
volatile int lastEncoderAState = HIGH;
volatile int lastEncoderBState = HIGH;
volatile unsigned long lastEncoderTime = 0UL;
volatile unsigned long encoderTime = 0UL;
bool isCelcius = true;
float temperatureIn = 25;
float temperatureOut = 21;
bool ledOn = false;
bool isBacklight = true;
LiquidCrystal_I2C lcd(0x27, 16, 2);

// TODO: ADD custom selector
byte customChar[] = {
    B00000,
    B00000,
    B00100,
    B00010,
    B11111,
    B00010,
    B00100,
    B00000,
};
struct LED
{
  char *name;
  int pin;
  int brightness;
  bool isOn;

  void changeBrightness()
  {
    if (!ledOn)
    {
      return;
    }
    this->brightness = (this->brightness + 5) % 255;
    analogWrite(this->pin, brightness);
  }

  void toggle()
  {
    if (ledOn)
    {
      analogWrite(this->pin, this->brightness);
    }
    else
    {
      analogWrite(this->pin, 0);
    }
  }

  LED(int pin, char *name)
  {
    this->name = name;
    this->pin = pin;
    this->brightness = 0;
    this->isOn = false;
    pinMode(pin, OUTPUT);
  }
};

struct Menu
{
  const char *name;
  int optionsCount;
  int selectedOption;
  struct Option *options[];
};

struct Option
{
  const char *name;
  const char **states;
  int selectedState;
  int statesCount;
  void (*action)();
  float (*getValue)();
  Option(const char *name, void (*action)() = nullptr, const char **states = nullptr, int statesCount = 0, float (*getValue)() = nullptr) : name(name), action(action), states(states), statesCount(statesCount), selectedState(0), getValue(getValue) {}

  virtual void execute()
  {
    nextState();
    if (action != nullptr)
    {
      action();
    }
  }
  virtual const char *getCurrentState()
  {
    if (states != nullptr)
    {
      return states[selectedState];
    }
    return nullptr;
  }
  void nextState()
  {
    if (states != nullptr)
    {
      selectedState = (selectedState + 1) % statesCount;
    }
  }
};

struct OptionTemp : Option
{
  OptionTemp(const char *name, float (*getValue)()) : Option(name)
  {
    Option::getValue = getValue;
  }
  const char *getCurrentState()
  {
    static char buffer[10];
    dtostrf(Option::getValue(), 6, 2, buffer);
    return buffer;
  }
};

struct OptionTempUnit : Option
{
  OptionTempUnit(const char *name, void (*action)() = nullptr) : Option(name, action) {}
  const char *getCurrentState()
  {
    return isCelcius ? "C" : "F";
  }
};

struct OptionLED : Option
{
  LED *led;
  OptionLED(LED *led) : Option(led->name), led(led) {}
  void execute()
  {
    this->led->changeBrightness();
  }
  const char *getCurrentState()
  {
    static char buffer[10];
    dtostrf(this->led->brightness, 6, 2, buffer);
    return buffer;
  }
};

LED leds[] = {
    LED(LED_RED, "RED"),
    LED(LED_GREEN, "GREEN"),
    LED(LED_BLUE, "BLUE")};

Menu *currentMenu;

void powerLedsAction()
{
  ledOn = !ledOn;
  for (LED &led : leds)
  {
    led.toggle();
  }
}

void toggleBacklight()
{
  isBacklight = !isBacklight;
  isBacklight ? lcd.backlight() : lcd.noBacklight();
}

void changeSelector()
{
}

void changeTempUnit()
{
  isCelcius = !isCelcius;
}

void changeMenu(Menu &menu)
{
  currentMenu = &menu;
  displayCurrentMenu();
}

Option powerLeds("POWER", powerLedsAction, new const char *[2]{"OFF", "ON"}, 2);
OptionLED redLed(&leds[0]);
OptionLED blueLed(&leds[1]);
OptionLED greenLed(&leds[2]);

Menu ledsMenu = {
    "Leds",
    4,
    0,
    {&powerLeds, &redLed, &blueLed, &greenLed},
};

Option backlight("BACKLIGHT", toggleBacklight, new const char *[2]{"ON", "OFF"}, 2);
Option selector("SELECTOR", changeSelector, new const char *[3]{">", "-", "c"}, 3);

Menu displayMenu = {
    "Display",
    2,
    0,
    {&backlight, &selector}};

OptionTemp sensorIn("IN", []() -> float
                    { return isCelcius ? temperatureIn : (1.8 * temperatureIn + 32); });
OptionTemp sensorOut("OUT", []() -> float
                     { return isCelcius ? temperatureOut : (1.8 * temperatureOut + 32); });
OptionTempUnit unit("UNIT", changeTempUnit);

Menu temperatureMenu = {
    "Temperature",
    3,
    0,
    {&sensorIn, &sensorOut, &unit}};

Menu aboutMenu = {
    "About",
    1,
    0,
    {new Option("Patryk Luszczek")}};

Option led = Option("LED", []() -> void
                    { changeMenu(ledsMenu); });
Option display = Option("DISPLAY", []() -> void
                        { changeMenu(displayMenu); });
Option temp = Option("TEMP", []() -> void
                     { changeMenu(temperatureMenu); });
Option about = Option("ABOUT", []() -> void
                      { changeMenu(aboutMenu); });

Menu mainMenu = {
    "Main",
    4,
    0,
    {&led, &display, &temp, &about}};

void displayOption(Option &option)
{
  lcd.print(option.name);
  const char *state = option.getCurrentState();
  if (state != nullptr)
  {
    lcd.print(" [");

    lcd.print(state);
    lcd.print("]");
  }
}

void displayCurrentMenu()
{
  lcd.clear();
  int currentOptionIndex = currentMenu->selectedOption;
  int nextOptionIndex = (currentOptionIndex + 1) % currentMenu->optionsCount;
  lcd.setCursor(0, 0);
  if (selector.selectedState == selector.statesCount - 1)
  {
    lcd.write(byte(0));
  }
  else
  {
    lcd.print(selector.getCurrentState());
  }
  displayOption(*currentMenu->options[currentOptionIndex]);
  if (currentMenu->options[currentOptionIndex] != currentMenu->options[nextOptionIndex])
  {
    lcd.setCursor(0, 1);
    displayOption(*currentMenu->options[nextOptionIndex]);
  }
}

void changeSelectedOption(int direction)
{
  currentMenu->selectedOption = (currentMenu->selectedOption + direction);
  if (currentMenu->selectedOption >= currentMenu->optionsCount)
  {
    currentMenu->selectedOption = 0;
  }
  if (currentMenu->selectedOption < 0)
  {
    currentMenu->selectedOption = currentMenu->optionsCount - 1;
  }
  displayCurrentMenu();
}

ISR(PCINT1_vect)
{
  lastEncoderAState = digitalRead(ENCODER_A_PIN);
  lastEncoderBState = digitalRead(ENCODER_B_PIN);
  encoderTime = millis();
}

void handleEncoder()
{
  int encoderA;
  int encoderB;
  unsigned long time;
  ATOMIC_BLOCK(ATOMIC_RESTORESTATE)
  {
    encoderA = lastEncoderAState;
    encoderB = lastEncoderBState;
    time = encoderTime;
  }
  if (encoderA == LOW && time > lastEncoderTime + 50UL)
  {
    if (encoderB == HIGH)
    {
      changeSelectedOption(1);
    }
    else
    {
      changeSelectedOption(-1);
    }
    lastEncoderTime = time;
  }
}

void initLCD()
{
  lcd.init();
  lcd.clear();
  lcd.backlight();
  lcd.setCursor(0, 0);
  currentMenu = &mainMenu;
  displayCurrentMenu();
}
void initButtons()
{
  pinMode(BUTTON_RED, INPUT_PULLUP);
  pinMode(BUTTON_GREEN, INPUT_PULLUP);
}

void initEncoder()
{
  pinMode(ENCODER_A_PIN, INPUT_PULLUP);
  pinMode(ENCODER_B_PIN, INPUT_PULLUP);
}

bool isPressed(int button, unsigned long *lastPressedTime, int *lastState, int *debouncedState)
{
  int currentState = digitalRead(button);
  bool pressed = false;

  if (currentState != *lastState)
  {
    *lastPressedTime = millis();
  }
  if ((millis() - *lastPressedTime) > debouncingPeriod)
  {
    if (currentState != *debouncedState)
    {
      if (currentState == LOW)
      {
        pressed = true;
      }
      *debouncedState = currentState;
    }
  }
  *lastState = currentState;
  return pressed;
}

void setup()
{
  Serial.begin(9600);
  initLCD();
  initButtons();
  initEncoder();
  lcd.createChar(0, customChar);
  PCICR |= (1 << PCIE1);
  PCMSK1 |= (1 << PCINT10);
}

void loop()
{
  handleEncoder();
  if (isPressed(BUTTON_GREEN, &lastPressedGreen, &lastStateGreen, &debouncedStateGreen))
  {
    currentMenu->options[currentMenu->selectedOption]->execute();
    displayCurrentMenu();
  }
  if (isPressed(BUTTON_RED, &lastPressedRed, &lastStateRed, &debouncedStateRed) && currentMenu != &mainMenu)
  {
    changeMenu(mainMenu);
  }
}