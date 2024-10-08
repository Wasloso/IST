import java.util.Arrays;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class HashMap<TKey, TValue> {
    int intiialSize;
    int capacity;
    int elements = 0;
    double loadFactor;
    Function<TKey, Integer> hashFunction;
    LinkedList<Node<TKey, TValue>>[] table;


    private static class Node<TKey, TValue> {
        TKey key;
        TValue value;

        Node(TKey key, TValue value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj.getClass() != Node.class) return false;
            else {
                Node<TKey, TValue> node = (Node<TKey, TValue>) obj;
                return this.key == node.key;
            }
        }

        @Override
        public String toString() {
            return "Node{" +
                    "key=" + key +
                    ", value=" + value +
                    '}';
        }
    }

    public HashMap(int initialSize, double loadFactor, Function<TKey, Integer> hashFunction) {
        this.intiialSize = initialSize;
        this.loadFactor = loadFactor;
        this.hashFunction = hashFunction;
        this.capacity = initialSize;
        this.table = (LinkedList<Node<TKey, TValue>>[]) new LinkedList[capacity];
    }

    public void add(TKey key, TValue value) throws DuplicateKeyException {
        int index = hashFunction.apply(key);
        Node<TKey, TValue> newNode = new Node<>(key, value);
        if (table[index] == null) {
            table[index] = new LinkedList<>();
            table[index].add(newNode);
            elements++;
            checkForOverload();
        } else {
            LinkedList<Node<TKey, TValue>> list = table[index];
            if (list.contains(newNode)) throw new DuplicateKeyException();
            list.add(newNode);
            elements++;
        }
    }

    public void clear() {
        capacity = intiialSize;
        elements = 0;
        table = (LinkedList<Node<TKey, TValue>>[]) new LinkedList[capacity];
    }

    public boolean containsKey(TKey key) {
        int index = hashFunction.apply(key);
        if (table[index] == null) return false;
        LinkedList<Node<TKey, TValue>> list = table[index];
        for (Node<TKey, TValue> node : list) {
            if (node.key.equals(key)) return true;
        }
        return false;
    }

    public boolean containsValue(TValue value) {
        System.out.println(toString());
        for (LinkedList<Node<TKey, TValue>> list : table) {
            if (list != null) {
                for (Node<TKey, TValue> node : list) {
                    if (node.value.equals(value)) return true;
                }
            }
        }
        return false;
    }

    public int elements() {
        return elements;
    }

    public TValue get(TKey key) throws NoSuchElementException {
        int index = hashFunction.apply(key);
        if (index >= capacity || table[index] == null) throw new NoSuchElementException();
        else {
            LinkedList<Node<TKey, TValue>> list = table[index];
            for (Node<TKey, TValue> node : list) {
                if (node.key == key) return node.value;
            }
        }
        return null;
    }

    public void put(TKey key, TValue value) {
        int index = hashFunction.apply(key);
        if (table[index] == null) {
            table[index] = new LinkedList<>();
            Node<TKey, TValue> newNode = new Node<>(key, value);
            table[index].add(newNode);
            elements++;
            checkForOverload();
        } else {
            for (Node<TKey, TValue> actNode : table[index]) {
                if (actNode.key == key) {
                    actNode.value = value;
                    return;
                }
            }
        }
    }

    public TValue remove(TKey key) {
        if (!containsKey(key)) return null;
        TValue retValue = null;
        Node<TKey, TValue> removedNode = null;
        int index = hashFunction.apply(key);
        for (Node<TKey, TValue> actNode : table[index]) {
            if (actNode.key == key) {
                removedNode = actNode;
                retValue = actNode.value;
            }
        }
        table[index].remove(removedNode);
        elements--;
        return retValue;
    }

    public int size() {
        return capacity;
    }

    private void resize() {
        capacity *= 2;
        LinkedList<Node<TKey, TValue>>[] newTable = (LinkedList<Node<TKey, TValue>>[]) new LinkedList[capacity];
        for (LinkedList<Node<TKey, TValue>> list : table) {
            if (list != null) {
                for (Node<TKey, TValue> node : list) {
                    int index = hashFunction.apply(node.key);
                    if(newTable[index]==null){
                        newTable[index]= new LinkedList<>();
                    }
                    newTable[index].add(node);
                }
            }
        }
        table = newTable;
    }

    boolean checkForOverload() {
        if ((float) elements / capacity > loadFactor) {
            resize();
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "HashMap{" +
                "table=" + Arrays.toString(table) +
                '}';
    }
}
