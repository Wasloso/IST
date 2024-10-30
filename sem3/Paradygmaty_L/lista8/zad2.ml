module ListOperations = struct
  let  empty : int list = [];;
  let is_empty = function
  | [] -> true
  | _ -> false
  ;;

  let length list =
    let rec lenght_rec acc = function
    | [] -> acc
    | h::t -> lenght_rec (acc+1) t
  in
  lenght_rec 0 list 
  ;;

  let sum list = 
    let rec sum_rec acc = function
    | [] -> acc
    | h::t -> sum_rec (acc+h) t
  in
  sum_rec 0 list
  ;;

  let filter_even list =
    let rec filter_even_rec acc = function
    | [] -> acc
    | h::t when h mod 2 = 0 -> filter_even_rec (h::acc) t
    | h::t -> filter_even_rec acc t
  in filter_even_rec [] list
  ;;

  let split_into_pairs list =
    let rec split_into_pairs_rec even odd = function
    | [] -> [even;odd]
    | h::t when h mod 2 = 0 -> split_into_pairs_rec (h::even) odd t
    | h::t -> split_into_pairs_rec even (h::odd) t
  in split_into_pairs_rec [] [] list
  ;;

end

let emptyList = ListOperations.empty;;
let exampleList = [1;2;3;4;5;6;7;8;9;10];;
let length = ListOperations.length(exampleList);;
let sum = ListOperations.sum(exampleList);;
let even = ListOperations.filter_even(exampleList);;
let split = ListOperations.split_into_pairs(exampleList);;