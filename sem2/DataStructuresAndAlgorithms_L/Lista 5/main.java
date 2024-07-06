import java.util.ArrayList;

class main {
    public static void main(String[] args) {
        InvertedSelectionSorter sorter = new InvertedSelectionSorter(new Checker(new ArrayList<>()));
        int[] values = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0,76,12,54,76,31,36,87,54,3,1,12};
        for (int value : values
        ) {
            System.out.print(value + " ");
        }
        sorter.sort(values);
        System.out.println();
        for (int value : values
        ) {
            System.out.print(value + " ");
        }


    }
}