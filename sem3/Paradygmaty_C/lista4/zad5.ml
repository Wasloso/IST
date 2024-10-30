type 'a graph = Graph of ('a -> 'a list);;
let g = Graph (function
  | 0 -> [3]
  | 1 -> [0;2;4]
  | 2 -> [1]
  | 3 -> []
  | 4 -> [0;2]
  | n -> failwith ("Graph g: node "^string_of_int n^" doesn't exist")
);;

let depthSearch (Graph myGraph) startNode = 
  let rec search visited queue acc =
    match queue with
    | [] -> List.rev acc
    | h::t -> if (List.mem h visited) then search visited t acc
              else search (h::visited) ((myGraph h)@ t)  (h::acc)
  in search [] [startNode] []
;;

