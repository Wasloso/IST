import requests
import json
from dataclasses import dataclass
import sys
import os


class api:
    baseUrl: str = "http://apps.who.int/gho/athena/api/"
    formatter: str = "?format=json"
    countries: dict = {}
    dimensions: dict = {}
    indicators: dict = {}

    def getCountries(self,write=False) -> dict:
        if os.path.exists(f'data/countries.json'):
            with open(f'data/countries.json', "r", encoding="utf-8") as f:
                self.countries = json.load(f)
                return self.countries
            
        response = requests.get(f'{self.baseUrl}/COUNTRY{self.formatter}')

        if response.status_code == 200:
            data = json.loads(response.text)
            for entry in data["dimension"][0]["code"]:
                self.countries[entry["label"]] = entry["display"]
        else:
            print('No response')

        if write:
             with open("data/countries.json", "a+", encoding="utf-8") as f:
                f.truncate(0)
                json.dump(self.countries, f, indent=4)
        return self.countries


    def getDimension(self,write=False) -> dict:

        response = requests.get(f"{self.baseUrl}{self.formatter}")
        if response.status_code == 200:
            self.dimensions = json.loads(response.text)["dimension"]
        else:
            print('No response')
            return
        if write:
            with open("data/dimensions.json", "a+", encoding="utf-8") as f:
                f.truncate(0)
                json.dump(self.dimensions, f, indent=4)
        return self.dimensions

    def getIndicators(self, dimension: str,wirte=False) -> dict:
        response = requests.get(f"{self.baseUrl}/{dimension}{self.formatter}")
        if response.status_code == 200:
            responseData: dict = json.loads(response.text)
            for entry in responseData["dimension"][0]["code"]:
                self.indicators[entry["label"]] = entry["display"]
        else:
            print('No response')
            return
        if wirte:
            with open("data/indicators.json", "a+", encoding="utf-8") as f:
                f.truncate(0)
                json.dump(self.indicators, f, indent=4) 
        return self.indicators

    def getValues(self, indicator: str,wirte=False) -> dict:
        '''Get the values of a specific indicator, if wirte is True, it will write the data to a json file.\n
           If data has been already written, retrieve data from a file
           To get list of indicators, use getIndicators('GHO') method'''
        if not self.countries:
            self.getCountries()
        
        if os.path.exists(f'data/{indicator}.json') and not wirte:
            with open(f'data/{indicator}.json', "r", encoding="utf-8") as f:
                return json.load(f)

        response = requests.get(
            f"{self.baseUrl}/GHO/{indicator}{self.formatter}"
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
                if entry["value"]["numeric"]:
                    value = entry["value"]["numeric"]
                else:
                    try:
                        value = float(entry["value"]["display"])
                    except ValueError:
                        value = None
                values[country] = value
            display = responseData["dimension"][0]["code"][0]["display"]
        if wirte:
            for country in self.countries:
                if country not in values.keys():
                    values[country] = None
            self.writeIndicatorValues({"indicator": indicator, "display": display, "values": values})
        return {"indicator": indicator, "display": display, "values": values}

    def writeIndicatorValues(self, data: dict) -> None:
        if data["values"] == {}:
            return
        with open(f'data/{data["indicator"]}.json', "a+", encoding="utf-8") as f:
            f.truncate(0)
            json.dump(data, f, indent=4)
        with open("data/avaliable_2.txt", "a") as f:
            f.write(f'{data['indicator']} - {data["display"]}\n')


if __name__ == "__main__":
    api1 = api()
    countries = api1.getCountries()
    api1.getValues('SA_0000001688',wirte=True)
        
