let f x y z = z x (y x);;
List.fold_right (fun y x -> if y mod 2 <> 0 then y::x else x) [1;2;3;4] [];;