let [@warning "-8"] connect1 = function
  [_;_;x;_;_] -> x
;;
let [@warning "-8"] connect2 = function
  [(_,_);(x,_)] -> x
;;

