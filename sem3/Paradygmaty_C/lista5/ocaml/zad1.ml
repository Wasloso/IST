type 'a llist = LNil | LCons of 'a * 'a llist Lazy.t;;
let rec lfrom k = LCons (k, lazy (lfrom (k+1)));;
let rec toLazyList = function
 [] -> LNil
 | x :: xs -> LCons(x, lazy (toLazyList xs))
;;
let rec ltake = function
 | (0, _) -> []
 | (_, LNil) -> []
 | (n, LCons(x, lazy xs)) -> x :: ltake(n-1, xs)
;; 


let rec lrepeat list k = 
  let rec helper list value count =
    match list, count with
    | LNil, _ -> LNil
    | LCons(value,lazy next), 0 -> helper next value k
    | _, count -> LCons(value, lazy (helper list value (count-1)))
  in helper list 0 0
;;

let lazyList = LCons(1,lazy (LCons(2,lazy (LCons(3, lazy (LCons(4,lazy (LNil))))))));;

let res = lrepeat lazyList 3;;

let lazyFib = 
  let rec helper l r =
    LCons(l+r,lazy (helper r (l+r)))
  in LCons(0,lazy (helper 1 0))
;;

let res1 = ltake(10,lazyFib);;

type 'a lBT = LEmpty | LNode of 'a * (unit ->'a lBT) * (unit -> 'a lBT);;

let lBreadth tree =
  let rec helper queue =
    match  queue with
    | [] -> LNil
    | LEmpty::t -> helper t
    | LNode(v,l,r)::t -> LCons(v,lazy (helper (t @ [l();r()])))
  in helper [tree]
;;

let rec lTree n =
  LNode(n,(fun () -> lTree (2*n)), fun () -> lTree (2*n+1))
;;

let myTree = ltake(20,(lBreadth (lTree 1)));;