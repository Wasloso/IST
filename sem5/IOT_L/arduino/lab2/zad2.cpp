#include <Arduino.h>
#define LED_GREEN 5
#define BUTTON_RED 2
#define BUTTON_GREEN 4

int brightness = 0;

void initRGB(){
    pinMode(LED_GREEN, OUTPUT);
    analogWrite(LED_GREEN, 0);
}

void initButtons(){
    pinMode(BUTTON_GREEN, INPUT_PULLUP);
    pinMode(BUTTON_RED, INPUT_PULLUP);
}

void setup(){
    initRGB();
    initButtons();
}

void loop(){
    if(digitalRead(BUTTON_GREEN)==LOW && brightness < 255){
        brightness = brightness+1;
    }
    if(digitalRead(BUTTON_RED)==LOW && brightness > 0){
        brightness = brightness-1;
    }
    analogWrite(LED_GREEN, brightness);
    delay(10);
}