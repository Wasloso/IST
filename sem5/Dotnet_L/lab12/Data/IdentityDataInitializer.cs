using Microsoft.AspNetCore.Identity;

namespace lab12.Data
{
    public class IdentityDataInitializer
    {
        public static async Task SeedData(UserManager<IdentityUser> userManager, RoleManager<IdentityRole> roleManager)
        {
            Console.WriteLine("Seeding users");
           await  SeedRoles(roleManager);
           await SeedUsers(userManager);
            Console.WriteLine("Finished seeding users");

        }

        private static async Task SeedRoles(RoleManager<IdentityRole> roleManager)
        {
            var roles = new List<string>{ "Admin", "Customer" };
            foreach (string role in roles) { 
                if(await roleManager.RoleExistsAsync(role)) continue;
                var newRole = new IdentityRole
                {
                    Name = role,
                };
                var result = await roleManager.CreateAsync(newRole);
            }
        }

        private static async Task SeedUser(UserManager<IdentityUser> userManager, string name, string password, string role = null)
        {
            if (!(await userManager.FindByNameAsync(name) == null)) return;
            IdentityUser user = new IdentityUser
            {
                UserName = name,
                Email = name
            };

            IdentityResult result = await userManager.CreateAsync(user, password);
            if (result.Succeeded && role!=null)
            {
                await userManager.AddToRoleAsync(user, role);
            }
  
        }

        private static async Task SeedUsers(UserManager<IdentityUser> userManager)
        {
            await SeedUser(userManager, "admin@localhost", "AdminPass1!", "Admin");
            await SeedUser(userManager, "customer@localhost", "CustomerPass1!", "Customer");
        }
    }
}
