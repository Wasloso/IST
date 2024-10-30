def palindrome[A](list: List[A]): Boolean = {
    def rev[A](list: List[A],acc: List[A]): List[A] ={
        list match
            case Nil => acc
            case head :: next => rev(next,head::acc)
    }

    if(list==rev(list,List())) then true
    else false
}

val a = palindrome(List(1,2,3,4,3,2,1));

val b = palindrome(List(1,2,3,4,3,2));

val c = palindrome(List());

println(a);
println(b);
println(c);