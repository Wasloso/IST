module TupleOperations = struct
  let get_second (a,b,c) = b;;
  let sum_element (a,b,c) = a + b + c;;
  let map f (a,b,c) = (f a,f b, f c);;
  let min_max (a,b,c) = (min a (min b c), max a (max b c));;
  let all_negative (a,b,c) =
    match (a<0,b<0,c<0) with
    | (true,true,true) -> true
    | _ -> false
  ;;
end

let exampleTuple = (10,20,30);;
let second = TupleOperations.get_second exampleTuple;;
let sum = TupleOperations.sum_element exampleTuple;;
let mapped = TupleOperations.map (fun x -> x*x) exampleTuple;;
let min_max = TupleOperations.min_max exampleTuple;; 
let negative = TupleOperations.all_negative exampleTuple;;
let negative = TupleOperations.all_negative (-1,-3,-4);;