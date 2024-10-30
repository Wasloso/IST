let palindrome list =
  list=(List.reverseList list)
;;

let a = palindrome [1;2;3;2;1]
let b = palindrome []
let c = palindrome [1;2;3]