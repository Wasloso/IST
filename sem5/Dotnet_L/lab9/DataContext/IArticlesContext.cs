using lab9.ViewModels;

namespace lab9.DataContext
{
    public interface IArticlesContext
    {
        IEnumerable<ArticleViewModel> GetArticles();
        ArticleViewModel? GetAritcleById(int id);
        void AddArticle(ArticleViewModel article);
        void UpdateArticle(ArticleViewModel article);
        void DeleteArticle(int id);
    }
}