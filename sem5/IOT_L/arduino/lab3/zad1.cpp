#include "Arduino.h"
#include <LiquidCrystal_I2C.h>

#define BUTTON_RED 2
#define BUTTON_GREEN 4

LiquidCrystal_I2C lcd(0x27,16,2);

bool redPressed;
bool greenPressed;
bool prevRedState;
bool prevGreenState;

void initLCD()
{
  lcd.init();
  lcd.clear();
  lcd.backlight();
  lcd.print(0);
}

void initButtons()
{
  pinMode(BUTTON_RED, INPUT_PULLUP);
  pinMode(BUTTON_GREEN, INPUT_PULLUP);
}


void blink_led(int counter){
  if(counter<=0)
    return displayErrorMessage(counter);
  lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print("BLINKING ");
  lcd.print(counter);
  lcd.print("TMES");
  for (int i = 0; i < counter; i++)
  {
    digitalWrite(LED_BUILTIN, HIGH);
    delay(150);
    digitalWrite(LED_BUILTIN, LOW);
    delay(250);
  }
  updateDisplay(counter);
}

void updateDisplay(int counter){
    lcd.clear();
    lcd.print("Counter: ");
    lcd.print(counter);
}

void displayErrorMessage(int counter){
  lcd.clear();
  lcd.print("Counter: ");
  lcd.print(counter);
  lcd.setCursor(0,1);
  lcd.print("must be positive!");
  delay(3000);
  updateDisplay(counter);
}



void setup(){
    initLCD();
    initButtons();
    Serial.begin(9600);
    updateDisplay(0);

}

void loop(){
  bool greenState = digitalRead(BUTTON_GREEN);
  bool redState = digitalRead(BUTTON_RED);
  static int counter = 0;


  if (greenState == LOW && prevGreenState == HIGH)
  {
    prevGreenState = LOW;
    while (digitalRead(BUTTON_GREEN) == LOW)
    {
      if(digitalRead(BUTTON_RED)==LOW){
        prevRedState = LOW;
        return blink_led(counter);
      }
    }
    counter++;
    updateDisplay(counter);
  }
  if(redState == LOW && prevRedState == HIGH){
    prevRedState = LOW;
    while (digitalRead(BUTTON_RED) == LOW)
    {
      if(digitalRead(BUTTON_GREEN)==LOW){
        prevGreenState = LOW;
        return blink_led(counter);
      }
    }
    counter--;
    updateDisplay(counter);

  }


  prevGreenState = greenState;
  prevRedState = redState;
  delay(30);
}