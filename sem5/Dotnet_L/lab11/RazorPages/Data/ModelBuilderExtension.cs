using RazorPages.Models;
using Microsoft.EntityFrameworkCore;

namespace RazorPages.Data
{
    public static class ModelBuilderExtension
    {
        public static void Seed(this ModelBuilder modelBuilder)
        {
            List<Category> categories = new List<Category>
{
                new() { Name = "Fruit" },
                new() { Name = "Toys" },
                new() { Name = "Clothing" },
                new() {Name = "Electronics"},
                new() {Name = "Vegetables" },
            };




        }
    }
}
