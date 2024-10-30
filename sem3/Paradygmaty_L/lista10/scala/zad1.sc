trait Debug{
    def debugName(): Unit = {
        println("Klasa: " + this.getClass());
    }
}

class Point(val xv: Int,val yv: Int) extends Debug {
 var x: Int = xv
 var y: Int = yv
 var test: String = "test";
}

class Shape(val x: Int,val y: Int) extends  Debug {
    var this.x: Int = x;
    var this.y: Int = y;
}

class _3D_Shape(override val x: Int, override val y:Int,val z:Int) extends Shape(x,y){
    var this.x: Int = x;
    var this.y: Int = y;
    var this.z: Int = z;
}

var p: Point = new Point(1,2);
p.debugName();
var s: Shape = new Shape(3,4);
s.debugName();
var s3: _3D_Shape = new _3D_Shape(1,2,3);
s3.debugName();