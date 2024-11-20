import sys

sys.path.append("..")
from config import *
import withermasensor
import board
import busio
import adafruit_bme280.advanced as adafruit_bme280


class ds18b20:
    def __init__(self):
        self.sensor = withermasensor.WithermaSensor()

    def getTemperature(self):
        return self.sensor.get_temperature()


class bme280:
    def __init__(self):
        self.i2c = busio.I2C(board.SCL, board.SDA)
        self.bme280 = adafruit_bme280.Adafruit_BME280_I2C(self.i2c, 0x76)
        self.bme280.sea_level_pressure = 1013.25
        self.bme280.standby_period = adafruit_bme280.STANDBY_TC_500
        self.bme280.iir_filter = adafruit_bme280.IIR_FILTER_X16

    def getTemperature(self):
        self.bme280.overscan_temperature = adafruit_bme280.OVERSCAN_X2
        return self.bme280.temperature

    def getPressure(self):
        self.bme280.overscan_pressure = adafruit_bme280.OVERSCAN_X16
        return self.bme280.pressure

    def getHumidity(self):
        self.bme280.overscan_humidity = adafruit_bme280.OVERSCAN_X1
        return self.bme280.humidity


def calculateHeight(pressure, seaLevelPressure=1013.25):
    return 44330 * (1 - (pressure / seaLevelPressure) ** (1 / 5.255))


def main():
    bme = bme280()
    temperature = bme.getTemperature()
    pressure = bme.getPressure()
    humidity = bme.getHumidity()
    height = calculateHeight(pressure)

    print(f"Temperature: {temperature} C")
    print(f"Pressure: {pressure} hPa")
    print(f"Humidity: {humidity} %")
    print(f"Height: {height} m")


if __name__ == "__main__":
    main()
