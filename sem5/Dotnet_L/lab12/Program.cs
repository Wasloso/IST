using System.Globalization;
using lab12.Data;
using Microsoft.EntityFrameworkCore;
using Microsoft.AspNetCore.Identity;
using lab12.Repositories;

namespace lab12
{
    public class Program
    {
        public static void Main(string[] args)
        {
            var builder = WebApplication.CreateBuilder(args);

            builder.Services.AddControllersWithViews()
                    .AddNewtonsoftJson(options =>
                    {
                        options.SerializerSettings.ReferenceLoopHandling = Newtonsoft.Json.ReferenceLoopHandling.Ignore;
                    });

            // Add services to the container.
            builder.Services.AddControllersWithViews();
            builder.Services.AddDbContextPool<StoreDbContextIdentity>(options => options.UseSqlServer(builder.Configuration.GetConnectionString("IdentityDB")));

            builder.Services.AddDefaultIdentity<IdentityUser>(options =>
            {
                options.SignIn.RequireConfirmedAccount = false;
                options.Password.RequireLowercase = false;
                options.Password.RequireUppercase = false;
            })
                .AddRoles<IdentityRole>()  //add the role service.  
                .AddEntityFrameworkStores<StoreDbContextIdentity>();

            builder.Services.AddAuthorization(options =>
            {
                options.AddPolicy("AdminOnly", policy => policy.RequireRole("Admin"));
                options.AddPolicy("NoAdminAccess", policy => policy.RequireAssertion(context =>
                    !context.User.IsInRole("Admin")
                ));
            });


            builder.Services.AddScoped<ArticlesRepository>();
            builder.Services.AddScoped<CategoriesRepository>();
            builder.Services.AddControllersWithViews();

            var cultureInfo = new CultureInfo("pl-PL");
            CultureInfo.DefaultThreadCurrentCulture = cultureInfo;
            CultureInfo.DefaultThreadCurrentUICulture = cultureInfo;



            var app = builder.Build();

            // Configure the HTTP request pipeline.
            if (!app.Environment.IsDevelopment())
            {
                app.UseExceptionHandler("/Home/Error");
                // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
                app.UseHsts();
            }

            app.UseHttpsRedirection();
            app.UseRouting();
            app.UseCors();

            app.UseAuthorization();


            using(var scope = app.Services.CreateScope())
            {
                var userManager = scope.ServiceProvider.GetRequiredService<UserManager<IdentityUser>>();
                var roleManager = scope.ServiceProvider.GetRequiredService<RoleManager<IdentityRole>>();
                IdentityDataInitializer.SeedData(userManager, roleManager).Wait();
            }

            app.MapStaticAssets();
            app.MapControllerRoute(
                name: "default",
                pattern: "{controller=Shop}/{action=Index}/{id?}")
                .WithStaticAssets();
            app.MapRazorPages()
              .WithStaticAssets();

            app.Run();
        }
    }
}
