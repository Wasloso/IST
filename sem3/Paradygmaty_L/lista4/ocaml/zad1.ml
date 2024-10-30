let additionFold listToFold = List.fold_left (+) 0 listToFold 

let filterSum list element =
  let rec filterSumHelp list element acc f =
    match list with
    | [] -> List.rev acc (*odwrocenie, poniewaz nowe elementy zostaly dodawane na poczatek*)
    | h::t -> 
      match f h = element with
      | true -> filterSumHelp t element (h::acc) f
      | false -> filterSumHelp t element acc f
  in 
  filterSumHelp list element [] additionFold
;;

let list =  [[1;2;3];[4;5;6];[10;-4;-6];[15;-9];[3;3;3;3;3];[10;-5;-5]];; (*6, 15, 0, 6, 15, 0*)
let elementA = 6;;
let elementB = 15;;
let elementC = 0;;
let wynik1 = filterSum list elementA;;
let wynik2 = filterSum list elementB;;
let wynik3 = filterSum list elementC;;