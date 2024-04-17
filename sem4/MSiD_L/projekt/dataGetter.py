import pandas as pd
from api import api
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LinearRegression
from sklearn.metrics import mean_squared_error, mean_absolute_error
from sklearn import preprocessing


def getData():
    _api = api()
    countries = _api.getCountries()
    life_expectancy = _api.getValues("WHOSIS_000001")
    pop_total = _api.getValues("RS_1845")
    alcohol_per_capita = _api.getValues("SA_0000001688")
    doctors_per_10k = _api.getValues("HWF_0001")
    hdi = _api.getValues("CCO_2")
    vit_A = _api.getValues("vita")
    comb = combineDicts(
        [
            life_expectancy,
            pop_total,
            alcohol_per_capita,
            doctors_per_10k,
            hdi,
            vit_A,
        ],
        countries,
    )
    df = pd.DataFrame.from_dict(comb)
    df = df[df["Life expectancy at birth (years)"] != 0]
    df.rename(
        {
            "Life expectancy at birth (years)": "Life Expectancy",
            '"Alcohol, total per capita (15+) consumption (in litres of pure alcohol) (SDG Indicator 3.5.2)"': "Alcohol pc",
            "Medical doctors (per 10 000 population)": "Doctors per 10k",
            "Human development index rank": "HDIR",
            "Children aged 6-59 months who received vitamin A supplementation (%)": "Vitamin A",
        },
        axis=1,
        inplace=True,
    )
    # df.drop(columns="Country", inplace=True)
    # X = df.drop("Life Expectancy", axis=1)
    # y = df["Life Expectancy"]
    # X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2)
    # model = LinearRegression()
    # model.fit(X_train, y_train)
    # model.predict(X_test)
    # print(model.score(X_test, y_test))

    df.to_csv("data.csv", sep=",", encoding="utf-8")
    print(df)


def combineDicts(dataList: list[dict], countries: dict):
    combined = []
    for country in countries:
        countryData = {}
        countryData["Country"] = countries[country]
        for data in dataList:
            countryData[data["display"]] = data["values"].get(country, 0)
        combined.append(countryData)
    return combined


if __name__ == "__main__":
    getData()
