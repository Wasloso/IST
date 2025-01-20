using lab12.Data;
using lab12.Models;
using Microsoft.EntityFrameworkCore;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace lab12.Repositories
{
    public class CategoriesRepository
    {
        private readonly StoreDbContextIdentity _context;

        public CategoriesRepository(StoreDbContextIdentity context)
        {
            _context = context;
        }

        public async Task<List<Category>> GetAllCategoriesAsync()
        {
            return await _context.Categories.ToListAsync();
        }

        public async Task<Category?> GetCategoryByIdAsync(int? id)
        {
            if (id == null) return null;
            return await _context.Categories.FirstOrDefaultAsync(c => c.Id == id);
        }

        public async Task<bool> AddCategoryAsync(Category category)
        {
            if (category == null) return false;

            _context.Categories.Add(category);
            await _context.SaveChangesAsync();
            return true;
        }

        public async Task<bool> UpdateCategoryAsync(Category category)
        {
            if (!_context.Categories.Any(c => c.Id == category.Id))
                return false;

            _context.Update(category);
            await _context.SaveChangesAsync();
            return true;
        }

        public async Task<bool> DeleteCategoryAsync(int id)
        {
            var category = await _context.Categories.FindAsync(id);
            if (category == null) return false;

            bool isReferenced = await _context.Articles.AnyAsync(a => a.CategoryId == id);
            if (isReferenced)
                return false;

            _context.Categories.Remove(category);
            await _context.SaveChangesAsync();
            return true;
        }

    }
}
