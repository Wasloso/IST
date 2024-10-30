let decToHex number =
  let rec decToHexHelp number acc =
    match number with
    | 0 -> acc
    | _ ->
      let rem = number mod 16 in
      let nextNum = (number - rem)/16 in
      decToHexHelp nextNum (rem::acc)
  in
  if number < 0 then decToHexHelp (number * -1) [] (*w przypadku liczby umjemnej zmiana znaku*)
  else decToHexHelp number []
;;

let wynikA = decToHex 31;;
let wynikB = decToHex 128;;
let wynikC = decToHex 54340849;;