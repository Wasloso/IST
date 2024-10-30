def g(xs: Any) ={
    xs match
        case (a::b)::c =>true;
        case _ => false
}

println(g(List(1,2,3)::List(3,4,5)));
println(g(1::2::3::Nil));