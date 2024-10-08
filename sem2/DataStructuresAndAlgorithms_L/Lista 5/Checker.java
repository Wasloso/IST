import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Proszę nie modyfikować tego pliku!
public class Checker implements IChecker {
    private List<int[]> steps;
    private int checksInvoked;


    public Checker(List<int[]> steps) {
        this.steps = steps;
        checksInvoked = 0;
    }

    @Override
    public void check(int[] array) {
        if (checksInvoked < steps.size()) {
            var expectedArray = steps.get(checksInvoked);
            assertArrayEquals(expectedArray, array);
            checksInvoked++;
        }
    }

    public int getChecksInvoked() {
        return checksInvoked;
    }
}
