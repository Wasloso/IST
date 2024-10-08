package com.company;

public class Merger {
        public static OneWayLinkedList<Integer> merge(
            OneWayLinkedList<Integer> list1,
            OneWayLinkedList<Integer> list2) {

        OneWayLinkedList<Integer> mergedList = new OneWayLinkedList<Integer>();
        int list1Pos = 0;
        int list2Pos = 0;
        while (list1Pos < list1.size() && list2Pos < list2.size()) {
            if (list1.get(list1Pos) < list2.get(list2Pos)) {
                mergedList.add(list1.get(list1Pos++));
            } else {
                mergedList.add(list2.get(list2Pos++));
            }
        }
        while (list1Pos < list1.size()) {
            mergedList.add(list1.get(list1Pos++));
        }
        while (list2Pos < list2.size()) {
            mergedList.add(list2.get(list2Pos++));
        }
        return mergedList;
    }
}
