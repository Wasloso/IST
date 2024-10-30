let root3 a =
  let epsilon = 1e-15 in
  let rec root3Help x =
    let left = abs_float(x ** 3. -. a) in
    let right = epsilon *. abs_float(a) in
    if left <= right then x
    else root3Help (x +. (a /. x ** 2. -. x) /. 3.)
  in
  if a > 1. then
    root3Help (a /. 3.)
  else
    root3Help a
;;

let result = root3 8.;;

