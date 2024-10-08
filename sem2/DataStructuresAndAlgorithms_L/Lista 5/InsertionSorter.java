public class InsertionSorter implements ISorter {
    private final IChecker checker;

    public InsertionSorter(IChecker checker) {
        this.checker = checker;
    }

    @Override
    public void sort(int[] values) {

        for (int i = 1; i < values.length; i++) {
            for (int j = i; j > 0; j--) {
                // Przesuwanie elementów w lewo póki są mniejsze (tzn poki nie znajda swojego "miejsca" pomiedzy dwoma wartosciami)
                if (values[j] <= values[j - 1]) {
                    swap(values, j, j - 1);
                } else break;
            }
            checker.check(values);
        }
    }
}
