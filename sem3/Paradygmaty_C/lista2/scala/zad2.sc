def fib(n: Int): Int = 
    if (n <= 0)  0
    else if (n == 1)  1
    else  fib(n - 1) + fib(n - 2)


def fib_tail(n: Int): Int = {
  def fibonacciTail(n: Int, a: Int, b: Int): Int = {
    if (n == 0) b
    else fibonacciTail(n - 1, a + b, a)
  }

  if (n <= 0) 0
  else fibonacciTail(n, 1, 0)
}
println(fib(10));
println(fib_tail(10));
