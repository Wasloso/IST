import requests
import json
from dataclasses import dataclass


@dataclass
class api:
    baseUrl: str = "http://apps.who.int/gho/athena/api/"
    formatter: str = "?format=json"

    def getDimension(self):
        response = requests.get(f"{self.baseUrl}{self.formatter}")
        if response.status_code == 200:
            data: dict = json.loads(response.text)
            return data["dimension"]
        else:
            return response.status_code

    def getIndicators(self, dimension: str):
        response = requests.get(f"{self.baseUrl}/{dimension}{self.formatter}")
        if response.status_code == 200:
            responseData: dict = json.loads(response.text)
            indicators: dict = {}
            for entry in responseData["dimension"][0]["code"]:
                indicators[entry["label"]] = entry["display"]
            return indicators
        else:
            return {}

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

    indicators = api.getIndicators("GHO")
    for indicator in indicators.keys():
        data = api.getValues("GHO", f"{indicator}")
        api.writeIndicatorValues(data)
