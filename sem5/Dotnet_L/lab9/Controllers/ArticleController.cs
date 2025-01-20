using Microsoft.AspNetCore.Mvc;
using lab9.DataContext;
using lab9.ViewModels;

namespace lab9.Controllers
{
    public class ArticleController : Controller
    {
        private readonly IArticlesContext _context;
        public ArticleController(IArticlesContext articlesContext)
        {
            _context = articlesContext;
        }

        public ActionResult Create()
        {
            return View();
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create(ArticleViewModel article)
        {
            try
            {
                if (ModelState.IsValid)
                {
                    _context.AddArticle(article);
                }
                else
                {
                    ArticleViewModel newArticle = new ArticleViewModel();
                    newArticle.Id = article.Id;
                    newArticle.Name = article.Name;
                    newArticle.Price = article.Price;
                    newArticle.Category = article.Category;
                    newArticle.ExpiresOn = article.ExpiresOn;
                    _context.AddArticle(newArticle);

                }
                return RedirectToAction(nameof(Index));

            }
            catch
            {
                return View();
            }
        }

        public ActionResult Edit(int id)
        {
            return View(_context.GetAritcleById(id));
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit(ArticleViewModel article)
        {
            try
            {
                if (ModelState.IsValid)
                {
                    _context.UpdateArticle(article);
                }
                return RedirectToAction(nameof(Index));

            }
            catch
            {
                return View(article);
            }
        }

        public ActionResult Delete(int id)
        {
            return View(_context.GetAritcleById(id));
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Delete(int id, ArticleViewModel article)
        {
            System.Diagnostics.Debug.WriteLine("Deleting article with id: " + id);

            try
            {
                _context.DeleteArticle(id);

                return RedirectToAction(nameof(Index));
            }
            catch
            {
                return View(article);
            }
        }

        public ActionResult Index()
        {

            return View(_context.GetArticles());
        }

        public ActionResult Details(int id)
        {
            return View(_context.GetAritcleById(id));
        }

        public ActionResult Duplicate(int id)
        {
            return View(_context.GetAritcleById(id));
        }


        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Duplicate(int id, ArticleViewModel? article)
        {
            article = _context.GetAritcleById(id);
            if (article != null)
                try
                {
                    ArticleViewModel newArticle = new ArticleViewModel();
                    newArticle.Price = article.Price;
                    newArticle.Category = article.Category;
                    newArticle.ExpiresOn = article.ExpiresOn;
                    newArticle.Name = article.Name;
                    _context.AddArticle(newArticle);
                    return RedirectToAction(nameof(Index));
                }
                catch
                {
                    return View(article);
                }
            return RedirectToAction(nameof(Index));


        }

    }



}