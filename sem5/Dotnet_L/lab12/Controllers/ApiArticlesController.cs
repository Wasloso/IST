using Microsoft.AspNetCore.Cors;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using lab12.Data;
using lab12.Models;
using lab12.Repositories;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.JsonPatch;
namespace lab12.Controllers
{
    [EnableCors]
    [Route("api/article")]
    [ApiController]
    public class ApiArticlesController : Controller

    {
        private IWebHostEnvironment _webHostEnvironment;
        private readonly ArticlesRepository _articlesRepository;
        private readonly CategoriesRepository _categoriesRepository;
        private readonly StoreDbContextIdentity _context;


        public ApiArticlesController(StoreDbContextIdentity context,ArticlesRepository articlesRepository, CategoriesRepository categoriesRepository, IWebHostEnvironment webHostEnvironment)
        {
            _articlesRepository = articlesRepository;
            _categoriesRepository = categoriesRepository;
            _webHostEnvironment = webHostEnvironment;
            _context = context;
        }
        [HttpGet]
        public async Task<IActionResult> Get()
        {
            var articles = await _articlesRepository.GetAllArticlesAsync();
            return Ok(articles);

        }

        [HttpGet("{id}")]
        public async Task<IActionResult> Get(int id)
        {
            var article = await _articlesRepository.GetArticleByIdAsync(id);
            if (article == null) { return NotFound(); }
            return Ok(article);
        }

        [HttpPost]
        public async Task<IActionResult> Post([FromBody] Article article)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }
            var category = await _categoriesRepository.GetCategoryByIdAsync(article.CategoryId);
            if(category == null)
            {
                return BadRequest("No such category");
            }
            await _articlesRepository.AddArticleAsync(article);

            return CreatedAtAction(nameof(Get), new { id = article.Id }, article);
        }

        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete(int id)
        {

            var article = await _articlesRepository.GetArticleByIdAsync(id);
            if (article == null)
            {
                return BadRequest("No such article");
            }
            if (article.ImagePath != null)
            {
                var fullPath = Path.Combine(_webHostEnvironment.WebRootPath, article.ImagePath);
                if (System.IO.File.Exists(fullPath) && article.ImagePath != "uploads/default.jpg")
                {
                    System.IO.File.Delete(fullPath);
                }
            }

            await _articlesRepository.DeleteArticleAsync(article);
            return Ok();
        }

        [HttpPut("{id}")]
        public async Task<IActionResult> Put(int id, [FromBody] Article article)
        {
            if (id != article.Id || article == null)
            {
                return BadRequest();
            }
            if(!ModelState.IsValid)
            { return BadRequest(ModelState); }
            var category = await _categoriesRepository.GetCategoryByIdAsync(article.CategoryId);
            if (category == null)
            {
                return BadRequest("No matching category");
            }
            var existingArticle = await _articlesRepository.GetArticleByIdAsync(id);
            if (existingArticle == null)
            {
                return BadRequest("No matching article");
            }
            existingArticle.Title = article.Title;
            existingArticle.Price = article.Price;
            existingArticle.Category = category;
            existingArticle.CategoryId = article.CategoryId;
            try
            {
                _context.Entry(existingArticle).State = EntityState.Modified;
                await _articlesRepository.UpdateArticleAsync(existingArticle);
                return Ok(existingArticle);
            }
                      
            catch (DbUpdateConcurrencyException)
            {
                return BadRequest("Error updating the article due to concurrency issue.");
            }

        }

        [HttpGet("page/{page:int}/take/{take:int}")]
        public async Task<IActionResult> GetPaged(int page = 1, int take = 10, int? categoryId = null)
        {
            var articles = await _articlesRepository.GetArticlesPaged(page, take, categoryId);
            return Ok(articles);
        }



    }
}
