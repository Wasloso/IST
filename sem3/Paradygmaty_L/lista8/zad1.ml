module QuadraticEquation = struct
  let solve(a, b, c) =
    if a = 0. then [if b = 0. then c else -.c/.b] (*rownanie liniowe, lub brak rownania jesli b=0*)
    else
      let delta = b*.b -. 4.*.a*.c in
      if (delta < 0.) then failwith("No real solutions") (*brak rozwiazan w zbiorze liczb rzeczywistych*)
      else if (delta > 0.) then [(-.b-.sqrt(delta))/.(2.*.a);(-.b+.sqrt(delta))/.(2.*.a)] (*dwa rozwiazania*)
      else [-.b/.(2.*.a)] (*jedno rozwiazanie*)
end

let res1 = QuadraticEquation.solve(2.,8.,-.10.);;
let res2 = QuadraticEquation.solve(2.,4.,2.);;
let res3 = QuadraticEquation.solve(2.,1.,2.);;
let res4 = QuadraticEquation.solve(0.,2.,-.2.);;
let res5 = QuadraticEquation.solve(0.,0.,0.)