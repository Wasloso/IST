#include "Arduino.h"
#include <LiquidCrystal_I2C.h>
#include "ButtonManager.h"

#include "Arduino.h"
#include <LiquidCrystal_I2C.h>

#define LED_RED 6
#define LED_GREEN 5
#define LED_BLUE 3

#define BUTTON_RED 2
#define BUTTON_GREEN 4

LiquidCrystal_I2C lcd(0x27, 16, 2);
Button *buttonRed, *buttonGreen;

int leds[] = {LED_RED, LED_GREEN, LED_BLUE};
char ledNames[] = {'r', 'g', 'b'};
unsigned long blinkPeriods[] = {900UL, 1000UL, 1100UL};
unsigned long previousBlinkTime[] = {0UL, 0UL, 0UL};
bool ledStates[] = {false, false, false};
const unsigned long periodChange = 100UL;
int *selectedIdx = new int(0);
volatile bool updateDisplayFlag = true;

byte arrowChar[] = {
    B00000,
    B00000,
    B00100,
    B00010,
    B11111,
    B00010,
    B00100,
    B00000,
};

void initRGB()
{
  for (int led : leds)
  {
    pinMode(led, OUTPUT);
    digitalWrite(led, LOW);
  }
}

void updateDisplay(LiquidCrystal_I2C *lcd)
{
  lcd->clear();
  for (int i = 0; i < 3; i++)
  {
    int position = i * 5 + 1;
    lcd->setCursor(position, 0);
    lcd->print(ledNames[i]);
    lcd->print(ledStates[i] ? "+" : "-");
    lcd->setCursor(position, 1);
    lcd->print(blinkPeriods[i] / 1000.0);
    if (i == *selectedIdx)
    {
      lcd->setCursor(position - 1, 0);
      lcd->write(byte(0));
    }
  }
}

void initLCD()
{
  lcd.init();
  lcd.clear();
  lcd.backlight();
  lcd.setCursor(0, 0);
  lcd.createChar(0, arrowChar);
}

void changeSelectedIndex()
{
  *selectedIdx = (*selectedIdx + 1) % 3;
  updateDisplayFlag = true;
}
void bliknSelectedLED()
{
  ledStates[*selectedIdx] = !ledStates[*selectedIdx];
  digitalWrite(leds[*selectedIdx], ledStates[*selectedIdx]);
  previousBlinkTime[*selectedIdx] = millis();
  updateDisplayFlag = true;
}

void changeBlinkPeriod()
{
  blinkPeriods[*selectedIdx] = (blinkPeriods[*selectedIdx] + periodChange);
  if (blinkPeriods[*selectedIdx] > 2000UL)
  {
    blinkPeriods[*selectedIdx] = 500UL;
  }
  updateDisplayFlag = true;
}

void blinkLEDS()
{
  for (int i = 0; i < 3; i++)
  {

    if (millis() - previousBlinkTime[i] >= blinkPeriods[i])
    {
      previousBlinkTime[i] = millis();
      if (ledStates[i])
      {
        digitalWrite(leds[i], !digitalRead(leds[i]));
      }
    }
  }
}

void initButtons()
{
  buttonRed = new Button(BUTTON_RED, 50UL, 250UL, changeSelectedIndex, bliknSelectedLED);
  buttonRed->begin();
  buttonGreen = new Button(BUTTON_GREEN, 50UL, 250UL, changeBlinkPeriod, nullptr);
  buttonGreen->begin();
}

void setup()
{

  initButtons();
  initLCD();
  initRGB();
  Serial.begin(9600);
}

void loop()
{
  blinkLEDS();
  if (updateDisplayFlag)
  {
    updateDisplay(&lcd);
    updateDisplayFlag = false;
  }
}
