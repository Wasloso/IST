trait Debug{
    def debugVars(): Unit = {
        for(field <- this.getClass().getDeclaredFields()){
            field.setAccessible(true);
            var fieldName = field.getName().toString();
            var fieldType = field.getType();
            var fieldValue = field.get(this);
            println("Pole: "+fieldName+" => "+fieldType + ", " + fieldValue);
            field.setAccessible(false);
        }
        
    }
}


class Point(xv: Int,yv: Int) extends Debug {
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
p.debugVars();
var s: Shape = new Shape(3,4);
s.debugVars();
var s3: _3D_Shape = new _3D_Shape(1,2,3);
s3.debugVars();