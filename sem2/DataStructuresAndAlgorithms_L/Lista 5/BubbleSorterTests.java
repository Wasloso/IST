import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

// Proszę nie modyfikować tego pliku!
public class BubbleSorterTests {
    @org.junit.jupiter.api.Test
    void test() {
        var expectedSteps = new ArrayList<int[]>();
        expectedSteps.add(new int[] {4, 8, 6, 5, 7, 3, 2, 9, 1, 10});
        expectedSteps.add(new int[] {4, 6, 5, 7, 3, 2, 8, 1, 9, 10});
        expectedSteps.add(new int[] {4, 5, 6, 3, 2, 7, 1, 8, 9, 10});
        expectedSteps.add(new int[] {4, 5, 3, 2, 6, 1, 7, 8, 9, 10});
        expectedSteps.add(new int[] {4, 3, 2, 5, 1, 6, 7, 8, 9, 10});
        expectedSteps.add(new int[] {3, 2, 4, 1, 5, 6, 7, 8, 9, 10});
        expectedSteps.add(new int[] {2, 3, 1, 4, 5, 6, 7, 8, 9, 10});
        expectedSteps.add(new int[] {2, 1, 3, 4, 5, 6, 7, 8, 9, 10});
        expectedSteps.add(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        var checker = new Checker(expectedSteps);
        var sorter = new BubbleSorter(checker);
        sorter.sort(new int[] {8, 4, 9, 6, 5, 7, 3, 2, 10, 1});
        assertNotEquals(0, checker.getChecksInvoked());
    }
}
