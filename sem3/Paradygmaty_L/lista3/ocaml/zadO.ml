let rec polacz listA listB =
  match (listA,listB) with
  | ([],[]) -> []
  | ([],_) -> listB
  | (_,[]) -> listA
  | (headA::tailA,headB::tailB) -> headA::headB::polacz tailA tailB
;;

let wynik1 = polacz [[1;2;3];[1;5;7]] [[2;7;5];[7;5;2]];;
let wynik2 = polacz [[1;2];[3;4]] [[5;6;7];[8;8;8];[9];[8;9;10]];;
let wynik3 = polacz [7;7;7;] [1];;


