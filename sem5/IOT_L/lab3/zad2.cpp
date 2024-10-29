#include "Arduino.h"
#include <LiquidCrystal_I2C.h>

#define LED_RED 6
#define LED_GREEN 5
#define LED_BLUE 3

#define BUTTON_RED 2
#define BUTTON_GREEN 4

LiquidCrystal_I2C lcd(0x27,16,2);

int leds[] = {LED_RED, LED_GREEN, LED_BLUE};
char ledNames[] = {'r', 'g', 'b'};
unsigned long blinkPeriods[] = {900UL, 1000UL, 1100UL};
unsigned long previousBlinkTime[] = {0UL, 0UL, 0UL};
const unsigned long periodChange = 100UL;
const unsigned long debouncingPeriod = 30UL;
unsigned long lastPressedRed =0UL;
unsigned long lastPressedGreen =0UL;
int lastStateGreen = HIGH;
int lastStateRed = HIGH;
int debouncedStateGreen = HIGH;
int debouncedStateRed = HIGH;

void initRGB(){
  for(int led : leds){
    pinMode(led, OUTPUT);
    digitalWrite(led, LOW);
  }
}

void initButtons(){
  pinMode(BUTTON_RED, INPUT_PULLUP);
  pinMode(BUTTON_GREEN, INPUT_PULLUP);
}

void initLCD()
{
  lcd.init();
  lcd.clear();
  lcd.backlight();
  lcd.setCursor(0, 0);
  updateDisplay(0);
}


void changeBlinkPeriod(int selectedIdx){
    blinkPeriods[selectedIdx] = (blinkPeriods[selectedIdx] + periodChange);
    if(blinkPeriods[selectedIdx]>2000UL){
        blinkPeriods[selectedIdx] = 500UL;
    }
}

bool isPressed(int button,unsigned long *lastPressedTime,int *lastState,int* debouncedState){
    int currentState = digitalRead(button);
    bool pressed = false;

    if (currentState != *lastState)
    {
        *lastPressedTime = millis();
    }
    if((millis()-*lastPressedTime)>debouncingPeriod){
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

void blinkLEDS(){
    for (int i = 0; i < 3;i++){
        if(millis()-previousBlinkTime[i]>=blinkPeriods[i]){
            previousBlinkTime[i] = millis();
            digitalWrite(leds[i], !digitalRead(leds[i]));
        }
    }
}



void updateDisplay(int selectedIdx){
    lcd.clear();
    for (int i = 0; i < 3; i++)
    {
        int position = i*5+1;
        lcd.setCursor(position,0);
        lcd.print(ledNames[i]);
        lcd.setCursor(position, 1);
        lcd.print(blinkPeriods[i] / 1000.0);
        if (i == selectedIdx)
        {
            lcd.setCursor(position-1,0);
            lcd.print("(");
            lcd.setCursor(position + 1, 0);
            lcd.print(")");
        }
    }
}



void setup(){
    initButtons();
    initLCD();
    initRGB();
    Serial.begin(9600);
}

void loop(){
    static int selectedIdx = 0;
    if (isPressed(BUTTON_GREEN, &lastPressedGreen, &lastStateGreen, &debouncedStateGreen))
    {
        selectedIdx = (selectedIdx + 1) % 3;
        updateDisplay(selectedIdx);
    }
    if(isPressed(BUTTON_RED,&lastPressedRed,&lastStateRed,&debouncedStateRed)){
        changeBlinkPeriod(selectedIdx);
        updateDisplay(selectedIdx);
    }
    blinkLEDS();
}