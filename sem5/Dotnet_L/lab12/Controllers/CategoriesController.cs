using lab12.Models;
using lab12.Repositories;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System.Threading.Tasks;

namespace lab12.Controllers
{
    [Authorize(Policy = "AdminOnly")]
    public class CategoriesController : Controller
    {
        private readonly CategoriesRepository _repository;

        public CategoriesController(CategoriesRepository repository)
        {
            _repository = repository;
        }

        public async Task<IActionResult> Index()
        {
            var categories = await _repository.GetAllCategoriesAsync();
            return View(categories);
        }

        public async Task<IActionResult> Details(int? id)
        {
            var category = await _repository.GetCategoryByIdAsync(id);
            if (category == null)
            {
                return NotFound();
            }

            return View(category);
        }

        public IActionResult Create()
        {
            return View();
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("Id,Name")] Category category)
        {
            if (ModelState.IsValid)
            {
                await _repository.AddCategoryAsync(category);
                return RedirectToAction(nameof(Index));
            }
            return View(category);
        }

        public async Task<IActionResult> Edit(int? id)
        {
            var category = await _repository.GetCategoryByIdAsync(id);
            if (category == null)
            {
                return NotFound();
            }

            return View(category);
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("Id,Name")] Category category)
        {
            if (id != category.Id)
            {
                return NotFound();
            }

            if (ModelState.IsValid)
            {
                var success = await _repository.UpdateCategoryAsync(category);
                if (!success)
                {
                    return NotFound();
                }

                return RedirectToAction(nameof(Index));
            }

            return View(category);
        }

        public async Task<IActionResult> Delete(int? id)
        {
            var category = await _repository.GetCategoryByIdAsync(id);
            if (category == null)
            {
                return NotFound();
            }

            return View(category);
        }

        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            var success = await _repository.DeleteCategoryAsync(id);
            if (!success)
            {
                TempData["ErrorMessage"] = "Category cannot be deleted as it is associated with existing articles.";
                var category = await _repository.GetCategoryByIdAsync(id);
                return View(category);
            }

            return RedirectToAction(nameof(Index));
        }
    }
}
