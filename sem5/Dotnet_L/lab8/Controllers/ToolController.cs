using Microsoft.AspNetCore.Mvc;
using Result = (double? nt1, double? int2, bool inf, bool nores);

namespace lab8.Controllers;

public class ToolController : Controller
{
    public IActionResult Index()
    {
        return View();
    }

    public ActionResult Solve(double a, double b, double c)
    {
        string message;
        string cssClass;

        var (nt1, int2, inf, nores) = QuadraticFormulaSolver(a, b, c);

        if (nores)
        {
            message = "No solutions";
            cssClass = "no-solution";
        }
        else if (inf)
        {
            message = "Infinity solutions";
            cssClass = "infinity-solution";
        }
        else if (int2 is not null)
        {
            message = $"Two solutions: x1 = {nt1}, x2 = {int2}";
            cssClass = "two-solutions";
        }
        else
        {
            message = $"One solution: x = {nt1}";
            cssClass = "one-solution";
        }
        ViewBag.a = a;
        ViewBag.b = b;
        ViewBag.c = c;
        ViewBag.message = message;
        ViewBag.cssClass = cssClass;
        return View();
    }

    public static Result QuadraticFormulaSolver(double a, double b, double c)
    {
        if (a == 0)
        {
            if (b == 0)
            {
                if (c == 0)
                {
                    return (null, null, true, false);
                }
                return (null, null, false, true);
            }
            return (-c / b, null, false, false);
        }

        double delta = b * b - 4 * a * c;

        if (delta < 0)
        {
            return (null, null, false, true);
        }
        else if (delta == 0)
        {
            return (-b / (2 * a), null, false, false);
        }

        double sqrtDelta = Math.Sqrt(delta);
        return ((-b - sqrtDelta) / (2 * a), (-b + sqrtDelta) / (2 * a), false, false);

    }
}

