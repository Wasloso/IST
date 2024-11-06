#include "ButtonManager.h"
#include "Arduino.h"

Button *instances[2] = {nullptr, nullptr};
Button::Button(int pin, unsigned long debounceTime, unsigned long longPressTime, Callback onClick, Callback onLongPress)
    : _pin(pin), _debounceTime(debounceTime), _longPressTime(longPressTime), _lastState(HIGH), _lastPressTime(0), _onClick(onClick), _onLongPress(onLongPress), debouncedState(HIGH), _lastInterruptTime(0)
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
    if (_pin == 2)
    {
        attachInterrupt(digitalPinToInterrupt(_pin), handleInterrupt_pin2, CHANGE);
    }
    else if (_pin == 4)
    {
        PCICR |= (1 << PCIE2);
        PCMSK2 |= (1 << PCINT20);
    }else{
        return
    }
    pinMode(_pin, INPUT_PULLUP);
}

ISR(PCINT2_vect)
{
    instances[1]->handleInterrupt_pin4();
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
    unsigned long currentMillis = millis();

    if (currentMillis - _lastInterruptTime > _debounceTime)
    {

        if (buttonState != debouncedState)
        {
            _lastInterruptTime = currentMillis;
            debouncedState = buttonState;

            if (debouncedState == LOW)
            {
                _lastPressTime = currentMillis;
            }
            else
            {

                if (currentMillis - _lastPressTime > _longPressTime)
                {
                    if (_onLongPress != nullptr)
                    {
                        _onLongPress();
                    }
                }
                else
                {
                    if (_onClick != nullptr)
                    {
                        _onClick();
                    }
                }
            }
        }
    }
}
