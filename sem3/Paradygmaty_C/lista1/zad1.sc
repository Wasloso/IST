def flatten1[A](list: List[List[A]]):List[A] = {
  if list == Nil then Nil
  else list.head ++ flatten1(list.tail)
}

flatten1(List(List(1,2), List(3,4)));

