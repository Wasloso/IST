public class BubbleSorter implements ISorter {
    private final IChecker checker;

    public BubbleSorter(IChecker checker) {
        this.checker = checker;
    }

    @Override
    public void sort(int[] values) {

        for (int i = 0; i < values.length; i++) {
            for (int j = 1; j < values.length - i; j++) {
                //Zamiana elementow obok siebie miejscami, aby byly w kolejnosci mniejszy -> wiekszy
                //Największy element zostanie przesunięty na sam koniec listy i już tam zostanie (nie będzie dostęny w następnej iteracji)
                if (values[j - 1] > values[j]) {
                    //customowa funkcja dostepna w interfejsie ISorter
                    swap(values, j - 1, j);
                }
            }
            checker.check(values);
        }
    }
}
