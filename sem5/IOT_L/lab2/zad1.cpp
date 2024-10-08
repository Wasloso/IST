#include <Arduino.h>
#define LED_RED 6
#define LED_GREEN 5
#define LED_BLUE 3

#define BUTTON_RED 2
#define BUTTON_GREEN 4

bool isOn = false;
int leds[] = { LED_RED, LED_GREEN, LED_BLUE };
int currentLED = 0;
bool redPressed;
bool greenPressed;
bool prevRedState;
bool prevGreenState;

void initRGB(){
  for(int led : leds){
    pinMode(led, OUTPUT);
    digitalWrite(led, LOW);
  }
}

void initButtons(){
  pinMode(BUTTON_RED, INPUT_PULLUP);
  pinMode(BUTTON_GREEN, INPUT_PULLUP);
  prevGreenState = digitalRead(BUTTON_GREEN);
  prevRedState = digitalRead(BUTTON_RED);
}

void changeColor(){
  if(!isOn)
    return;
  digitalWrite(leds[currentLED], LOW);
  currentLED = (currentLED + 1) % 3;
  digitalWrite(leds[currentLED], HIGH);
}

void turnLED(){
  isOn = !isOn;
  if (isOn){
    digitalWrite(leds[currentLED], HIGH);
  }else{
    digitalWrite(leds[currentLED], LOW);
  }
}

void setup(){
  initRGB();
  initButtons();
}


void loop(){
  bool greenState = digitalRead(BUTTON_GREEN);
  if(greenState==LOW && prevGreenState==HIGH){
    changeColor();
  }
  prevGreenState = greenState;

  bool redState = digitalRead(BUTTON_RED);
  if(redState==LOW && prevRedState==HIGH){
    turnLED();
  }
  prevRedState = redState;
  delay(30);
}


