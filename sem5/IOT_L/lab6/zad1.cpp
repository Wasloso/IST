#include "Arduino.h"
#include <LiquidCrystal_I2C.h>
#include <OneWire.h>
#include <DallasTemperature.h>

#define BUTTON_RED 2
LiquidCrystal_I2C lcd(0x27, 16, 2);
OneWire oneWire(A1);
unsigned long lastCheckTime = 0UL;
unsigned long checkPeriod = 250UL;
unsigned long lastPressedRed = 0UL;
int lastStateRed = HIGH;
int debouncedStateRed = HIGH;

byte maxChar[] = {
    B00100,
    B01110,
    B11111,
    B00100,
    B00100,
    B00100,
    B00100,
    B00100,
};
byte minChar[] = {
    B00100,
    B00100,
    B00100,
    B00100,
    B00100,
    B11111,
    B01110,
    B00100,
};

enum TempType
{
    IN,
    OUT
};

struct Temps
{
    float tempIN;
    float tempOUT;
};

struct Sensors
{
    DallasTemperature sensors;
    Sensors(OneWire *OneWire) : sensors(DallasTemperature(OneWire)) {};

    void init()
    {
        sensors.begin();
    }

    Temps requestTemperatures()
    {
        sensors.requestTemperatures();
        Temps temps = {sensors.getTempCByIndex(0), sensors.getTempCByIndex(1)};
        return temps;
    }
};
void clearAndPrint(float value, int col, int row)
{
    lcd.setCursor(col, row);
    lcd.print("       ");
    lcd.setCursor(col, row);
    lcd.print(value);
}

struct TempDisplay
{
    Temps maxTemps;
    Temps minTemps;
    Temps initTemp;
    TempType currentType = IN;
    TempDisplay() {}

    void init(Temps initTemp)
    {
        lcd.init();
        lcd.clear();
        lcd.backlight();
        lcd.setCursor(0, 0);
        lcd.print(currentType == IN ? "IN:" : "OUT:");
        lcd.setCursor(0, 1);
        lcd.write(byte(1));
        lcd.setCursor(8, 1);
        lcd.write(byte(0));
        maxTemps = minTemps = initTemp;
    }
    void update(const Temps &temps)
    {
        if (temps.tempIN >= maxTemps.tempIN)
        {
            maxTemps.tempIN = temps.tempIN;
        }
        if (temps.tempOUT >= maxTemps.tempOUT)
        {
            maxTemps.tempOUT = temps.tempOUT;
        }

        if (temps.tempIN <= minTemps.tempIN)
        {
            minTemps.tempIN = temps.tempIN;
        }
        if (temps.tempOUT <= minTemps.tempOUT)
        {
            minTemps.tempOUT = temps.tempOUT;
        }
        displayTemp(temps);
    }
    void displayTemp(const Temps &temps)
    {
        clearAndPrint(currentType == IN ? temps.tempIN : temps.tempOUT, currentType == IN ? 4 : 5, 0);
        clearAndPrint(currentType == IN ? minTemps.tempIN : minTemps.tempOUT, 1, 1);
        clearAndPrint(currentType == IN ? maxTemps.tempIN : maxTemps.tempOUT, 9, 1);
    }
    void switchTempType()
    {
        currentType = currentType == IN ? OUT : IN;
        lcd.setCursor(0, 0);
        lcd.print("            ");
        lcd.setCursor(0, 0);
        lcd.print(currentType == IN ? "IN:" : "OUT:");
    }
};

Sensors sensors(&oneWire);
TempDisplay tempDisplay;

bool isPressed(int button, unsigned long *lastPressedTime, int *lastState, int *debouncedState)
{
    int currentState = digitalRead(button);
    bool pressed = false;

    if (currentState != *lastState)
    {
        *lastPressedTime = millis();
    }
    if ((millis() - *lastPressedTime) > 50UL)
    {
        if (currentState != *debouncedState)
        {
            if (currentState == HIGH)
            {
                pressed = true;
            }
            *debouncedState = currentState;
        }
    }
    *lastState = currentState;
    return pressed;
}

void setup()
{
    pinMode(BUTTON_RED, INPUT_PULLUP);
    Serial.begin(9600);
    sensors.init();
    tempDisplay.init(sensors.requestTemperatures());
    lcd.createChar(0, maxChar);
    lcd.createChar(1, minChar);
}

void loop()
{
    unsigned long currentTime = millis();
    if (currentTime > lastCheckTime + 500UL)
    {
        tempDisplay.update(sensors.requestTemperatures());
        lastCheckTime = currentTime;
    }
    if (isPressed(BUTTON_RED, &lastPressedRed, &lastStateRed, &debouncedStateRed))
    {
        tempDisplay.switchTempType();
        tempDisplay.update(sensors.requestTemperatures());
        lastCheckTime = currentTime;
    }
}