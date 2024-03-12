import wikipedia

article = input("Enter article name: ")
page = wikipedia.page(article)
print(f"Summary: \n{page.summary}")
print(f"url: {page.url}")
