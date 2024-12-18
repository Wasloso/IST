using lab10.Data;
using lab10.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace lab10.Controllers
{
    public class ShopController : Controller
    {
        private readonly StoreDbContext _context;
        public ShopController(StoreDbContext context)
        {
            _context = context;
        }


        public async Task<IActionResult> Index(int? categoryId)
        {
            var categories = await _context.Categories
                .OrderBy(c => c.Name)
                .ToListAsync();
            ViewBag.Categories = categories;

            var articles = _context.Articles.Include(a => a.Category).AsQueryable();
            if (categoryId != null)
            {
                articles = articles.Where(a => a.CategoryId == categoryId);
            }

            return View(await articles.ToListAsync());
        }

    }
}
