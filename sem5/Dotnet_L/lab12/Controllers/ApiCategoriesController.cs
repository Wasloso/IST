using lab12.Models;
using Microsoft.AspNetCore.JsonPatch;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using lab12.Data;
using lab12.Models;
using lab12.Repositories;

namespace WebApplication1.Controllers
{
    [Route("api/category")]
    [ApiController]
    public class ApiCategoriesController : ControllerBase
    {
        private readonly CategoriesRepository _categoriesRepository;
        private readonly StoreDbContextIdentity _context;


        public ApiCategoriesController(StoreDbContextIdentity context, CategoriesRepository categoriesRepository)
        {
            _categoriesRepository = categoriesRepository;
            _context = context;
        }
        [HttpGet]
        public async Task<IActionResult> Get()
        {
            var categories = await _categoriesRepository.GetAllCategoriesAsync();
            return Ok(categories);
        }

        [HttpGet("{id}")]
        public async Task<IActionResult> Get(int id)
        {
            var category = await _categoriesRepository.GetCategoryByIdAsync(id);

            if (category == null)
            {
                return NotFound();
            }

            return Ok(category);
        }

        [HttpPost]
        public async Task<IActionResult> Post([FromBody] Category category)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }
            await _categoriesRepository.AddCategoryAsync(category);

            return CreatedAtAction(nameof(Get), new { id = category.Id }, category);
        }

        [HttpPut("{id}")]
        public async Task<IActionResult> Put(int id, [FromBody] Category category)
        {
            if (id != category.Id)
            {
                return BadRequest();
            }

            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }
            var existingCategiry = await _categoriesRepository.GetCategoryByIdAsync(id);
            if (existingCategiry==null)
            {
                return BadRequest("No category with such id");
            }
            existingCategiry.Name = category.Name;
            try
            {
                _context.Entry(existingCategiry).State = EntityState.Modified;
                await _categoriesRepository.UpdateCategoryAsync(existingCategiry);
            }
            catch (DbUpdateConcurrencyException)
            {
                return BadRequest("Error with database");
            }

            return NoContent();
        }

        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete(int id)
        {
            var category = await _categoriesRepository.GetCategoryByIdAsync(id);
            if (category == null)
            {
                return BadRequest("No such category");
            }
            if(!await _categoriesRepository.DeleteCategoryAsync(id))
            {
                return BadRequest("This category cannot be deleted as its referenced in existing articles");
            }
            return NoContent();

           
        }

     
        

    }
}