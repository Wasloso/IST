using lab12.Models;
using Microsoft.EntityFrameworkCore;
using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
namespace lab12.Data
{
    public class StoreDbContextIdentity : IdentityDbContext
    {
        public StoreDbContextIdentity(DbContextOptions<StoreDbContextIdentity> options) : base(options) { }
        public DbSet<Article>? Articles { get; set; }
        public DbSet<Category>? Categories { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<Article>()
                .Property(a => a.Price)
                .HasPrecision(10, 2);

            modelBuilder.Entity<Article>()
                .HasOne(a => a.Category)
                .WithMany(c => c.Articles)
                .HasForeignKey(a => a.CategoryId)
                .OnDelete(DeleteBehavior.Restrict);

            base.OnModelCreating(modelBuilder);
        }


    }
}
