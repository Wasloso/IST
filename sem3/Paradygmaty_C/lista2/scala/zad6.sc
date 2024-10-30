def replaceNth[A](list: List[A],postition: Int,replacement: A): List[A] = {
    def replaceNthHelp[A](list: List[A],replacement: A,current: Int): List[A] ={
        list match
            case Nil => Nil
            case head :: next => 
                if current==postition then replacement::next
                else head::(replaceNthHelp(next,replacement,(current+1)))
    }
    
    replaceNthHelp(list,replacement,0);
}

val originalList = List(1, 2, 3, 4, 5)
val modifiedList = replaceNth(originalList, 2, 10)
println(modifiedList)  // Output: List(1, 2, 10, 4, 5)