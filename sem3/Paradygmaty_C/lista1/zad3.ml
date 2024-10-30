let rec replicate (x,n) =
  match n with
  | 0 -> []
  | _ -> [x] @ replicate(x,n-1)
;;

let rec replicate1 x n =
  if n==0 then []
  else x::replicate(x,n-1)
;;

let val1 = replicate1 1 20;