let f3 g x = g x (g x x);;
let g y z = y * z + 1;;
f3 g 2;;