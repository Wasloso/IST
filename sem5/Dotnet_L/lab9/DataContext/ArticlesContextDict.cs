using System.Reflection.Metadata.Ecma335;
using lab9.ViewModels;

namespace lab9.DataContext
{
    public class ArticlesContextDict : IArticlesContext
    {
        private readonly Dictionary<int, ArticleViewModel> _articles;
        public ArticlesContextDict()
        {
            _articles = [];
        }
        public void AddArticle(ArticleViewModel article)
        {
            if (_articles.Count == 0)
                article.Id = 0;
            else
                article.Id = _articles.Keys.Max(a => a) + 1;
            _articles.Add(article.Id, article);

        }

        public void DeleteArticle(int id)
        {
            _articles.Remove(id);
        }

        public ArticleViewModel? GetAritcleById(int id)
        {
            if (_articles.TryGetValue(id, out _))
            {
                return _articles[id];
            }
            return null;

        }

        public IEnumerable<ArticleViewModel> GetArticles()
        {
            return _articles.Values;
        }

        public void UpdateArticle(ArticleViewModel article)
        {
            if (_articles.ContainsKey(article.Id))
                _articles[article.Id] = article;

        }
    }
}