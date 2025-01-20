using Microsoft.AspNetCore.Mvc.RazorPages;
using Microsoft.EntityFrameworkCore;
using RazorPages.Data;
using RazorPages.Models;
using System.Collections.Generic;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;

namespace RazorPages.Pages.Shop
{
    public class ShopModel : PageModel
    {
        private readonly StoreDbContext _context;

        public ShopModel(StoreDbContext context)
        {
            _context = context;
        }

        public List<Category> Categories { get; set; }
        public List<Article> Articles { get; set; }

        [BindProperty(SupportsGet = true)]
        public int? CategoryId { get; set; }

        public async Task OnGetAsync()
        {
            Categories = await _context.Categories
                .OrderBy(c => c.Name)
                .ToListAsync();

            var articlesQuery = _context.Articles.Include(a => a.Category).AsQueryable();

            if (CategoryId != null)
            {
                articlesQuery = articlesQuery.Where(a => a.CategoryId == CategoryId);
            }

            Articles = await articlesQuery.ToListAsync();
        }


    }
}
