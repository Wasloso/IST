using lab12.Data;
using lab12.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Internal;
using Newtonsoft.Json;

namespace lab12.Controllers
{

    [Authorize(Policy = "NoAdminAccess")]
    public class ShopController : Controller
    {
        private readonly StoreDbContextIdentity _context;
        private const string CookiePrefix = "artice_";
        public ShopController(StoreDbContextIdentity context)
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
            var currentQuantity = GetQuantity(cookieName) + 1;
            SaveCookie(cookieName, currentQuantity);
            return RedirectToAction("Index", new { categoryId = selectedCategoryId });
        }

        public IActionResult ModifyQuantity(int articleId, bool increase = true)
        {
            var cookieName = $"{CookiePrefix}{articleId}";
            var currentQuantity = GetQuantity(cookieName) + (increase ? 1 : -1);
            if (currentQuantity > 0)
            {
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

        public async Task<List<CartItem>> GetCartItems()
        {
            List<CartItem> cartItems = [];
            foreach(var cookie in Request.Cookies)
            {
                if (!cookie.Key.StartsWith(CookiePrefix)) continue;
                var articleId = int.Parse(cookie.Key.Replace(CookiePrefix, ""));
                var quantity = GetQuantity(cookie.Key);
                var article = await _context.Articles.FindAsync(articleId);
                if (article == null) continue;
                cartItems.Add(new CartItem { Article = article, Quantity = quantity });
            }
            return cartItems;
        }

        public async Task<IActionResult> Cart()
        {
            var cartItems = await GetCartItems();
            decimal totalCost = 0;

            foreach (var cartItem in cartItems) {
                totalCost += cartItem.Article.Price * cartItem.Quantity;
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


        [HttpGet]
        [Authorize(Policy = "NoAdminAccess")]
        public async Task<IActionResult> PlaceOrder()
        {
            var cartItems = await GetCartItems();
            if (!cartItems.Any()) return RedirectToAction("Cart");
            var totalCost = cartItems.Sum(ci => ci.Article.Price * ci.Quantity);
            var viewModel = new OrderViewModel
            {
                CartItems = cartItems,
                TotalCost = totalCost,
            };


            return View(viewModel);
        }
        [HttpPost]
        [Authorize(Policy = "NoAdminAccess")]

        public async Task<IActionResult> PlaceOrder(OrderViewModel viewModel)
        {
            var cartItems = await GetCartItems();
            if (!cartItems.Any())
            {
                ModelState.AddModelError("", "Your cart is empty.");
                return RedirectToAction("Cart");
            }

            if (!ModelState.IsValid)
            {
                viewModel.CartItems = cartItems; 
                viewModel.TotalCost = cartItems.Sum(ci => ci.Article.Price * ci.Quantity);
                return View(viewModel);
            }

            foreach (var cookie in Request.Cookies.Keys.Where(k => k.StartsWith(CookiePrefix)))
            {
                Response.Cookies.Delete(cookie);
            }
            TempData["OrderViewModel"] = JsonConvert.SerializeObject(viewModel); 
            return RedirectToAction("OrderConfirmation");
        }

        [HttpGet]
        [Authorize(Policy = "NoAdminAccess")]
        public IActionResult OrderConfirmation()
        {
            if (TempData["OrderViewModel"] is string serializedViewModel)
            {
                var viewModel = JsonConvert.DeserializeObject<OrderViewModel>(serializedViewModel); 
                return View(viewModel);
            }
            return RedirectToAction("Cart"); 
        }

        [HttpPost]
        public IActionResult AddToCart(int articleId)
        {
            var cookieName = $"{CookiePrefix}{articleId}";
            var currentQuantity = GetQuantity(cookieName) + 1;
            SaveCookie(cookieName, currentQuantity);

            return Json(new { success = true, message = "Item added to cart!" });
        }

    }


}
