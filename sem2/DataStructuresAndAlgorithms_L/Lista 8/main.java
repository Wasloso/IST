import static org.junit.jupiter.api.Assertions.fail;

public class main {
    public static void main(String[] args) {
        var tree = new BinarySearchTree<Integer>();

        try {
            tree.add(5);
            tree.add(8);
            tree.add(2);
            tree.add(1);
            tree.add(3);
            tree.add(10);
            tree.add(7);
        } catch (Exception exception) {
            fail(exception);
        }
        System.out.println(tree.toStringPreOrder());
        System.out.println(tree.toStringInOrder());
        System.out.println(tree.toStringPostOrder());

    }
}
