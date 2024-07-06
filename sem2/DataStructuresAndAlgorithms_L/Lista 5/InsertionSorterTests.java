import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

// Proszę nie modyfikować tego pliku!
public class InsertionSorterTests {
    @org.junit.jupiter.api.Test
    void test() {
        var expectedSteps = new ArrayList<int[]>();
        expectedSteps.add(new int[] {4, 8, 9, 6, 5, 7, 3, 2, 10, 1});
        expectedSteps.add(new int[] {4, 8, 9, 6, 5, 7, 3, 2, 10, 1});
        expectedSteps.add(new int[] {4, 6, 8, 9, 5, 7, 3, 2, 10, 1});
        expectedSteps.add(new int[] {4, 5, 6, 8, 9, 7, 3, 2, 10, 1});
        expectedSteps.add(new int[] {4, 5, 6, 7, 8, 9, 3, 2, 10, 1});
        expectedSteps.add(new int[] {3, 4, 5, 6, 7, 8, 9, 2, 10, 1});
        expectedSteps.add(new int[] {2, 3, 4, 5, 6, 7, 8, 9, 10, 1});
        expectedSteps.add(new int[] {2, 3, 4, 5, 6, 7, 8, 9, 10, 1});
        expectedSteps.add(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        var checker = new Checker(expectedSteps);
        var sorter = new InsertionSorter(checker);
        sorter.sort(new int[] {8, 4, 9, 6, 5, 7, 3, 2, 10, 1});
        assertNotEquals(0, checker.getChecksInvoked());
    }
}
