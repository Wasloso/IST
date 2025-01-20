using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using Microsoft.EntityFrameworkCore;
using RazorPages.Data;
using RazorPages.Models;

namespace RazorPages.Pages.Categories
{
    public class DeleteModel : PageModel
    {
        private readonly RazorPages.Data.StoreDbContext _context;

        public DeleteModel(RazorPages.Data.StoreDbContext context)
        {
            _context = context;
        }

        [BindProperty]
        public Category Category { get; set; } = default!;

        public async Task<IActionResult> OnGetAsync(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var category = await _context.Categories.FirstOrDefaultAsync(m => m.Id == id);

            if (category is not null)
            {
                Category = category;

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

            var category = await _context.Categories.FindAsync(id);
            if (category != null)
            {
                Category = category;
                bool isReferenced = await _context.Articles.AnyAsync(a => a.CategoryId == id);
                if (isReferenced)
                {
                    TempData["ErrorMessage"] = "Category cannot be deleted as it is associated with existing articles.";
                    return Page();
                }
             _context.Categories.Remove(Category);
                await _context.SaveChangesAsync();
            }

            return RedirectToPage("./Index");
        }
    }
}
