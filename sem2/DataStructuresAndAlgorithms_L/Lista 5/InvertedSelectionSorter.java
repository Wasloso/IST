public class InvertedSelectionSorter implements ISorter {
    private final IChecker checker;

    public InvertedSelectionSorter(IChecker checker) {
        this.checker = checker;
    }

    @Override
    public void sort(int[] values) {


        for (int i = values.length-1; i >= 0; i--) {
            int max = i; //max - indeks najwiekszej wartosci w liscie zaczynajac od jej końca
            for (int j = i ; j >= 0; j--) {
                //Szukanie indeksu najwiekszej wartosci
                if (values[j] > values[max]) {
                    max = j;
                }
            }
            //Wstawianie najwiekszej wartości na koniec listy (potem na miejsce od ktorego zaczeto poszukiwania - i)
            swap(values, max, i);
            checker.check(values);
        }
    }
}
