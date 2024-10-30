let rec sqrtList list = 
  if list = [] then []
  else ((List.hd list) * (List.hd list)) :: sqrtList (List.tl list)
;;

sqrtList([10;20;30]);;