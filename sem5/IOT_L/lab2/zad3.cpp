#include "Arduino.h"
#define LED_RED 6
#define LED_GREEN 5
#define LED_BLUE 3

int leds[] = {LED_RED, LED_GREEN, LED_BLUE};
int currentLedIdx = 0;

void initRGB(){
    for(int led : leds){
        pinMode(led, OUTPUT);
        analogWrite(led, 0);
    }
}

void setup(){
    initRGB();
}

void loop(){
    int brightness = 255;
    while (brightness != 0)
    {
        analogWrite(leds[currentLedIdx], brightness);
        analogWrite(leds[(currentLedIdx + 1) % 3], 255 - brightness);
        brightness = brightness - 1;
        delay(25);
    }
    currentLedIdx = (currentLedIdx + 1) % 3;
}