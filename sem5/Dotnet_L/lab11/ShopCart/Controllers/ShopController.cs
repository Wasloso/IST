using lab10.Data;
using lab10.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace lab10.Controllers
{
    public class ShopController : Controller
    {
        private readonly StoreDbContext _context;
        private const string CookiePrefix = "artice_";
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
                ViewBag.SelectedCategory = categoryId;
            }

            return View(await articles.ToListAsync());
        }

        public IActionResult AddToCart(int articleId, int? selectedCategoryId)
        {
            var cookieName = $"{CookiePrefix}{articleId}";
            var currentQuantity = GetQuantity(cookieName) +1;
            SaveCookie(cookieName, currentQuantity);
            return RedirectToAction("Index", new { categoryId = selectedCategoryId });
        }

        public IActionResult ModifyQuantity(int articleId, bool increase=true)
        {
            var cookieName = $"{CookiePrefix}{articleId}";
            var currentQuantity = GetQuantity(cookieName)+(increase ? 1 : -1);
            if (currentQuantity > 0) {
                SaveCookie(cookieName, currentQuantity);
            }
            else
            {
                Response.Cookies.Delete(cookieName);
            }
            return RedirectToAction("Cart");
        }
        public IActionResult RemoveFromCart(int articleId)
        {
            var cookieName = $"{CookiePrefix}{articleId}";
            Response.Cookies.Delete(cookieName);
            return RedirectToAction("Cart");
        }


        public async Task<IActionResult> Cart()
        {
            var cartItems = new List<(Article Article, int Quantity)>();
            var totalCost = 0m;

            foreach (var cookie in Request.Cookies)
            {
                if (cookie.Key.StartsWith(CookiePrefix))
                {
                    var articleId = int.Parse(cookie.Key.Replace(CookiePrefix, ""));
                    var quantity = GetQuantity(cookie.Key);

                    var article = await _context.Articles.FindAsync(articleId);
                    if (article != null)
                    {
                        cartItems.Add((article, quantity));
                        totalCost += article.Price * quantity;
                    }
                }
            }

            ViewBag.TotalCost = totalCost;
            return View(cartItems);
        }

        public void SaveCookie(string key, int value)
        {
            var cookieOptions = new CookieOptions
            {
                Expires = System.DateTime.Now.AddDays(7),
                HttpOnly = true
            };
            Response.Cookies.Append(key, value.ToString(), cookieOptions);

        }

        public int GetQuantity(string cookieName)
        {
            var currentQuantity = 0;

            var cookieValue = Request.Cookies[cookieName];
            if (!string.IsNullOrEmpty(cookieValue))
            {
                currentQuantity = int.Parse(cookieValue);
            }
            return currentQuantity;
        }

    }
}
