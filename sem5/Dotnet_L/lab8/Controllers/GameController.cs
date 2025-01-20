using Microsoft.AspNetCore.Mvc;

namespace lab8.Controllers
{
    public class GameController : Controller
    {
        private static int n;
        private static int randomValue;
        private static int counter;
        private static List<int> guessList = [];
        public IActionResult Index()
        {
            return View();
        }

        public ActionResult Set(int num)
        {
            guessList.Clear();
            if (num <= 0)
            {
                n = 3;
                ViewBag.Message = $"Invalid set value, using default range = {n}";
            }
            else
            {
                n = num;
                ViewBag.Message = $"Set range to {n}";
            }
            return View();
        }

        public ActionResult Draw()
        {
            if (n == 0)
            {
                ViewBag.Message = "Please set the range first!";
                return View();
            }
            Random random = new Random();
            randomValue = random.Next(n);
            counter = 0;
            ViewBag.Message = $"A random number between 0 and {n} has been generated!";
            return View();
        }

        public ActionResult Guess(int guess)
        {
            if (n == 0)
            {
                return RedirectToAction("Draw");
            }
            string message;
            counter++;
            guessList.Add(guess);
            ViewBag.guessList = string.Join(", ", guessList);
            if (guess == randomValue)
            {
                message = $"You won in {counter} tries! The number was {randomValue}";
                n = 0;
            }
            else if (guess < randomValue)
            {
                message = $"The number is bigger than {guess}! Try again";
            }
            else
            {
                message = $"The number is smaller than {guess}! Try again";
            }
            ViewBag.Counter = counter;
            ViewBag.CounterClass = counter switch
            {
                <= 3 => "counter-small",
                <= 6 => "counter-medium",
                _ => "counter-big"
            };
            ViewBag.Won = guess == randomValue;
            ViewBag.Message = message;
            return View();
        }





    }
}
