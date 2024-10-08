public class DisjointSetForest implements IDisjointSetStructure {

    private int[] representatives;

    public DisjointSetForest(int size) {
        representatives = new int[size];
        //na poczatku wszystkie przedzialy sa jednoelementowe
        for (int i = 0; i < size; i++) {
            representatives[i] = i;
        }
    }

    @Override
    public int findSet(int item) throws ItemOutOfRangeException {
        if (item < 0 || item >= representatives.length) {
            throw new ItemOutOfRangeException();
        }
        //szukanie najwyzszego reprezentanta
        while (item != representatives[item]) {
            item = representatives[item];
        }
        return item;
    }

    @Override
    public void union(int item1, int item2) throws ItemOutOfRangeException {
        if (item1 < 0 || item1 >= representatives.length || item2 < 0 || item2 >= representatives.length) {
            throw new ItemOutOfRangeException();
        }
        int rep1 = findSet(item1);
        int rep2 = findSet(item2);
        representatives[rep2] = rep1;
    }
}
