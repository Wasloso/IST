def len[A](list: List[A]): Int ={
    list match
        case Nil => 0
        case head :: next => 1 + len(next)
}