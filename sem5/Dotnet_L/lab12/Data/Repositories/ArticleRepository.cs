using lab12.Data;
using lab12.Models;
using Microsoft.EntityFrameworkCore;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

public class ArticlesRepository
{
    private readonly StoreDbContextIdentity _context;

    public ArticlesRepository(StoreDbContextIdentity context)
    {
        _context = context;
    }

    public async Task<List<Article>> GetAllArticlesAsync()
    {
        return await _context.Articles.Include(a => a.Category).ToListAsync();
    }

    public async Task<Article?> GetArticleDetailsAsync(int? id)
    {
        if (id == null)
        {
            return null;
        }

        return await _context.Articles
            .Include(a => a.Category)
            .FirstOrDefaultAsync(m => m.Id == id);
    }

    public async Task<Article?> GetArticleByIdAsync(int? id)
    {
        if (id == null)
        {
            return null;
        }

        return await _context.Articles
            .Include(a => a.Category)
            .FirstOrDefaultAsync(m => m.Id == id);
    }

    public async Task<bool> ArticleExistsAsync(int id)
    {
        return await _context.Articles.AnyAsync(e => e.Id == id);
    }

    public async Task AddArticleAsync(Article article)
    {
        _context.Add(article);
        await _context.SaveChangesAsync();
    }

    public async Task UpdateArticleAsync(Article article)
    {
        _context.Update(article);
        await _context.SaveChangesAsync();
    }

    public async Task DeleteArticleAsync(Article article)
    {
        _context.Articles.Remove(article);
        await _context.SaveChangesAsync();
    }

    public async Task<List<Article>> GetArticlesPaged(int page, int take, int? categoryId = null)
    {
        var articles = _context.Articles.OrderBy(a => a.Title).Include(a => a.Category).AsQueryable();

        if (categoryId != null)
        {
            articles = articles.Where(a => a.CategoryId == categoryId);
        }

        return await articles
            .Skip((page - 1) * take).Take(take).ToListAsync();
    }
}
