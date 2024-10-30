sealed trait BT[+A]
case object Empty extends BT[Nothing]
case class Node[+A](elem: A, left: BT[A], right: BT[A]) extends BT[A]

def breadthBT[A](tree: BT[A]): List[A] = {
    @annotation.tailrec
    def breathBThelp[A](queue: List[BT[A]], acc: List[A]): List[A] ={
        queue match
            case Empty::t => breathBThelp(t,acc)
            case Node[A](v,l,r)::t => breathBThelp((t++List[BT[A]](l,r)),v::acc)
            case Nil => acc.reverse
    }
    breathBThelp(List[BT[A]](tree),List[A]())
}

val tt: Node[Int] =
Node(1,Node(2,Node(4,Empty,Empty),Empty),Node(3,Node(5,Empty,Node(6,Empty,Empty)),Empty));
val l = breadthBT(tt);
print(l);