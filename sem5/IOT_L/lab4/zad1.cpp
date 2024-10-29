#include "Arduino.h"
#include <string.h>

#define LED_RED 6
#define LED_GREEN 5
#define LED_BLUE 3

#define BUTTON_RED 2
#define BUTTON_GREEN 4
String input;
String message = "Command format - \"led_name brightness\", where led_name=[red,blue,green], brightness=[0-255]";
String errorMessage = "Invalid input!";
int leds[] = {LED_RED, LED_BLUE, LED_GREEN};

void setLed(int led,int brightness){
  analogWrite(led, brightness);
}

int mapStringToLed(const String &input){  
  if(input.equalsIgnoreCase("red")){
    return LED_RED;
  }
  if (input.equalsIgnoreCase("green"))
  {
    return LED_GREEN;
  }
  if (input.equalsIgnoreCase("blue"))
  {
    return LED_BLUE;
  }
  return -1;
}

int mapStringToBrightness(const String &input){
  for(const char &c : input){
    if(c<'0' || c>'9')
      return -1;
  }
  int output = input.toInt();
  if(output<0 || output>255)
    return -1;
  return output;
}



void setup()
{
  for (int &led : leds)
  {
    pinMode(led, OUTPUT);
    analogWrite(led, 0);
  }
  Serial.begin(9600);
  Serial.println(message);
}

void loop(){
  int spacePos;
  int selectedLed;
  int selectedBrightness;
  if(Serial.available()){
      input = Serial.readString();
      spacePos = input.indexOf(" ");
      selectedLed = mapStringToLed(input.substring(0, spacePos));
      selectedBrightness = mapStringToBrightness(input.substring(spacePos + 1,input.indexOf("\n")));
      if(selectedLed != -1 && selectedBrightness!=-1)
      {
        setLed(selectedLed, selectedBrightness);
      }else{
        Serial.println(errorMessage);
        Serial.println(message);
      }
    }
}