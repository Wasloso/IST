// Proszę nie modyfikować tego pliku!
public interface ISorter {
    void sort(int[] values);

    default void swap(int[] values, int a, int b) {
        int tmpA = values[a];
        int tmpB = values[b];
        values[a] = tmpB;
        values[b] = tmpA;
    }
}
