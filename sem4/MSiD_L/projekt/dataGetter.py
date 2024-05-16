import pandas as pd
from api import api
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LinearRegression
from sklearn.metrics import mean_squared_error, mean_absolute_error
from sklearn import preprocessing


def getData():
    _api = api()
    # seatbelts = _api.getValues("RS_209")
    # print(seatbelts)
    income_level = _api.getValues("RS_576")
    print(income_level)

    # df.drop(columns="Country", inplace=True)
    # X = df.drop("Life Expectancy", axis=1)
    # y = df["Life Expectancy"]
    # X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2)
    # model = LinearRegression()
    # model.fit(X_train, y_train)
    # model.predict(X_test)
    # print(model.score(X_test, y_test))


def combineDicts(dataList: list[dict], countries: dict):
    combined = []
    for country in countries:
        countryData = {}
        countryData["Country"] = countries[country]
        for data in dataList:
            countryData[data["display"]] = data["values"].get(country, 0)
        combined.append(countryData)
    return combined


def combineIndicators(countries: dict, indicators: list[dict]):
    combined = {k: {} for k in countries.values()}
    for indicator in indicators:
        for country, value in indicator["values"].items():
            combined[countries[country]][indicator["display"]] = value
    return combined


if __name__ == "__main__":
    getData()
