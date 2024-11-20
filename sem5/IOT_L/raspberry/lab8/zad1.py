import sys
from time import sleep

sys.path.append("../")
from config import *


class Led:
    def __init__(self, pin):
        self.pin = pin
        self.pwm = GPIO.PWM(self.pin, 100)
        self.dutyCycle = 0
        self.pwm.start(self.dutyCycle)
        self.state = False

    def changeDutyCycle(self, up: bool):
        if up:
            self.dutyCycle = min(100, self.dutyCycle + 5)
        else:
            self.dutyCycle = max(0, self.dutyCycle - 5)
        self.pwm.ChangeDutyCycle(self.dutyCycle)

    def turnOff(self):
        self.pwm.ChangeDutyCycle(0)

    def turnOn(self):
        self.pwm.ChangeDutyCycle(self.dutyCycle)

    def __str__(self):
        return f"Led {self.pin}: " + f"{self.dutyCycle}" if self.state else "OFF"

    def __del__(self):
        self.pwm.stop()


leds = [Led(led1), Led(led2), Led(led3), Led(led4)]
currentLed = 0


def handleEncoder(channel):
    if channel == encoderLeft:
        leds[currentLed].changeDutyCycle(False)
    else:
        leds[currentLed].changeDutyCycle(True)


def handleButton(channel):
    global currentLed
    leds[currentLed].turnOff()
    if channel == buttonRed:
        currentLed = (currentLed - 1) % len(leds)
    else:
        currentLed = (currentLed + 1) % len(leds)
    leds[currentLed].turnOn()


GPIO.add_event_detect(encoderLeft, GPIO.FALLING, callback=handleEncoder, bouncetime=200)
GPIO.add_event_detect(
    encoderRight, GPIO.FALLING, callback=handleEncoder, bouncetime=200
)
GPIO.add_event_detect(buttonRed, GPIO.FALLING, callback=handleButton, bouncetime=200)
GPIO.add_event_detect(buttonGreen, GPIO.FALLING, callback=handleButton, bouncetime=200)

while True:
    sleep(0.1)
    print(leds[currentLed], end="", flush=True)
