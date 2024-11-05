#include "Arduino.h"
#include <LiquidCrystal_I2C.h>
#include "ButtonManager.h"

#define BUTTON_RED 2
#define BUTTON_GREEN 4

Button *buttonRed;
Button *buttonGreen;
int counter = 0;

LiquidCrystal_I2C lcd(0x27, 16, 2);

void initLCD()
{
  lcd.init();
  lcd.clear();
  lcd.backlight();
  lcd.print(0);
}

void setup()
{
  initLCD();
  Serial.begin(9600);
  buttonRed = new Button(BUTTON_RED, 50UL, 500UL, nullptr, []()
                         { Serial.println("LONG PRESS red"); });
  buttonRed->begin();
  buttonGreen = new Button(BUTTON_GREEN, 50UL, 500UL, []()
                           { Serial.println("PRESSED green"); }, []()
                           { Serial.println("LONG PRESS green"); });
  buttonGreen->begin();
}

void loop()
{
}