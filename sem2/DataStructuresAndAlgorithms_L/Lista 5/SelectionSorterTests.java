import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SelectionSorterTests {
    @org.junit.jupiter.api.Test
    void test() {
        var expectedSteps = new ArrayList<int[]>();
        expectedSteps.add(new int[] {1, 4, 9, 6, 5, 7, 3, 2, 10, 8});
        expectedSteps.add(new int[] {1, 2, 9, 6, 5, 7, 3, 4, 10, 8});
        expectedSteps.add(new int[] {1, 2, 3, 6, 5, 7, 9, 4, 10, 8});
        expectedSteps.add(new int[] {1, 2, 3, 4, 5, 7, 9, 6, 10, 8});
        expectedSteps.add(new int[] {1, 2, 3, 4, 5, 7, 9, 6, 10, 8});
        expectedSteps.add(new int[] {1, 2, 3, 4, 5, 6, 9, 7, 10, 8});
        expectedSteps.add(new int[] {1, 2, 3, 4, 5, 6, 7, 9, 10, 8});
        expectedSteps.add(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 10, 9});
        expectedSteps.add(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        var checker = new Checker(expectedSteps);
        var sorter = new SelectionSorter(checker);
        sorter.sort(new int[] {8, 4, 9, 6, 5, 7, 3, 2, 10, 1});
        assertNotEquals(0, checker.getChecksInvoked());
    }
}
