def replicate[A](x:A,n:Int): List[A] = {
    if (n < 0) Nil
    else n match
        case 0 => Nil
        case _ => x::replicate(x,n-1)
   
}

replicate("ala",5);
replicate(1,20);
replicate(2.0,-1);