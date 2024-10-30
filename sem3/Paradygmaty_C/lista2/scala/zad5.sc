def initSegment[A](listA: List[A],listB: List[A]): Boolean ={
    (listA,listB) match
        case (Nil,_) => true
        case (_,Nil) => false
        case (hA::tA,hB::tB) => hA==hB && initSegment(tA,tB)
}

val l1 = List(1,2,3);
val l2 = List(1,2,3,4,5,6,7);
val l3 = List(1,2,3,5);

println(initSegment(l1,l2));
println(initSegment(l2,l1));
println(initSegment(l3,l2));

