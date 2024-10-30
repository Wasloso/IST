type 'a bt = Empty | Node of 'a * 'a bt * 'a bt
let tt = Node(1,
 Node(2,
 Node(4,
 Empty,
Empty
 ),
 Empty
 ),
 Node(3,
 Node(5,
 Empty,
Node(6,
 Empty,
Empty
 )
 ),
 Empty
 )
 );; 


let breadthBT tree =
  let rec breadthHelp queue acc = 
     match queue with
     | [] -> List.rev acc
     | Empty::t -> breadthHelp t acc
     | Node(value,tA,tB)::t -> breadthHelp (t@[tA;tB]) (value::acc)
  in breadthHelp [tree] []
;;

let list = breadthBT tt;;

