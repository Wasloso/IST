type 'a llist = LK | LE of 'a * (unit -> 'a llist);;
type 'a nlist = K | E of 'a * ('a nlist);;

(* uzywanie tutaj rekurencji ogonowej wymaga odwrocenia wyniku *)
let nRev list = 
  let rec nRevHelp list acc =
    match list with
    | K -> acc
    | E(h,t) -> nRevHelp t (E(h,acc))
  in 
  nRevHelp list K
;;

let lRev list = 
  let rec lRevHelp list acc =
    match list with
    | LK -> acc
    | LE(h,t) -> lRevHelp (t()) (LE(h,fun () -> acc))
  in 
  lRevHelp list LK
;;


let powiel list =
  let rec powielHelp list count current acc = 
    match list, count with
    | K, 0 -> nRev acc
    | E (h, t), 0 -> powielHelp t (if h > 0 then h else 0) h acc (* wartosci niedodatnie sa pomijane *)
    | _, count -> powielHelp list (count - 1) current (E (current, acc)) (* dopisywanie koeljnych elementow do acc*)
  in
  powielHelp list 0 0 K
;;

let lpowiel list =
  let rec lpowielHelp list count current acc =
    match list, count with
    | LK, 0 -> lRev acc
    | LE (h, t), 0 -> lpowielHelp (t ()) (if h > 0 then h else 0) h acc
    | _, count -> lpowielHelp list (count - 1) current (LE (current, fun () -> acc))
  in
  lpowielHelp list 0 0 LK
;;

let normalList1 = E(1,E(2,E(3,E(4,K))));;
let normalList2 = E(5,E(-1,E(6,K)));;

let normalList3 = K;;

let lazyList1 = LE(1,fun () -> LE(2,fun () ->LE(3,fun () -> LE(4,fun () -> LE(5,fun () -> LK)))));;
let lazyList2 = LE(5,fun () -> LE(-1,fun () ->LE(6,fun () -> LK)));;

let nres1 = powiel normalList1;;
let nres2 = powiel normalList2;;
let lres1 = lpowiel lazyList1;;
let lres2 = lpowiel lazyList2;;


