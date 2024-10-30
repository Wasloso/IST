def f(list: Any) = list match {
    case a::b::Nil => {
        println(a)
        println(b)
        true
    }
    case _ => false
}

println(f(1::2::3::Nil))
println(f(List(1,2,3)))
println(f((1::Nil)::(2::Nil)::Nil))
println(f((1::Nil)::(2::Nil)::Nil::Nil))
println(f((1,2)::(1,4)::Nil))
println(f(List(1,2)::List(3,4)))