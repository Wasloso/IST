def whileLoop (condition: =>Boolean)(exp: =>Any): Unit ={
    if(condition){
        exp;
        whileLoop(condition)(exp)
    }
}

def swap(tab: Array[Int], i: Int, j: Int): Unit = {
  val tmp = tab(i)
  tab(i) = tab(j)
  tab(j) = tmp
}

def partition(tab: Array[Int], l: Int, r: Int): (Int, Int) = {
  var i = l
  var j = r
  val pivot = tab((i+j)/2)

  while (i <= j) {
    while (tab(i) < pivot) { i = i + 1 }
    while (tab(j) > pivot) { j = j - 1 }

    if (i <= j) {
      swap(tab, i, j)
      i = i + 1
      j = j - 1
    }
  }
  (i, j)
}

def quick(tab: Array[Int], l: Int, r: Int) : Unit = {
  if (l < r) {
    val (i, j) = partition(tab, l, r)

    if (j-l < r-i) {
      quick(tab, i, r)
    } else {
      quick(tab, l, j)
    }
  }
}
  
def quicksort(tab: Array[Int]): Unit = {
  quick(tab, 0, tab.length-1)
}

