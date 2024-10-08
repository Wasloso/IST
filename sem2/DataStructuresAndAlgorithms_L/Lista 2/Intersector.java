package com.company;

import java.util.Iterator;

public class Intersector {
    public static OneWayLinkedList<Integer> intersect(
            OneWayLinkedList<Integer> list1,
            OneWayLinkedList<Integer> list2) {
        OneWayLinkedList<Integer> intersect = new OneWayLinkedList<>();

        Iterator<Integer> list1Iterator = list1.iterator();
        Integer nextValue;
        while(list1Iterator.hasNext()){
            nextValue = list1Iterator.next();
            if(list2.contains(nextValue))
                intersect.add(nextValue);
        }


        return intersect;
    }
}
