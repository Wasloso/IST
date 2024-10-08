import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public abstract class StringMatcherTestBase {
    private final IStringMatcher stringMatcher;

    protected StringMatcherTestBase(IStringMatcher stringMatcher) {
        this.stringMatcher = stringMatcher;
    }

    @Test
    public void noMatches() {
        var validShifts = stringMatcher.validShifts("ABCDEFGHIJKLMNOPQRSTUVWXYZ", "abcd");
        assertTrue(validShifts.isEmpty());
    }

    @Test
    public void oneMatchInTheBeginning() {
        var validShifts = stringMatcher.validShifts("ABCDEFGHIJ", "ABC");
        assertEquals(Arrays.asList(0), validShifts);
    }

    @Test
    public void oneMatchInTheMiddle() {
        var validShifts = stringMatcher.validShifts("ABCDEFGHIJ", "DEFG");
        assertEquals(Arrays.asList(3), validShifts);
    }

    @Test
    public void oneMatchInTheEnd() {
        var validShifts = stringMatcher.validShifts("ABCDEFGHIJ", "HIJ");
        assertEquals(Arrays.asList(7), validShifts);
    }

    @Test
    public void almostTwoMatches_missingCharInTheBeginning() {
        var validShifts = stringMatcher.validShifts("ABCDEFABCDEFG", "ABCDEFG");
        assertEquals(Arrays.asList(6), validShifts);
    }

    @Test
    public void almostTwoMatches_missingCharInTheEnd() {
        var validShifts = stringMatcher.validShifts("ABCDEFGABCDEF", "ABCDEFG");
        assertEquals(Arrays.asList(0), validShifts);
    }

    @Test
    public void threeConsecutiveMatches() {
        var validShifts = stringMatcher.validShifts("ABCABCABC", "ABC");
        assertEquals(Arrays.asList(0, 3, 6), validShifts);
    }

    @Test
    public void threeMatchesWithSpacesBetween() {
        var validShifts = stringMatcher.validShifts("ABCDABCEFABC", "ABC");
        assertEquals(Arrays.asList(0, 4, 9), validShifts);
    }

    @Test
    public void veryLongString() {
        var testData = VeryLongTextGenerator.generate(100000, 75, 10);

        var validShifts = stringMatcher.validShifts(testData.getText(), testData.getPattern());
        assertEquals(testData.getValidShifts(), validShifts);
    }
}
