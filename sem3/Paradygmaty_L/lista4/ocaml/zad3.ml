let flattenStringTuple (a,b,c,d) = a^b^c^d;; (*funkcja pomocnicza łącząca String w krotce*)

let merge list = 
  let rec merge_help list acc f  =
    match list with
    | [] -> List.rev acc 
    | h::t -> merge_help t (f h::acc)
  in merge_help list [] flattenStringTuple

let listA = [("ala","ma","kot","a");("ala","nie","ma","kota")];;
let  listB = [("a","b","c","d");("A","B","C","D")];;
let listC = [("1","2","3","4");("abc","abc","bcd","bcd");("!!!","???","!!!","???")];;
let wynikA = merge listA;;
let wynikB = merge listB;;

let wynikC = merge listC