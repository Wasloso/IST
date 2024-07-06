import java.util.Arrays;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

public class BinarySearchTreeSorterTests {
    @org.junit.jupiter.api.Test
    public void sort_emptyList() throws DuplicateElementException {
        var originalList = new LinkedList<Integer>();
        var expectedList = new LinkedList<Integer>();

        BinarySearchTreeSorter.sort(originalList);
        assertEquals(expectedList, originalList);
    }

    @org.junit.jupiter.api.Test
    public void sort_fullList() throws DuplicateElementException {
        var originalList = new LinkedList<Integer>();
        originalList.addAll(Arrays.asList(4, 3, 9, 6, 10, 1, 8, 2, 7, 5));

        var expectedList = new LinkedList<Integer>();
        expectedList.addAll(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

        BinarySearchTreeSorter.sort(originalList);
        assertEquals(expectedList, originalList);
    }
}
