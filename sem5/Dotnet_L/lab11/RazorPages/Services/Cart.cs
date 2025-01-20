using Newtonsoft.Json;
using Microsoft.AspNetCore.Http;
using System.Collections.Generic;
using System.Linq;

namespace RazorPages.Services
{
    public class Cart
    {
        private const string CartCookieKey = "article_";
        private readonly IHttpContextAccessor _httpContextAccessor;

        public Cart(IHttpContextAccessor httpContextAccessor)
        {
            _httpContextAccessor = httpContextAccessor;
        }

        public void AddItem(int articleId)
        {
            string cookieName = $"{CartCookieKey}{articleId}";
            var cuurentQuantity = _httpContextAccessor.HttpContext.Request.Cookies[cookieName];
        }


    }
}
