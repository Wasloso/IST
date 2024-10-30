let fib_tail n =
  let rec fib_help n current prev =
    if n = 0 then prev
    else fib_help (n - 1) (current + prev) current
  in
  fib_help n 1 0
;;


let rec fib n =
  if n = 0 then 0
  else if n = 1 then 1
  else fib (n-1) + fib (n-2)
;;

let res1 = fib 10
let res2 = fib_tail 30