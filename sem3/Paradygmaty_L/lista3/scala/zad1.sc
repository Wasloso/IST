def przetworzListe (f: (Double) => Double,list: List[Double]): List[Double] ={
    //adnotacja wymuszajaca uzycie tail recursion
    @annotation.tailrec
    def przetworzPom(f:(Double)=>Double,list:List[Double],acc:List[Double]): List[Double] ={
        list match
            case head :: tail => {
                val acc1 = acc :+ f(head);
                przetworzPom(f,tail,acc1)
            }
            case Nil => acc;
    }
    przetworzPom(f,list,List());
}

val sqr: Double => Double = (x:Double) => x*x;
val abs: Double => Double = (x:Double) => {
    if(x>=0) x;
    else -x;
}
val exp1: Double => Double = (x:Double) => Math.exp(x)-1;


val list = List(1.0,-2.0,3,-4.0,5.0,6.0,-7.5,8.3);

println(przetworzListe(sqr,list));
println(przetworzListe(abs,list));
println(przetworzListe(exp1,list));

