(* funckja insert dziala tylko dla list posortowanych niemalejąco *)

(* funckja pomocnicza do odwrocenia listy *)
let reverse list =
  let rec reverseHelp acc = function
    | h::t -> reverseHelp (h::acc) t
    | [] -> acc
  in reverseHelp [] list
;;

let insertSortedTail list e =
  let rec insertHelper acc = function
    | h::t ->
        if e <= h then (reverse (h::e::acc)) @ t
        else insertHelper (h::acc) t
    | [] -> reverse(e :: acc)
  in
  insertHelper [] list
;;
let sortedList = [1;2;3;5;6;7;9];;
let result1 = insertSortedTail sortedList 8;;
let result2 = insertSortedTail result1 0;;
let result3 = insertSortedTail result2 4;;
let result4 = insertSortedTail result3 8;;
let result5 = insertSortedTail result4 22;;
