package com.company;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IntersectorTests {
    @org.junit.jupiter.api.Test
    void intersectTwoLists() {
        var list1 = new OneWayLinkedList<Integer>();
        list1.add(1);
        list1.add(2);
        list1.add(4);
        list1.add(5);
        list1.add(6);
        list1.add(8);
        list1.add(9);
        list1.add(10);

        var list2 = new OneWayLinkedList<Integer>();
        list2.add(1);
        list2.add(3);
        list2.add(4);
        list2.add(5);
        list2.add(7);
        list2.add(8);
        list2.add(9);
        list2.add(11);

        var intersectList = Intersector.intersect(list1, list2);
        assertEquals(1, intersectList.get(0));
        assertEquals(4, intersectList.get(1));
        assertEquals(5, intersectList.get(2));
        assertEquals(8, intersectList.get(3));
        assertEquals(9, intersectList.get(4));
    }
}
