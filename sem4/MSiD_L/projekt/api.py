import requests
import json
from dataclasses import dataclass


class api:
    baseUrl: str = "http://apps.who.int/gho/athena/api/"
    formatter: str = "?format=json"
    countries: dict = {}
    dimensions: dict = {}
    indicators: dict = {}

    def getCountries(self):
        response = requests.get(f'{self.baseUrl}/COUNTRY{self.formatter}')
        if response.status_code == 200:
            data = json.loads(response.text)
            for entry in data["dimension"][0]["code"]:
                self.countries[entry["label"]] = entry["display"]
        return self.countries

        with open("data/countries.json", "a+", encoding="utf-8") as f:
            f.truncate(0)
            json.dump(self.countries, f, indent=4)

    def getDimension(self):
        response = requests.get(f"{self.baseUrl}{self.formatter}")
        if response.status_code == 200:
            self.dimensions = json.loads(response.text)["dimension"]
        with open("data/dimensions.json", "a+", encoding="utf-8") as f:
            f.truncate(0)
            json.dump(self.dimensions, f, indent=4)
        return self.dimensions

    def getIndicators(self, dimension: str):
        response = requests.get(f"{self.baseUrl}/{dimension}{self.formatter}")
        if response.status_code == 200:
            responseData: dict = json.loads(response.text)
            for entry in responseData["dimension"][0]["code"]:
                self.indicators[entry["label"]] = entry["display"]
        with open("data/indicators.json", "a+", encoding="utf-8") as f:
            f.truncate(0)
            json.dump(self.indicators, f, indent=4)
        return self.indicators

    def getValues(self, dimension: str, indicator: str):
        response = requests.get(
            f"{self.baseUrl}/{dimension}/{indicator}{self.formatter}"
        )
        display = "No data avaliable"
        values: dict = {}
        if response.status_code == 200:
            responseData: dict = json.loads(response.text)
            if responseData["fact"] == []:
                return {"indicator": indicator, "display": display, "values": values}
            for entry in responseData["fact"]:
                country = ""
                for dim in entry["Dim"]:
                    if dim["category"] == "COUNTRY":
                        country = dim["code"]
                        break
                if not country:
                    continue
                value = (
                    entry["value"]["numeric"]
                    if entry["value"]["numeric"]
                    else entry["value"]["display"]
                )
                values[country] = value
            display = responseData["dimension"][0]["code"][0]["display"]
        for country in self.countries:
            if country not in values.keys():
                values[country] = None
        return {"indicator": indicator, "display": display, "values": values}

    def writeIndicatorValues(self, data: dict):
        if data["values"] == {}:
            return
        with open(f'data/{data["indicator"]}.json', "a+", encoding="utf-8") as f:
            f.truncate(0)
            json.dump(data, f, indent=4)
        with open("data/avaliable.txt", "a") as f:
            f.write(f'{data['indicator']} - {data["display"]}\n')


if __name__ == "__main__":
    api = api()

    indicators = api.getCountries()
