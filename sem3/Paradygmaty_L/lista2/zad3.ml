let podziel list =
  let rec pom list even odd =
    match list with
    | [] -> (even,odd)
    | head::tail ->
      match (head mod 2 = 0) with
      | true -> pom tail (head::even) odd
      | false -> pom tail even (head::odd)
  in 
  pom list [] []
;;


let wynik1 = podziel [3;6;8;9;13];;
let wynik2 = podziel [];;
let wynik3 = podziel [345;4;3;4;7;8;123;5;8;0;-1];;
let wynik4 = podziel [1;2;3;4;5;6;7;8;9;10];;

