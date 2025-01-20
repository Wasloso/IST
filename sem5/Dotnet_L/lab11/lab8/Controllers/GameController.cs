using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Http;
using System;
using System.Collections.Generic;
using System.Linq;

namespace lab8.Controllers
{
    public class GameController : Controller
    {
        private int n
        {
            get => HttpContext.Session.GetInt32(nameof(n)) ?? 0;
            set => HttpContext.Session.SetInt32(nameof(n), value);
        }

        private int randomValue
        {
            get => HttpContext.Session.GetInt32(nameof(randomValue)) ?? 0;
            set => HttpContext.Session.SetInt32(nameof(randomValue), value);
        }

        private int counter
        {
            get => HttpContext.Session.GetInt32(nameof(counter)) ?? 0;
            set => HttpContext.Session.SetInt32(nameof(counter), value);
        }

        private List<int> guessList
        {
            get
            {
                string? sessionList = HttpContext.Session.GetString($"{nameof(GameController.guessList)}");
                if (sessionList != null)
                {
                    return System.Text.Json.JsonSerializer.Deserialize<List<int>>(sessionList) ?? [];
                }
                return [];
            }
            set
            {
                HttpContext.Session.SetString($"{nameof(GameController.guessList)}", System.Text.Json.JsonSerializer.Serialize(value));
            }
        }

        public IActionResult Index()
        {
            return View();
        }

        public ActionResult Set(int num)
        {
            guessList = new List<int>();
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
            var guesses = guessList;
            guesses.Add(guess);
            guessList = guesses;
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
