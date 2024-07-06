package com.company;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OneWayLinkedListTests {
    private IList<String> list;

    @org.junit.jupiter.api.BeforeEach
    public void beforeEach() {
        list = new OneWayLinkedList<String>();
    }

    @org.junit.jupiter.api.Test
    void addToEmptyList() {
        list.add("a");
        assertEquals("a", list.get(0));
    }

    @org.junit.jupiter.api.Test
    void addToNotEmptyList() {
        list.add("a");
        list.add("b");
        list.add("c");

        assertEquals("c", list.get(2));
    }

    @org.junit.jupiter.api.Test
    void addAtZero() {
        list.add("b");
        list.addAt(0, "a");

        assertEquals("a", list.get(0));
        assertEquals("b", list.get(1));
    }

    @org.junit.jupiter.api.Test
    void addAtNonZero() {
        list.add("a");
        list.add("c");
        list.addAt(1, "b");

        assertEquals("a", list.get(0));
        assertEquals("b", list.get(1));
        assertEquals("c", list.get(2));
    }

    @org.junit.jupiter.api.Test
    void addAtInvalidIndex() {
        assertThrows(NoSuchElementException.class, () -> {
            list.addAt(-2, "doesn't matter");
        });
    }

    @org.junit.jupiter.api.Test
    void clearNonEmptyList() {
        list.add("a");
        list.add("b");
        list.clear();

        assertEquals(0, list.size());
    }

    @org.junit.jupiter.api.Test
    void clearEmptyList() {
        list.clear();
        assertEquals(0, list.size());
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
    void setAtZero() {
        list.add("a");
        list.add("b");

        list.set(0, "c");
        assertEquals("c", list.get(0));
    }

    @org.junit.jupiter.api.Test
    void setAtNonZero() {
        list.add("a");
        list.add("b");
        list.add("c");

        list.set(1, "d");
        assertEquals("d", list.get(1));
    }

    @org.junit.jupiter.api.Test
    void setAtInvalidIndex() {
        assertThrows(NoSuchElementException.class, () -> {
            list.set(-2, "doesn't matter");
        });
    }

    @org.junit.jupiter.api.Test
    void indexOfElementAtZero() {
        list.add("a");
        list.add("b");
        list.add("c");

        assertEquals(0, list.indexOf("a"));
    }

    @org.junit.jupiter.api.Test
    void indexOfElementAtNonZero() {
        list.add("a");
        list.add("b");
        list.add("c");

        assertEquals(1, list.indexOf("b"));
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
    void removeAtZero() {
        list.add("a");
        list.add("b");
        list.add("c");

        assertEquals("a", list.removeAt(0));
        assertEquals("b", list.get(0));
    }

    @org.junit.jupiter.api.Test
    void removeAtNonZero() {
        list.add("a");
        list.add("b");
        list.add("c");

        assertEquals("b", list.removeAt(1));
        assertEquals("c", list.get(1));
    }

    @org.junit.jupiter.api.Test
    void removeAtInvalidIndex() {
        assertThrows(NoSuchElementException.class, () -> {
            list.removeAt(-2);
        });
    }

    @org.junit.jupiter.api.Test
    void removeElementOnZero() {
        list.add("a");
        list.add("b");
        list.add("c");

        assertEquals(true, list.remove("a"));
        assertEquals("b", list.get(0));
    }

    @org.junit.jupiter.api.Test
    void removeElementOnNonZero() {
        list.add("a");
        list.add("b");
        list.add("c");

        assertEquals(true, list.remove("b"));
        assertEquals("c", list.get(1));
    }

    @org.junit.jupiter.api.Test
    void removeNonExistingElement() {
        list.add("a");
        list.add("b");
        list.add("c");

        assertEquals(false, list.remove("d"));
        assertEquals("b", list.get(1));
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
}
