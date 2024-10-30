let sum (f,a,b) = 
  let rec sum_pom (f,a,b,acc) =
    match a=b with
    | true -> acc+f (a)
    | false -> sum_pom (f,a+1,b,acc+f (a))
  in
  if a<=b then sum_pom (f,a,b,0)
  else 0

;;
let id (x) = x;;
let cube (x) = x*x*x;;
let fact (x) = 
  let rec fact (x,acc) =
    else if x = 1 then acc
    else fact(x-1,acc*x)
  in
  if x>0 then fact(x,1)
  else 0
;;
let a = 0;;
let b = 10;;
let wynik1 = sum (id,a,b);;
let wynik2 = sum (cube,a,b);;
let wynik3 = sum (fact,a,b);;