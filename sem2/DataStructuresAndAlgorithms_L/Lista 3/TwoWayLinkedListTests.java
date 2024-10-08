package com.company;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class TwoWayLinkedListTests {
    private TwoWayLinkedList<String> list;

    @org.junit.jupiter.api.BeforeEach
    public void beforeEach() {
        list = new TwoWayLinkedList<String>();
    }

    @org.junit.jupiter.api.Test
    void addToEmptyList() {
        list.add("a");
        assertListContent(new String[] {"a"});
    }

    @org.junit.jupiter.api.Test
    void addToNotEmptyList() {
        list.add("a");
        list.add("b");
        list.add("c");

        assertListContent(new String[] {"a", "b", "c"});
    }

    @org.junit.jupiter.api.Test
    void addAtZero() {
        list.add("b");
        list.addAt(0, "a");

        assertListContent(new String[] {"a", "b"});
    }

    @org.junit.jupiter.api.Test
    void addAtNonZero() {
        list.add("a");
        list.add("c");
        list.addAt(1, "b");

        assertListContent(new String[] {"a", "b", "c"});
    }

    @org.junit.jupiter.api.Test
    void addAtTheEnd() {
        list.add("a");
        list.add("b");
        list.addAt(2, "c");

        assertListContent(new String[] {"a", "b", "c"});
    }

    @org.junit.jupiter.api.Test
    void addAtNegativeIndex() {
        assertThrows(NoSuchElementException.class, () -> {
            list.addAt(-2, "doesn't matter");
        });
    }

    @org.junit.jupiter.api.Test
    void addAtTooBigIndex() {
        assertThrows(NoSuchElementException.class, () -> {
            list.addAt(5, "doesn't matter");
        });
    }

    @org.junit.jupiter.api.Test
    void clearNonEmptyList() {
        list.add("a");
        list.add("b");
        list.clear();

        assertListContent(new String[]{});
    }

    @org.junit.jupiter.api.Test
    void clearEmptyList() {
        list.clear();
        assertListContent(new String[]{});
    }

    @org.junit.jupiter.api.Test
    void containsNoElementMatching() {
        list.add("a");
        list.add("b");
        list.add("c");

        assertEquals(false, list.contains("d"));
    }

    @org.junit.jupiter.api.Test
    void containsMatchInBeginning() {
        list.add("a");
        list.add("b");
        list.add("c");

        assertEquals(true, list.contains("a"));
    }

    @org.junit.jupiter.api.Test
    void containsMatchInTheMiddle() {
        list.add("a");
        list.add("b");
        list.add("c");

        assertEquals(true, list.contains("b"));
    }

    @org.junit.jupiter.api.Test
    void containsMatchInTheEnd() {
        list.add("a");
        list.add("b");
        list.add("c");

        assertEquals(true, list.contains("c"));
    }

    @org.junit.jupiter.api.Test
    void setInTheBeginning() {
        list.add("a");
        list.add("b");
        list.add("c");
        list.set(0, "d");

        assertListContent(new String[] {"d", "b", "c"});
    }

    @org.junit.jupiter.api.Test
    void setInTheMiddle() {
        list.add("a");
        list.add("b");
        list.add("c");
        list.set(1, "d");

        assertListContent(new String[] {"a", "d", "c"});
    }

    @org.junit.jupiter.api.Test
    void setInTheEnd() {
        list.add("a");
        list.add("b");
        list.add("c");
        list.set(2, "d");

        assertListContent(new String[] {"a", "b", "d"});
    }

    @org.junit.jupiter.api.Test
    void setAtNegativeIndex() {
        assertThrows(NoSuchElementException.class, () -> {
            list.set(-2, "doesn't matter");
        });
    }

    @org.junit.jupiter.api.Test
    void setAtTooBigIndex() {
        assertThrows(NoSuchElementException.class, () -> {
            list.set(5, "doesn't matter");
        });
    }

    @org.junit.jupiter.api.Test
    void indexOfElementInTheBeginning() {
        list.add("a");
        list.add("b");
        list.add("c");

        assertEquals(0, list.indexOf("a"));
    }

    @org.junit.jupiter.api.Test
    void indexOfElementInTheMiddle() {
        list.add("a");
        list.add("b");
        list.add("c");

        assertEquals(1, list.indexOf("b"));
    }

    @org.junit.jupiter.api.Test
    void indexOfElementInTheEnd() {
        list.add("a");
        list.add("b");
        list.add("c");

        assertEquals(2, list.indexOf("c"));
    }

    @org.junit.jupiter.api.Test
    void indexOfNonExistingElement() {
        list.add("a");
        list.add("b");
        list.add("c");

        assertEquals(-1, list.indexOf("d"));
    }

    @org.junit.jupiter.api.Test
    void isEmptyOnEmptyList() {
        assertEquals(true, list.isEmpty());
    }

    @org.junit.jupiter.api.Test
    void isEmptyOnNonEmptyList() {
        list.add("a");
        assertEquals(false, list.isEmpty());
    }

    @org.junit.jupiter.api.Test
    void removeAtTheBeginning() {
        list.add("a");
        list.add("b");
        list.add("c");

        assertEquals("a", list.removeAt(0));
        assertListContent(new String[] {"b", "c"});
    }

    @org.junit.jupiter.api.Test
    void removeAtTheMiddle() {
        list.add("a");
        list.add("b");
        list.add("c");

        assertEquals("b", list.removeAt(1));
        assertListContent(new String[] {"a", "c"});
    }

    @org.junit.jupiter.api.Test
    void removeAtTheEnd() {
        list.add("a");
        list.add("b");
        list.add("c");

        assertEquals("c", list.removeAt(2));
        assertListContent(new String[] {"a", "b"});
    }

    @org.junit.jupiter.api.Test
    void removeAtNegativeIndex() {
        assertThrows(NoSuchElementException.class, () -> {
            list.removeAt(-2);
        });
    }

    @org.junit.jupiter.api.Test
    void removeAtTooBigIndex() {
        assertThrows(NoSuchElementException.class, () -> {
            list.removeAt(5);
        });
    }

    @org.junit.jupiter.api.Test
    void removeInTheBeginning() {
        list.add("a");
        list.add("b");
        list.add("c");

        assertEquals(true, list.remove("a"));
        assertListContent(new String[] {"b", "c"});
    }

    @org.junit.jupiter.api.Test
    void removeInTheMiddle() {
        list.add("a");
        list.add("b");
        list.add("c");

        assertEquals(true, list.remove("b"));
        assertListContent(new String[] {"a", "c"});
    }

    @org.junit.jupiter.api.Test
    void removeInTheEnd() {
        list.add("a");
        list.add("b");
        list.add("c");

        assertEquals(true, list.remove("c"));
        assertListContent(new String[] {"a", "b"});
    }

    @org.junit.jupiter.api.Test
    void removeNonExistingElement() {
        list.add("a");
        list.add("b");
        list.add("c");

        assertEquals(false, list.remove("d"));
        assertListContent(new String[] {"a", "b", "c"});
    }

    @org.junit.jupiter.api.Test
    void sizeEmpty() {
        assertEquals(0, list.size());
    }

    @org.junit.jupiter.api.Test
    void sizeNotEmpty() {
        list.add("a");
        list.add("b");

        assertEquals(2, list.size());
    }

    @org.junit.jupiter.api.Test
    void iterator() {
        list.add("a");
        list.add("b");
        list.add("c");

        var iterator = list.iterator();

        assertEquals(true, iterator.hasNext());
        assertEquals("a", iterator.next());

        assertEquals(true, iterator.hasNext());
        assertEquals("b", iterator.next());

        assertEquals(true, iterator.hasNext());
        assertEquals("c", iterator.next());

        assertEquals(false, iterator.hasNext());
        assertThrows(NoSuchElementException.class, () -> {
            iterator.next();
        });
    }

    @org.junit.jupiter.api.Test
    void insertListBeforeOriginalListByIndex() {
        list.add("d");
        list.add("e");
        list.add("f");

        var anotherList = new TwoWayLinkedList<String>();
        anotherList.add("a");
        anotherList.add("b");
        anotherList.add("c");

        list.insert(anotherList, 0);
        assertListContent(new String[] {"a", "b", "c", "d", "e", "f"});
    }

    @org.junit.jupiter.api.Test
    void insertListBeforeOriginalListByValue() {
        list.add("d");
        list.add("e");
        list.add("f");

        var anotherList = new TwoWayLinkedList<String>();
        anotherList.add("a");
        anotherList.add("b");
        anotherList.add("c");

        list.insert(anotherList, "d");
        assertListContent(new String[] {"a", "b", "c", "d", "e", "f"});
    }

    @org.junit.jupiter.api.Test
    void insertListInMiddleOfOriginalListByIndex() {
        list.add("d");
        list.add("e");
        list.add("f");

        var anotherList = new TwoWayLinkedList<String>();
        anotherList.add("a");
        anotherList.add("b");
        anotherList.add("c");

        list.insert(anotherList, 1);
        assertListContent(new String[] {"d", "a", "b", "c", "e", "f"});
    }

    @org.junit.jupiter.api.Test
    void insertListInMiddleOfOriginalListByValue() {
        list.add("d");
        list.add("e");
        list.add("f");

        var anotherList = new TwoWayLinkedList<String>();
        anotherList.add("a");
        anotherList.add("b");
        anotherList.add("c");

        list.insert(anotherList, "e");
        assertListContent(new String[] {"d", "a", "b", "c", "e", "f"});
    }

    @org.junit.jupiter.api.Test
    void insertBeforeNegativeIndex() {
        list.add("d");
        list.add("e");
        list.add("f");

        var anotherList = new TwoWayLinkedList<String>();
        anotherList.add("a");
        anotherList.add("b");
        anotherList.add("c");

        assertThrows(NoSuchElementException.class, () -> {
            list.insert(anotherList, -2);
        });
    }

    @org.junit.jupiter.api.Test
    void insertBeforeTooBigIndex() {
        list.add("d");
        list.add("e");
        list.add("f");

        var anotherList = new TwoWayLinkedList<String>();
        anotherList.add("a");
        anotherList.add("b");
        anotherList.add("c");

        assertThrows(NoSuchElementException.class, () -> {
            list.insert(anotherList, 5);
        });
    }

    @org.junit.jupiter.api.Test
    void insertBeforeInvalidValue() {
        list.add("d");
        list.add("e");
        list.add("f");

        var anotherList = new TwoWayLinkedList<String>();
        anotherList.add("a");
        anotherList.add("b");
        anotherList.add("c");

        assertThrows(NoSuchElementException.class, () -> {
            list.insert(anotherList, "g");
        });
    }

    private <T> void assertListContent(T[] expectedContent) {
        var iterator = list.iterator();

        for (var i = 0; i < expectedContent.length; i++) {
            if (iterator.hasNext() == false) {
                fail("Didn't expect the list to end.");
            }

            assertEquals(expectedContent[i], iterator.next());
        }

        assertFalse(iterator.hasNext());
    }
}
