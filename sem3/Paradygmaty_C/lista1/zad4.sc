def sqrtList: List[Int] => List[Int] = (list: List[Int]) => {
  list.map(x=>x*x);
}

print(sqrtList(List(10,20,30)));

