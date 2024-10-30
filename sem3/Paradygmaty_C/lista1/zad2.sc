def count[A](x: A, list: List[A]): Int = {
  if (list.isEmpty) 0
  else if (list.head == x) 1 + count(x, list.tail)
  else count(x, list.tail)
  
}

count(1,List(1,2,3,4,1,23,4,1));