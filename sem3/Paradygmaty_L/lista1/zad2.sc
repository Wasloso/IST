def polacz(list:List[String],separator:String):String = {

    if(list.length!=0){
        if(list.length==1) return list.head;
        else{
         list.head+separator+polacz(list.tail,separator);
        }
    }else return "Empty list!";
}

polacz(List("ala","ma","kota"),"+");
polacz(List("ala","nie","ma","kota"),"-");
polacz(List("ala","chce","miec","kota"),"");
polacz(List(),"");

