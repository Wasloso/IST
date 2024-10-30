sealed trait Tree[+A] //keyword sealed informuje kompilator że wszystkie podklasy Tree zostaly zdefiniiwane w tym pliku
case object Empty extends Tree[Nothing] // pusty wezel - liść
case class Node[A](value: A, left: Tree[A], right: Tree[A]) extends Tree[A] //węzeł zawierajacy: wartość,podwezel lewy,podwezel prawy

def sumTree(t: Tree[Int]): Int = {
    @annotation.tailrec
    def sumTreeHelp(list: List[Tree[Int]],acc: Int): Int = {
        list match
            case Node(value,left,right)::tail => sumTreeHelp(left::right::tail,acc+value) //dodanie wartosci wezla do acc oraz odlozenie podwezlow do listy
            case Empty::tail => sumTreeHelp(tail,acc) //pusty wezel pomijany
            case Nil => acc // pusta lista - zwrocenie wyniku
    }
    sumTreeHelp(List(t),0); //przetworzenie drzewa za pomoca funkcji pomocniczej wykorzystujac listę
}

val wykladTree: Tree[Int] = Node(1,Node(2,Node(4,Empty,Empty),Empty),Node(3,Node(5,Empty,Node(6,Empty,Empty)),Empty));
val myTree: Tree[Int] = Node(6,Node(3,Node(1,Node(7,Empty,Empty),Empty),Node(10,Empty,Empty)),Node(7,Node(9,Empty,Node(6,Node(12,Empty,Empty),Empty)),Empty));
val emptyTree: Tree[Int] = Empty;

println("Suma drzewa z wykladu: " + sumTree(wykladTree));
println("Suma myTree: " + sumTree(myTree));
println("Suma emptyTree: " + sumTree(emptyTree));


