import java.util.ArrayList;
import java.util.List;

public class KMPMatcher implements IStringMatcher {
    @Override
    public List<Integer> validShifts(String textToSearch, String patternToFind) {
        List<Integer> validShifts = new ArrayList<>();
        int textLength = textToSearch.length();
        int patternLength = patternToFind.length();
        int[] prefixTable = getPrefixTable(patternToFind, patternLength);

        int patternIndex = 0;
        for (int textIndex = 0; textIndex < textLength; textIndex++) {
            if (textToSearch.charAt(textIndex) == patternToFind.charAt(patternIndex)) {
                patternIndex++;
                if (patternIndex == patternLength) {
                    validShifts.add(textIndex - patternIndex + 1);
                    patternIndex = prefixTable[patternIndex - 1];
                }
            } else if (patternIndex != 0) {
                patternIndex = prefixTable[patternIndex - 1];
                textIndex--;
            }
        }
        return validShifts;
    }


    private int[] getPrefixTable(String pattern, int patternLength) {
        int[] T = new int[patternLength];
        int length = 0;
        T[0] = 0;
        for (int i = 1; i < patternLength; i++) {
            if (pattern.charAt(i) == pattern.charAt(length)) {
                length++;
                T[i] = length;
            } else {
                if (length != 0) {
                    length = T[length - 1];
                    i--;
                } else {
                    T[i] = 0;
                }
            }
        }
        return T;
    }

}
