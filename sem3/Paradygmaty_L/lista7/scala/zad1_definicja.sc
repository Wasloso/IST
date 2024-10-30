//defnicja listy leniwej zgodna ze specyfikacja podana w liscie6
sealed trait lazyList[+A]
case object lazyNothing extends lazyList[Nothing]
case class lazyElement[A](value: A, next: () => lazyList[A]) extends lazyList[A]



def lwybierz[A](list: lazyList[A],every: Int,begin: Int): lazyList[A] = {
  def lwybierzHelp[A](list: lazyList[A],current:Int): lazyList[A] = {
    list match
      case lazyElement(value, next) => {
        // spelnienie warunku - dopisanie nowego elementu do listy i przejscie dalej
        if ((current >= begin) && ((current - begin) % every == 0)) then lazyElement(value, ()=>lwybierzHelp(next(),current+1)) 
        else lwybierzHelp(next(),current+1) // niespelnienie warunku - przejscie do nastepnego elementu
      }
      case lazyNothing => lazyNothing //pusty element - koniec dzialania
  }
  // te argumenty musza byc wieksze od 0
  if(every<0 || begin<=0){
    println("Wrong arguments");
    lazyNothing
  } else  lwybierzHelp(list,1);
}

// funkcja pomocnicza do wyswietlenia wyniku
def lazyToNormal[A](list: lazyList[A]): List[A] = {
  list match
    case lazyElement(value, next) => value::lazyToNormal(next())
    case lazyNothing => Nil
}

val list: lazyList[Int] = lazyElement(1,()=>lazyElement(2,()=>lazyElement(3,()=>lazyElement(4,()=>lazyElement(5,()=>lazyElement(6,()=>lazyNothing))))));
val output1 = lwybierz(list,2,2);
val outputList1 = lazyToNormal(output1);
println(output1);
println(outputList1);