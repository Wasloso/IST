public class DisjointSetLinkedList implements IDisjointSetStructure {

    private Node[] nodes;

    public DisjointSetLinkedList(int size) {
        nodes = new Node[size];
        for (int i = 0; i < size; i++) {
            nodes[i] = new Node(i, i);
        }
    }

    @Override
    public int findSet(int item) throws ItemOutOfRangeException {
        if (item < 0 || item >= nodes.length) {
            throw new ItemOutOfRangeException();
        }
        return nodes[item].rep;
    }

    @Override
    public void union(int item1, int item2) throws ItemOutOfRangeException {
        if (item1 < 0 || item1 >= nodes.length || item2 < 0 || item2 >= nodes.length) {
            throw new ItemOutOfRangeException();
        }

        Node node1 = nodes[findSet(item1)];
        Node node2 = nodes[findSet(item2)];

        nodes[node1.last].next=node2.rep;
        node1.last = node2.last;
        node2.rep = node1.rep;

        int current = node2.next;
        while (current!=-1){
            nodes[current].rep=node1.rep;
            current=nodes[current].next;
        }
    }


    private static class Node {
        int value;
        int rep;
        int next;
        int last;

        public Node(int value, int position) {
            this.value = value;
            this.rep = position;
            this.next = -1;
            this.last = position;
        }
    }
}
