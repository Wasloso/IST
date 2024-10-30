def root3_tail(a: Double): Double = {
  @annotation.tailrec
  def root3Tail(x: Double): Double = {
    if (math.abs(x * x * x - a) <= (1e-15) * math.abs(a)) {
      x
    } else {
      root3Tail(x + (a / (x * x) - x) / 3)
    }
  }

  if (a > 1) {
    root3Tail(a / 3)
  } else {
    root3Tail(a)
  }
}

val roo3_tailF: Double => Double = (a : Double ) => {
  @annotation.tailrec
  def root3Tail(x: Double): Double = {
    if (math.abs(x * x * x - a) <= 1e-15 * math.abs(a)) {
      x
    } else {
      root3Tail(x + (a / (x * x) - x) / 3)
    }
  }

  if (a > 1) {
    root3Tail(a / 3)
  } else {
    root3Tail(a)
  }
}
println(root3_tail(9))
val a = roo3_tailF (9)
println(a)