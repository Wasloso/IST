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


let internalDepth tree =
  let rec internalDepthHelp q1 q2 lvl acc =
    match q1, q2 with
    | [], [] -> acc
    | [], _ -> internalDepthHelp q2 [] (lvl + 1) acc
    | Node(_, l, r)::t, q2 -> internalDepthHelp t (l::r::q2) lvl (acc+lvl)
    | Empty::t, q2 -> internalDepthHelp t q2 lvl acc
  in
  match tree with
  | Empty -> 0
  | _ -> internalDepthHelp [tree] [] 0 0;;


let externalDepth tree =
  let rec internalDepthHelp q1 q2 lvl acc =
    match q1, q2 with
    | [], [] -> acc
    | [], _ -> internalDepthHelp q2 [] (lvl + 1) acc
    | Node(_, l, r)::t, q2 -> internalDepthHelp (t) (l::r::q2) lvl acc
    | Empty::t, q2 -> internalDepthHelp t q2 lvl (acc+lvl)
  in
  match tree with
  | Empty -> 0
  | _ -> internalDepthHelp [tree] [] 0 0;;
let res2 = externalDepth tt;;