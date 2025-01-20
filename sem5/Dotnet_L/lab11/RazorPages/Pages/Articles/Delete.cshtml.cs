using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using Microsoft.EntityFrameworkCore;
using RazorPages.Data;
using RazorPages.Models;

namespace RazorPages.Pages.Articles
{
    public class DeleteModel : PageModel
    {
        private readonly RazorPages.Data.StoreDbContext _context;
        private readonly IWebHostEnvironment _webHostEnvironment;

        public DeleteModel(RazorPages.Data.StoreDbContext context, IWebHostEnvironment webHostEnvironment)
        {
            _context = context;
            _webHostEnvironment = webHostEnvironment;
        }

        [BindProperty]
        public Article Article { get; set; } = default!;

        public async Task<IActionResult> OnGetAsync(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var article = await _context.Articles
                .Include(a => a.Category)
                .FirstOrDefaultAsync(m => m.Id == id); ;

            if (article is not null)
            {
                Article = article;

                return Page();
            }

            return NotFound();
        }

        public async Task<IActionResult> OnPostAsync(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var article = await _context.Articles.FindAsync(id);
            if (article != null)
            {
                Article = article;
                if (article.ImagePath != null)
                {
                    var fullPath = Path.Combine(_webHostEnvironment.WebRootPath, article.ImagePath);
                    if (System.IO.File.Exists(fullPath) && article.ImagePath != "uploads/default.jpg")
                    {
                        System.IO.File.Delete(fullPath);
                    }
                }
                _context.Articles.Remove(Article);
                await _context.SaveChangesAsync();
            }

            return RedirectToPage("./Index");
        }
    }
}
