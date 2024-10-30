def lwybierz[A](streamList: Stream[A], every: Int, begin: Int): Stream[A] = {
  def lwybierzHelp[A](list: Stream[A], current: Int): Stream[A] =
    ((current >= begin) && ((current - begin) % every == 0), list) match {
      case (true, head #:: tail) => head #:: lwybierzHelp(tail, current + 1) // spelnienie warunku, dopisanie wartosci do wyniku
      case (false, _ #:: tail) => lwybierzHelp(tail, current + 1) // niespelnienie warunku, pominięcie wartosci
      case _ => Stream.empty // koniec dzialania
    }

  if (every <= 0 || begin <= 0) {
    println("Wrong arguments!")
    Stream.empty
  } else lwybierzHelp(streamList, 1)
}

val stream = Stream(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);

// wynik w formie Stream
val output1 = lwybierz(stream, 1, 1);
val output2 = lwybierz(stream, 5, 2);
val output3 = lwybierz(stream, 3, 3);

// uzycie Stream.toList aby wyswietlic wyniki 
println(output1.toList);
println(output2.toList);
println(output3.toList);

