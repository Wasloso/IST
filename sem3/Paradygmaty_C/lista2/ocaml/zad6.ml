let replaceNth list position replacement =
  let rec replaceNthHelp list current =
    match list with
    | [] -> []
    | h::t ->
      if current = position then replacement::t
      else h::(replaceNthHelp t (current + 1))
  in 
  replaceNthHelp list 0
;;

let res0 = replaceNth ['o';'l';'a'; 'm'; 'a'; 'k'; 'o'; 't'; 'a'] 1 's';;
