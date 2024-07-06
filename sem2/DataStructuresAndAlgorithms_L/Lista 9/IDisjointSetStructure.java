public interface IDisjointSetStructure {
    int findSet(int item) throws ItemOutOfRangeException;
    void union(int item1, int item2) throws ItemOutOfRangeException;
}
