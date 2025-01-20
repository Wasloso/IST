using lab12.Data;
using lab12.Models;
using lab12.Repositories;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using System.IO;
using System.Linq;
using System.Threading.Tasks;

namespace lab12.Controllers
{
    [Authorize(Policy = "AdminOnly")]
    public class ArticlesController : Controller
    {
        private readonly ArticlesRepository _articlesRepository;
        private readonly CategoriesRepository _categoriesRepository;
        private readonly IWebHostEnvironment _webHostEnvironment;

        public ArticlesController(ArticlesRepository articlesRepository,CategoriesRepository categoriesRepository, IWebHostEnvironment webHostEnvironment)
        {
            _articlesRepository = articlesRepository;
            _categoriesRepository = categoriesRepository;
            _webHostEnvironment = webHostEnvironment;
        }

        public async Task<IActionResult> Index()
        {
            var articles = await _articlesRepository.GetAllArticlesAsync();
            return View(articles);
        }

        public async Task<IActionResult> Details(int? id)
        {
            var article = await _articlesRepository.GetArticleDetailsAsync(id);
            if (article == null)
            {
                return NotFound();
            }

            return View(article);
        }

        public async Task<IActionResult> Create()
        {
            var categories = await _categoriesRepository.GetAllCategoriesAsync();
            ViewData["CategoryId"] = new SelectList(categories, "Id", "Name");
            return View();
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("Id,Title,Price,ImagePath,CategoryId")] Article article, IFormFile? imageFile)
        {
            if (ModelState.IsValid)
            {
                if (imageFile != null)
                {
                    string uploadsFolder = Path.Combine(_webHostEnvironment.WebRootPath, "upload");
                    string uniqueFileName = Guid.NewGuid().ToString() + "_" + Path.GetFileName(imageFile.FileName);
                    string filePath = Path.Combine(uploadsFolder, uniqueFileName);
                    using (var fileStream = new FileStream(filePath, FileMode.Create))
                    {
                        await imageFile.CopyToAsync(fileStream);
                    }

                    article.ImagePath = "upload/" + uniqueFileName;
                }

                await _articlesRepository.AddArticleAsync(article);
                return RedirectToAction(nameof(Index));
            }

            var categories = await _categoriesRepository.GetAllCategoriesAsync();
            ViewData["CategoryId"] = new SelectList(categories, "Id", "Name", article.CategoryId);
            return View(article);
        }

        public async Task<IActionResult> Edit(int? id)
        {
            var article = await _articlesRepository.GetArticleByIdAsync(id);
            if (article == null)
            {
                return NotFound();
            }
            var categories = await _categoriesRepository.GetAllCategoriesAsync();
            ViewData["CategoryId"] = new SelectList(categories, "Id", "Name", article.CategoryId);
            return View(article);
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("Id,Title,Price,ImagePath,CategoryId")] Article article)
        {
            if (id != article.Id)
            {
                return NotFound();
            }

            if (ModelState.IsValid)
            {
                await _articlesRepository.UpdateArticleAsync(article);
                return RedirectToAction(nameof(Index));
            }

            var categories = await _categoriesRepository.GetAllCategoriesAsync();
            ViewData["CategoryId"] = new SelectList(categories, "Id", "Name", article.CategoryId);
            return View(article);
        }

        public async Task<IActionResult> Delete(int? id)
        {
            var article = await _articlesRepository.GetArticleDetailsAsync(id);
            if (article == null)
            {
                return NotFound();
            }

            return View(article);
        }

        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            var article = await _articlesRepository.GetArticleByIdAsync(id);
            if (article != null)
            {
                if (article.ImagePath != null)
                {
                    var fullPath = Path.Combine(_webHostEnvironment.WebRootPath, article.ImagePath);
                    if (System.IO.File.Exists(fullPath) && article.ImagePath != "uploads/default.jpg")
                    {
                        System.IO.File.Delete(fullPath);
                    }
                }

                await _articlesRepository.DeleteArticleAsync(article);
            }

            return RedirectToAction(nameof(Index));
        }
    }
}
