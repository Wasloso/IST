type eval = 
  | Num of float
  | Sub 
  | Add 
  | Mul 
  | Div 
;;

(* funkcja pomocnicza aplikujaca odpowiednie operatory w standardowy sposob *)
let standardInterpreter a b = function (*keyword function automatycznie dopasowuje ostatni argument za pomoca "match" *)
     | Add -> (a +. b)
     | Sub -> (a -. b)
     | Mul -> (a *. b)
     | Div -> if b <> 0. then (a /. b) else failwith "Can't divide by 0"
     | _ -> failwith "Not an operator!"
;;


let evaluate (list: eval list) =
  let rec evaluateHelp list stack interpreter =
    match list, stack with
    | [],[result] -> result (*brak nowych elementow, a stos zawiera jeden element - koniec dzialania*)
    | Num a::tail,_ -> evaluateHelp tail (a::stack) interpreter (*liczba  odlozona na stos*)
    | op::tail,b::a::stackTail -> evaluateHelp tail ((interpreter a b op)::stackTail) interpreter (*operator oraz stos zawiera przynajmniej dwa elementy -> wykonanie dzialania*)
    | _,_ -> failwith "Something went wrong!" (*lista pusta i sa elementy na stosie/ operator a na stosie nie ma wystarczajacej liczby elementow*)
  in
  match list with
  | [] -> failwith "Empty list" 
  | list -> evaluateHelp list [] standardInterpreter
;;



let result1 = evaluate [Num 2.; Num 2.; Mul;Num 4.; Div;Num 19.;Add];; (* 2*2 / 4 + 19 *)
let result2 = evaluate [Num 5.;Add ; Num 1.];;  (* (5 + ) 1 - blad zapisu *)
let result3 = evaluate [];; 
let result4 = evaluate [Add; Mul];; (* + * - blad zapisu *)
let result5 = evaluate [Num 10.; Num 5.; Sub; Num 20.; Mul; Num 2.; Div];; (* (10 - 5) * 20 / 2 *)
let result6 = evaluate [Num 1.;Num 0.; Div];; (* dzielenie przez 0 - blad *)

