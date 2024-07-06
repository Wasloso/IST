import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

// Proszę nie modyfikować tego pliku!
public class InvertedSelectionSorterTests {
    @org.junit.jupiter.api.Test
    void test() {
        var expectedSteps = new ArrayList<int[]>();
        expectedSteps.add(new int[] {8, 4, 9, 6, 5, 7, 3, 2, 1, 10});
        expectedSteps.add(new int[] {8, 4, 1, 6, 5, 7, 3, 2, 9, 10});
        expectedSteps.add(new int[] {2, 4, 1, 6, 5, 7, 3, 8, 9, 10});
        expectedSteps.add(new int[] {2, 4, 1, 6, 5, 3, 7, 8, 9, 10});
        expectedSteps.add(new int[] {2, 4, 1, 3, 5, 6, 7, 8, 9, 10});
        expectedSteps.add(new int[] {2, 4, 1, 3, 5, 6, 7, 8, 9, 10});
        expectedSteps.add(new int[] {2, 3, 1, 4, 5, 6, 7, 8, 9, 10});
        expectedSteps.add(new int[] {2, 1, 3, 4, 5, 6, 7, 8, 9, 10});
        expectedSteps.add(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

        var checker = new Checker(expectedSteps);
        var sorter = new InvertedSelectionSorter(checker);
        sorter.sort(new int[] {8, 4, 9, 6, 5, 7, 3, 2, 10, 1});
        assertNotEquals(0, checker.getChecksInvoked());
    }
}
