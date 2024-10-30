def sum (f: Int=>Int,a: Int, b:Int ): Int ={
    @annotation.tailrec
    def sum_help(f: Int=>Int,a: Int, b:Int, acc: Int): Int ={
        a<=b match
           case true => sum_help(f,a+1,b,acc+f(a))
            case false => acc
    }
    if(a<=b) sum_help(f,a,b,0)
    else 0
}
val id: Int=>Int = x => x;
val cube: Int=>Int = x => x*x*x;
val fact: Int=>Int = x => 
    @annotation.tailrec
    def fact_help (x: Int, acc: Int): Int =
        if x<=0 then 0
        else if x==1 then acc
        else fact_help(x-1,acc*x)
    fact_help(x,1);

val a=0;
val b=10;
//z funckjami wyzszego rzedu
println(sum(id,a,b));
println(sum(cube,a,b));
println(sum(fact,a,b));
//z anonimowymi
println(sum((x: Int) => x,a,b));
println(sum((x:Int)=>x*x*x,a,b));
println(sum((x:Int)=>{
    def fact_help (x: Int, acc: Int): Int =
        if x<=0 then 0
        else if x==1 then acc
        else fact_help(x-1,acc*x)
    fact_help(x,1);
},a,b));

