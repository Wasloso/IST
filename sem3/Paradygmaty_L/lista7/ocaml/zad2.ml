type 'a nlist = Koniec | Element of 'a * ('a nlist);;
type 'a llist = LKoniec | LElement of 'a * (unit -> 'a llist);;

(* dzialanie na listach typu nlist*)
let dzialanie listA listB op =
  let rec dzialanieHelp listA listB op =
    match (listA,listB) with
    | (Element(hA,tA),Element(hB,tB)) -> Element((op hA hB),dzialanieHelp tA tB op)
    | (listA,Koniec) -> listA
    | (Koniec,listB) ->listB
  in
  if op == (/) then failwith "Can't use division!" (*nie znalazlem sposobu na porownanie operatorów, to sprawdzenie nie dziala*)
  else dzialanieHelp listA listB op
;;

(* testy dla nlist *)
let nlistA = Element(1,Element(2,Element(3,Element(4,Element(5,Koniec)))));;
let nlistB = Element(6,Element(7,Element(8,Koniec)));;
let noutput1 = dzialanie nlistA nlistB (+);;
let noutput2 = dzialanie nlistB nlistA ( * );;
let noutput3 = dzialanie nlistA nlistB (-);;

(* ldzialanie na listach typu llist *)
let rec ldzialanie listA listB op = 
  match (listA,listB) with
    | (LElement(hA,nextA),LElement(hB,nextB)) -> LElement((op hA hB),fun () -> (ldzialanie (nextA()) (nextB()) op))
    | (listA,LKoniec) -> listA
    | (LKoniec, listB) -> listB
;;

(* testy dla llist *)
let llistA = LElement(1,fun () -> LElement(2,fun () ->LElement(3,fun () -> LElement(4,fun () -> LElement(5,fun () -> LKoniec)))));;
let llistB = LElement(6, fun () -> LElement(7,fun () -> LElement(7, fun () -> LKoniec)));;
let loutput1 = ldzialanie llistA llistB (+);;
let loutput2 = ldzialanie llistB llistA ( * );;
let loutput3 = ldzialanie llistA llistB (-);;





