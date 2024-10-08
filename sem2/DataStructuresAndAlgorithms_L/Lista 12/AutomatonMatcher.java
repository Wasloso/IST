import java.util.List;
import java.util.ArrayList;

public class AutomatonMatcher implements IStringMatcher {
    int ASCII_length = 128;

    @Override
    public List<Integer> validShifts(String textToSearch, String patternToFind) {
        List<Integer> validShifts = new ArrayList<>();
        int patternLength = patternToFind.length();
        int textLength = textToSearch.length();
        int[][] stateTable = createStateTable(patternToFind,patternLength); // tablica stanow

        int currentState = 0;
        for (int i = 0; i < textLength; i++) {
            currentState = stateTable[currentState][textToSearch.charAt(i)];
            if (currentState == patternLength) {
                validShifts.add(i - patternLength + 1);
            }
        }
        return validShifts;
    }

    private int[][] createStateTable(String pattern, int m) {
        int[][] T = new int[m + 1][ASCII_length];
        for (int state = 0; state <= m; ++state) {
            for (int ascii = 0; ascii < ASCII_length; ++ascii) {
                T[state][ascii] = getNextState(pattern, m, state, ascii);
            }
        }
        return T;
    }

    private int getNextState(String pattern, int m, int currentState, int ascii) {
        // Jesli nastepny znak odpowiada stanowi -> zwiekszenie stanu o 1
        if (currentState < m && ascii == pattern.charAt(currentState)) {
            return currentState + 1;
        }

        // W przeciwnym wypadku szukanie najdluzszego sufiksa ktory jest prefiksem patternu
        int nextState;
        for (nextState = currentState; nextState > 0; nextState--) {
            if (pattern.charAt(nextState - 1) == ascii) {
                int i;
                for (i = 0; i < nextState - 1; i++) {
                    if (pattern.charAt(i) != pattern.charAt(currentState - nextState + 1 + i)) {
                        break;
                    }
                }
                if (i == nextState - 1) {
                    return nextState;
                }
            }
        }
        // Jesli nie zostanie znaleziony zaden prefiks to wracamy do stanu 0
        return 0;
    }
}

