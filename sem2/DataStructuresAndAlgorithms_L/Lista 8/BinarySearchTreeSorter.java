import java.util.List;
import java.util.Stack;

public class BinarySearchTreeSorter {
    public static <T extends Comparable<T>> void sort(List<T> list) throws DuplicateElementException {
        BinarySearchTree<T> bst = new BinarySearchTree<>();
        for(T element : list){
            bst.add(element);
        }
        list.clear();
        list.addAll(bst.toListInOrder());
    }
}
