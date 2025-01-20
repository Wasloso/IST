using lab9.ViewModels;

namespace lab9.DataContext
{

    public class ArticlesContextList : IArticlesContext
    {
        protected List<ArticleViewModel> _articles;
        public ArticlesContextList()
        {
            _articles = [];
        }

        public void AddArticle(ArticleViewModel article)
        {
            if (_articles.Count == 0)
                article.Id = 0;
            else
                article.Id = _articles.Max(a => a.Id) + 1;
            _articles.Add(article);
        }

        public void DeleteArticle(int id)
        {
            ArticleViewModel? articleToRemove = GetAritcleById(id);
            if (articleToRemove != null)
            {
                _articles.Remove(articleToRemove);
            }
        }

        public ArticleViewModel? GetAritcleById(int id)
        {
            return _articles.FirstOrDefault(a => a.Id == id);
        }

        public IEnumerable<ArticleViewModel> GetArticles()
        {
            return _articles;
        }

        public void UpdateArticle(ArticleViewModel article)
        {
            ArticleViewModel? articleToUpdate = _articles.FirstOrDefault(a => a.Id == article.Id);
            if (articleToUpdate != null)
            {
                _articles = _articles.Select(a => a.Id == article.Id ? article : a).ToList();
            }
        }
    }

    public class ArticlesContextListMock : ArticlesContextList
    {
        public ArticlesContextListMock()
        {
            _articles =
            [
                new ArticleViewModel
                {
                    Id = 0,
                    Name = "Apple",
                    Price = 1.99m,
                    ExpiresOn = DateTime.Now.AddDays(7),
                    Category = ArticleCategory.Food
                },
                new ArticleViewModel
                {
                    Id = 1,
                    Name = "Banana",
                    Price = 0.99m,
                    ExpiresOn = DateTime.Now.AddDays(3),
                    Category = ArticleCategory.Food
                },
                new ArticleViewModel
                {
                    Id = 2,
                    Name = "Orange",
                    Price = 2.99m,
                    ExpiresOn = DateTime.Now.AddDays(5),
                    Category = ArticleCategory.Food
                },
                new ArticleViewModel
                {
                    Id = 3,
                    Name = "Laptop",
                    Price = 999.99m,
                    ExpiresOn = DateTime.Now.AddDays(30),
                    Category = ArticleCategory.Electronics
                },
                new ArticleViewModel
                {
                    Id = 4,
                    Name = "Tablet",
                    Price = 499.99m,
                    ExpiresOn = DateTime.Now.AddDays(30),
                    Category = ArticleCategory.Electronics
                },
                new ArticleViewModel
                {
                    Id = 5,
                    Name = "Smartphone",
                    Price = 299.99m,
                    ExpiresOn = DateTime.Now.AddDays(30),
                    Category = ArticleCategory.Electronics
                },
                new ArticleViewModel
                {
                    Id = 6,
                    Name = "Book",
                    Price = 19.99m,
                    ExpiresOn = DateTime.Now.AddDays(365),
                    Category = ArticleCategory.Books
                },
                new ArticleViewModel
                {
                    Id = 7,
                    Name = "Magazine",
                    Price = 4.99m,
                    ExpiresOn = DateTime.Now.AddDays(30),
                    Category = ArticleCategory.Books
                },
                new ArticleViewModel
                {
                    Id = 8,
                    Name = "Notebook",
                    Price = 1.99m,
                    ExpiresOn = DateTime.Now.AddDays(365),
                    Category = ArticleCategory.Books
                },
            ];
        }
    }
}

