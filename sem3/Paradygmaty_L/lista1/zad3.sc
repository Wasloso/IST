def sumR(list:List[Double]):Double={
    if(list.isEmpty) return 0;
    else{
        if(list.length==1) return list.head;
        else{
            return list.head + sumR(list.tail)
        }
    }
}


sumR(List(10.0,20.0,50.5,40.5));
sumR(List(1.0,2.2,3.3,4.4));
sumR(List(15.0,25.0,30.9,1.3));

sumR(List());