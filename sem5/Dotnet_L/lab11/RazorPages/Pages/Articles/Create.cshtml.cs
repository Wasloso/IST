using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using RazorPages.Data;
using RazorPages.Models;
using static System.Runtime.InteropServices.JavaScript.JSType;

namespace RazorPages.Pages.Articles
{
    public class CreateModel : PageModel
    {
        private readonly RazorPages.Data.StoreDbContext _context;
        private readonly IWebHostEnvironment _webHostEnvironment;
        public CreateModel(RazorPages.Data.StoreDbContext context, IWebHostEnvironment webHostEnvironment)
        {
            _context = context;
            _webHostEnvironment = webHostEnvironment;

        }

        public async Task OnGetAsync()
        {
            Categories = new SelectList(await _context.Categories.ToListAsync(), "Id", "Name");
        }

        [BindProperty]
        public Article Article { get; set; } = default!;


        public SelectList Categories { get; set; }

        // For more information, see https://aka.ms/RazorPagesCRUD.
        public async Task<IActionResult> OnPostAsync()
        {
            if (!ModelState.IsValid)
            {
                Categories = new SelectList(await _context.Categories.ToListAsync(), "Id", "Name");
                return Page();
            }
            if (Article.ImageFile != null)
            {
                string uploadsFolder = Path.Combine(_webHostEnvironment.WebRootPath, "upload");
                string uniqueFileName = Guid.NewGuid().ToString() + "_" + Path.GetFileName(Article.ImageFile.FileName);
                string filePath = Path.Combine(uploadsFolder, uniqueFileName);
                using (var fileStream = new FileStream(filePath, FileMode.Create))
                {
                    await Article.ImageFile.CopyToAsync(fileStream);
                }

                Article.ImagePath = "upload/" + uniqueFileName;
                System.Diagnostics.Debug.WriteLine(Article.ImagePath);
            }
            System.Diagnostics.Debug.WriteLine("NoImagePath");

            _context.Articles.Add(Article);
            await _context.SaveChangesAsync();

            return RedirectToPage("./Index");
        }
    }
}
