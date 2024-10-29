#include "Arduino.h"
#include <LiquidCrystal_I2C.h>

#define potentiometer A0
float maxVoltage = 1023.0;
LiquidCrystal_I2C lcd(0x27, 16, 2);
int previousADC;

void initLCD()
{
    lcd.init();
    lcd.clear();
    lcd.backlight();
    lcd.setCursor(0, 0);
    lcd.print("Miernik");
    lcd.setCursor(0,1);
    lcd.print("V=");
    lcd.setCursor(6, 1);
    lcd.print("V");
    lcd.setCursor(8, 0);
    lcd.print("A0");
    lcd.setCursor(8,1);
    lcd.print("ADC=");
}

float calculateVoltage(int value){
    return value * (5.0 / maxVoltage);
}

void updateDisplay(int value, float voltage){
  lcd.setCursor(2,1);
  lcd.print(voltage);
  lcd.setCursor(12,1);
  lcd.print(value);
  if(value<10){
      lcd.print(" ");
  }
  if(value<100){
      lcd.print(" ");
  }
  if(value<1000){
      lcd.print(" ");
  }
}

void setup(){
    pinMode(potentiometer, INPUT);
    initLCD();
    Serial.begin(9600);
    previousADC = analogRead(potentiometer);
    updateDisplay(previousADC, calculateVoltage(previousADC));
}
void loop(){
    int adc = analogRead(potentiometer);
    float voltage = calculateVoltage(adc);
    Serial.print(adc);
    Serial.print("\t");
    Serial.println(voltage);
    if (adc != previousADC)
    {
        previousADC = adc;
        updateDisplay(adc,voltage);
    }
}