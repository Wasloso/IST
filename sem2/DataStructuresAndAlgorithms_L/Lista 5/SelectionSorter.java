public class SelectionSorter implements ISorter {
    private final IChecker checker;

    public SelectionSorter(IChecker checker) {
        this.checker = checker;
    }

    @Override
    public void sort(int[] values) {
        for (int i = 0; i < values.length; i++) {
            int min = i; //min - indeks najmniejszej wartosci w liscie zaczynajac od i
            for (int j = i + 1; j < values.length; j++) {
                //Szukanie indeksu najmniejszej wartosci
                if (values[j] < values[min]) {
                    min = j;
                }
            }
            //Wstawianie najmniejszej wartości na początek listy (potem na miejsce od ktorego zaczeto poszukiwania - i)
            swap(values, min, i);
            checker.check(values);
        }
    }
}
