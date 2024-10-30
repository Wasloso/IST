let rec flatten x =
  match x with
  | [] -> []
  | head::tail -> head @ (flatten tail);;



let rec flatten1 x =
    if x = [] then []  
    else
      let head = List.hd x in
      let tail = List.tl x in
      head @ (flatten1 tail);;


flatten([["ala";"ma";"kota"];["i";"psa"]]);;
flatten1([["ala";"ma";"kota"];["i";"psa"]]);;