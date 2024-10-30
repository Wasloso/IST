def connect[T](l:List[T]): T ={
    l match
        case List(_,_,x,_,_) => x
}
def connect2[T](l:List[(T,T)]): T ={
    l match
        case List(_,(x,_)) => x
}