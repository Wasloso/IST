#ifndef BUTTONMANAGER_H
#define BUTTONMANAGER_H

typedef void (*Callback)();

class Button
{
private:
    int _pin;
    unsigned long _debounceTime;
    unsigned long _longPressTime;
    bool _lastState;
    unsigned long _lastPressTime;
    unsigned long _lastInterruptTime;
    Callback _onClick;
    Callback _onLongPress;
    int debouncedState;
    void handleButton();

public:
    Button(int pin, unsigned long debounceTime = 50UL, unsigned long longPressTime = 500UL, Callback onClick = nullptr, Callback onLongPress = nullptr);
    void begin();
    ~Button();
    static void handleInterrupt_pin2();
    static void handleInterrupt_pin4();
};

#endif