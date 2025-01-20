var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.AddControllersWithViews();

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

app.UseAuthorization();

app.MapStaticAssets();



app.MapControllerRoute(
    name: "solve",
    pattern: "Tool/Solve/{a}/{b}/{c}",
    defaults: new { controller = "Tool", action = "Solve" })
    .WithStaticAssets();


app.MapControllerRoute(
    name: "set",
    pattern: "Set,{num}",
    defaults: new { controller = "Game", action = "Set" }
    );



app.MapControllerRoute(
    name: "draw",
    pattern: "Draw",
    defaults: new { controller = "Game", action = "Draw" }
    );


app.MapControllerRoute(
    name: "guess",
    pattern: "Guess,{guess}",
    defaults: new { controller = "Game", action = "Guess" }
    );



app.MapControllerRoute(
    name: "default",
    pattern: "{controller=Home}/{action=Index}/{id?}")
    .WithStaticAssets();



app.Run();
