import java.util.*;
import java.util.stream.Collectors;

public class VeryLongTextGenerator {
    private static final char[] alphabet = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final Random random = new Random();

    public static VeryLongText generate(int textLength, int patternLength, int numberOfValidShifts) {
        var shifts = calculateShifts(textLength, patternLength, numberOfValidShifts);
        var pattern = generatePattern(patternLength);
        var text = generateText(textLength, pattern, shifts);

        return new VeryLongText(text, pattern, shifts);
    }

    private static Set<Integer> calculateShifts(int textLength, int patternLength, int numberOfValidShifts) {
        var shifts = new HashSet<Integer>();
        int index;

        while (shifts.size() != numberOfValidShifts) {
            do {
                index = random.nextInt(0, textLength - patternLength);
            } while (isIndexInvalid(index, shifts, patternLength));

            shifts.add(index);
        }

        return shifts;
    }

    private static boolean isIndexInvalid(int indexToTest, Set<Integer> existingIndexes, int patternLength) {
        return existingIndexes.stream().anyMatch(existingIndex -> indexToTest >= existingIndex && indexToTest < existingIndex + patternLength);
    }

    private static String generatePattern(int length) {
        var pattern = new StringBuffer(length);

        for (var i = 0; i < length; i++) {
            pattern.append(randomLetter());
        }

        return pattern.toString();
    }

    private static String generateText(int desiredLength, String pattern, Set<Integer> indexesOfOccurrences) {
        var text = new StringBuffer(desiredLength);
        var currentIndex = 0;

        while (currentIndex < desiredLength) {
            if (indexesOfOccurrences.contains(currentIndex)) {
                text.append(pattern);
                currentIndex += pattern.length();
            } else {
                text.append(randomLetter());
                currentIndex++;
            }
        }

        return text.toString();
    }

    private static char randomLetter() {
        return alphabet[random.nextInt(alphabet.length)];
    }

    public static class VeryLongText {
        private String text;
        private String pattern;
        private List<Integer> validShifts;

        public VeryLongText(String text, String pattern, Set<Integer> validShifts) {
            this.text = text;
            this.pattern = pattern;
            this.validShifts = validShifts.stream().sorted().collect(Collectors.toList());
        }

        public String getText() {
            return text;
        }

        public String getPattern() {
            return pattern;
        }

        public List<Integer> getValidShifts() {
            return validShifts;
        }
    }
}
