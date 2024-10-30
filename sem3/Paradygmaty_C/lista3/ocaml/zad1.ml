let curry3sugar f a b c = f (a,b,c);;
let curry3noSugar f = fun a -> fun b -> fun c -> f (a,b,c);;
let uncurry3sugar f (a,b,c) = f a b c;;
let uncurry3noSugar f = fun (a,b,c) -> f a b c;;


let sumProd list = 
  List.fold_left (fun (s,p) x -> (s+x,p*x)) (0,1) list
;;


(* to nie dziala, bo jak beda 2 takie same elementy w liscie to bedzie dzialac rekursywnie w nieskonczonosc*)
let rec quicksort = function
 | [] -> []
 | [x] -> [x]
 | xs -> let small = List.filter (fun y -> y < List.hd xs ) xs
         and large = List.filter (fun y -> y >= List.hd xs ) xs
 in quicksort small @ quicksort large;;

let sortedQuicksort1 = quicksort [5;4;5;3;2;1];;

(* a tu, jesli wystepuja 2 t akie same elementy, to beda one pomijane*)
let rec quicksort' = function
 | [] -> []
 | x::xs -> let small = List.filter (fun y -> y < x ) xs
            and large = List.filter (fun y -> y > x ) xs
 in quicksort' small @ (x :: quicksort' large);;

 let sortedQuicksort'1 = quicksort' [5;4;5;3;2;1];;



let insertionSort comp listToSort  =
  let rec insert element list = 
    match list with
    | [] -> [element]
    | h::t ->
      if (comp element h) then element::list
      else if (comp h element) then h::insert element t
      else h::element::t
  in
  let rec sort acc = function
    | [] -> acc
    | h::t -> sort (insert h acc) t
  in
  sort [] listToSort
;;

let stability_test_list = insertionSort (fun (k1,_) (k2,_) -> k1<k2) [(2, "Alice"); (1, "Bob"); (2, "Charlie"); (1, "David")];;

let mergeSort comp listToSort =
  let split = List.map (fun x -> [x]) listToSort in
  let rec merge list1 list2 acc =
    match list1, list2 with
    | [],[] -> acc
    | [],list2 -> (List.rev acc) @ list2
    | list1,[] -> (List.rev acc) @ list1
    | h1::t1,h2::t2 -> if (comp h1 h2) then merge t1 list2 (h1::acc)
                       else merge list1 t2 (h2::acc)
  in
  let rec sort list =
    match list with
    | a::b::[] -> merge a b []
    | a::[] -> a
    | a::b::t -> sort([merge a b []; sort t])
    | [] -> []
  in sort split
;;
let stability_test_list = mergeSort (fun (k1,_) (k2,_) -> k1<k2) [(2, "Alice"); (1, "Bob"); (2, "Charlie"); (1, "David")];;



