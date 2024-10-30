let rec count (x, list) =
  if list = [] then 0
  else if List.hd list = x then 1 + count (x, List.tl list)
  else count (x, List.tl list);;

let rec countM (x, list) =
  match list with
  | [] -> 0
  | head :: tail ->
    if head = x then
      1 + countM (x, tail)
    else
      countM (x, tail);;

  