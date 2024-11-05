#include "ButtonManager.h"
#include "Arduino.h"

Button *instances[2] = {nullptr, nullptr};
Button::Button(int pin, unsigned long debounceTime, unsigned long longPressTime, Callback onClick, Callback onLongPress)
    : _pin(pin), _debounceTime(debounceTime), _longPressTime(longPressTime), _lastState(HIGH), _lastPressTime(0), _onClick(onClick), _onLongPress(onLongPress), debouncedState(HIGH)
{
    if (_pin == 2)
    {
        instances[0] = this;
    }
    else if (_pin == 4)
    {
        instances[1] = this;
    }
}

Button::~Button()
{
    detachInterrupt(_pin);
}

void Button::begin()
{
    pinMode(_pin, INPUT_PULLUP);
    attachInterrupt(digitalPinToInterrupt(_pin), _pin == 2 ? handleInterrupt_pin2 : handleInterrupt_pin4, CHANGE);
}

void Button::handleInterrupt_pin2()
{
    instances[0]->handleButton();
}

void Button::handleInterrupt_pin4()
{
    instances[1]->handleButton();
}

void Button::handleButton()
{
    int buttonState = digitalRead(_pin);
    static unsigned long lastInterruptTime = 0;
    unsigned long interruptTime = millis();
    if (interruptTime - lastInterruptTime > _debounceTime)
    {
        if (debouncedState != buttonState)
        {
            if (buttonState == LOW)
            {
                _lastPressTime = interruptTime;
            }
            else
            {
                if (interruptTime - _lastPressTime > _longPressTime && _onLongPress)
                {
                    _onLongPress();
                }
                else if (_onClick)
                {
                    _onClick();
                }
            }
        }
        debouncedState = buttonState;
    }
    lastInterruptTime = interruptTime;
}