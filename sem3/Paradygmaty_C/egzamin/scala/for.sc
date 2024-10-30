def f(c: =>Boolean)(e: Unit){if(c){e;f(c)(e);}}
var i=0;
f(i<5){print(i+" ");i+=2}