let rec initSegment listA listB =
  match listA,listB with
  | hA::tA,hB::tB -> hA=hB && initSegment tA tB
  | [],_ -> true
  | _,[] -> false

  
  