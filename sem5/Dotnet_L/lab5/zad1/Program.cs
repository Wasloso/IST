using System;
using Result = (double? nt1, double? int2, bool inf, bool nores);

namespace zad1
{
    internal class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("Enter a: ");
            string? a_str = Console.ReadLine();
            Console.WriteLine("Enter b: ");
            string? b_str = Console.ReadLine();
            Console.WriteLine("Enter c: ");
            string? c_str = Console.ReadLine();
            if(a_str is null || b_str is  null || c_str is null){
                Console.WriteLine("Provide each parameter");
                return;
            }
            if (!(double.TryParse(a_str, out double a) && double.TryParse(b_str, out double b) && double.TryParse(c_str, out double c)))
            {
                Console.WriteLine("Variables must be numbers");
                return;
            }

            var (x1,x2,inf,nores) = QuadraticFormulaSolver(a,b,c);
            if(nores){
                Console.WriteLine("No solutions");
            }
            else if(inf){
                Console.WriteLine("Infinity solutions");
            }
            else if(x2 is not null){
                Console.WriteLine($"x1 = {x1}, x2 = {x2}");
            }else{
                Console.WriteLine($"x = {x1}");
            }

            
        }

        public static Result QuadraticFormulaSolver(double a, double b, double c)
        {
            if (a == 0)
            {
                if (b == 0)
                {
                    if(c==0){
                         return (null, null, true, false);
                    }
                    return (null, null, false, true);
                }
                return (-c / b,null,false,false);
            }

            double delta = b * b - 4 * a * c;

            if (delta < 0)
            {
                return (null,null,false,true); 
            }
            else if (delta == 0)
            {
                return (-b / (2 * a),null,false,false);
            }
            
            double sqrtDelta = Math.Sqrt(delta);
            return ((-b - sqrtDelta) / (2 * a), (-b + sqrtDelta) / (2 * a), false, false);
            
        }
    }
}
