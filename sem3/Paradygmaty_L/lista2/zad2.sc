def dlugosc[A](x: List[A]): Int ={
    x match
        case head :: tail => 1+dlugosc(tail)
        case Nil => 0
}

println(dlugosc(List(1,2,3,4,5)));
println(dlugosc(List(List(1,2,3),List(4,5,6),List("a","b"),List())));
println(dlugosc(List()));
println(dlugosc(List("a","b","c","d","e")));