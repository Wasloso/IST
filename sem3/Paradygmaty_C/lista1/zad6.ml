let rec len list = 
  match list with
  | [] -> 0
  | h::t -> 1+(len list)