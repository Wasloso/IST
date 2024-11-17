using System;

namespace zad1
{
    internal class Program
    {
        static void Main(string[] args)
        {
            Console.Write("Enter a: ");
            double a = double.Parse(Console.ReadLine());

            Console.Write("Enter b: ");
            double b = double.Parse(Console.ReadLine());

            Console.Write("Enter c: ");
            double c = double.Parse(Console.ReadLine());

            double[] results = QuadraticFormulaSolver(a, b, c);

            if (results.Length == 0)
            {
                Console.WriteLine("No solutions.");
            }
            else if (results.Length == 1)
            {
                Console.WriteLine("One solution: {0:F2}", results[0]);
            }
            else
            {
                Console.WriteLine($"Two solutions: {results[0]:F2}, {results[1]:F2}");
            }
        }

        public static double[] QuadraticFormulaSolver(double a, double b, double c)
        {
            if (a == 0)
            {
                if (b == 0)
                {
                    return new double[0];
                }
                return new double[] { -c / b };
            }

            double delta = b * b - 4 * a * c;

            if (delta < 0)
            {
                return new double[0]; 
            }
            else if (delta == 0)
            {
                return new double[] { -b / (2 * a) };
            }
            else
            {
                double sqrtDelta = Math.Sqrt(delta);
                return new double[]
                {
                    (-b - sqrtDelta) / (2 * a),
                    (-b + sqrtDelta) / (2 * a)
                }; 
            }
        }
    }
}
