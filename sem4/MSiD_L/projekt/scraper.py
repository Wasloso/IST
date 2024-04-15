import requests
import json
import csv


class c_who_mortality_data:

    def __init__(self):

        # Set the file name variable and create the parameters for the API request.

        headers = {"Content-type": "application/json"}

        # Get data in JSON format and then write it to a CSV file.

        data_list = self.get_data(headers)

        print(data_list)

    def get_data(self, headers):

        # Post the data request to the BLS API. Return the resulting JSON structure.

        post = requests.post(
            "https://ghoapi.azureedge.net/api/NCDMORT3070", headers=headers
        )
        data = json.loads(post.text)
        data_list = data["value"]

        return data_list

    def get_sex(sef, sex_code):

        # Convert the sex code to a sex name.

        sex = ""

        if sex_code == "FMLE":
            sex = "Female"
        elif sex_code == "MLE":
            sex = "Male"
        elif sex_code == "BTSX":
            sex = "Both Sexes"
        else:
            sex = "Unknown"

        return sex
https://www.who.int/data/gho/info/gho-odata-api

c_who_mortality_data()
