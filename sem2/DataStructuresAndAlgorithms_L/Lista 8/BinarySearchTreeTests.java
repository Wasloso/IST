import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

public class BinarySearchTreeTests {
    @org.junit.jupiter.api.Test
    public void add_emptyTree() throws DuplicateElementException {
        var tree = new BinarySearchTree<Integer>();
        tree.add(1);
        assertEquals("1", tree.toStringPreOrder());
    }

    @org.junit.jupiter.api.Test
    public void add_valueAlreadyExists() throws DuplicateElementException {
        var tree = new BinarySearchTree<Integer>();
        tree.add(1);

        assertThrows(DuplicateElementException.class, () -> {
            tree.add(1);
        });
    }

    @org.junit.jupiter.api.Test
    public void add_firstLevelChildren() throws DuplicateElementException {
        var tree = new BinarySearchTree<Integer>();
        tree.add(2);
        tree.add(3);
        tree.add(1);
        assertEquals("2, 1, 3", tree.toStringPreOrder());
    }

    @org.junit.jupiter.api.Test
    public void add_secondLevelChildren() throws DuplicateElementException {
        var tree = new BinarySearchTree<Integer>();
        tree.add(5);
        tree.add(8);
        tree.add(2);
        tree.add(1);
        tree.add(3);
        tree.add(10);
        tree.add(7);
        assertEquals("5, 2, 1, 3, 8, 7, 10", tree.toStringPreOrder());
    }

    @org.junit.jupiter.api.Test
    public void contains_emptyTree() {
        var tree = new BinarySearchTree<Integer>();
        assertFalse(tree.contains(1));
    }

    @org.junit.jupiter.api.Test
    public void contains_leftSubtreeLevel1() {
        var tree = createTestTree();
        assertTrue(tree.contains(2));
    }

    @org.junit.jupiter.api.Test
    public void contains_rightSubtreeLevel1() {
        var tree = createTestTree();
        assertTrue(tree.contains(8));
    }

    @org.junit.jupiter.api.Test
    public void contains_leftRightSubtreeLevel2() {
        var tree = createTestTree();
        assertTrue(tree.contains(3));
    }

    @org.junit.jupiter.api.Test
    public void contains_rightLeftSubtreeLevel2() {
        var tree = createTestTree();
        assertTrue(tree.contains(7));
    }

    @org.junit.jupiter.api.Test
    public void delete_emptyTree() {
        var tree = new BinarySearchTree<Integer>();
        assertDoesNotThrow(() -> {
            tree.delete(6);
        });
    }

    @org.junit.jupiter.api.Test
    public void delete_valueDoesntExist() {
        var tree = createTestTree();
        assertEquals("5, 2, 1, 3, 8, 7, 10", tree.toStringPreOrder());
    }

    @org.junit.jupiter.api.Test
    public void delete_noChildren() {
        var tree = createTestTree();
        tree.delete(3);
        assertEquals("5, 2, 1, 8, 7, 10", tree.toStringPreOrder());
    }

    @org.junit.jupiter.api.Test
    public void delete_oneChildOnLeft() {
        var tree = createTestTree();
        tree.delete(3);
        tree.delete(2);
        assertEquals("5, 1, 8, 7, 10", tree.toStringPreOrder());
    }

    @org.junit.jupiter.api.Test
    public void delete_oneChildOnRight() {
        var tree = createTestTree();
        tree.delete(1);
        tree.delete(2);
        assertEquals("5, 3, 8, 7, 10", tree.toStringPreOrder());
    }

    @org.junit.jupiter.api.Test
    public void delete_twoChildren() {
        var tree = createTestTree();
        tree.delete(2);
        assertEquals("5, 3, 1, 8, 7, 10", tree.toStringPreOrder());
    }

    @org.junit.jupiter.api.Test
    public void delete_twoChildrenRoot() {
        var tree = createTestTree();
        tree.delete(5);
        assertEquals("7, 2, 1, 3, 8, 10", tree.toStringPreOrder());
    }

    @org.junit.jupiter.api.Test
    public void toStringPreOrder_emptyTree() {
        toString_emptyTree(tree -> tree.toStringPreOrder());
    }

    @org.junit.jupiter.api.Test
    public void toStringPreOrder_onlyRoot() {
        toString_onlyRoot(tree -> tree.toStringPreOrder());
    }

    @org.junit.jupiter.api.Test
    public void toStringPreOrder_rootAndLeft() {
        toString_rootAndLeft(tree -> tree.toStringPreOrder(), "2, 1");
    }

    @org.junit.jupiter.api.Test
    public void toStringPreOrder_rootAndRight() {
        toString_rootAndRight(tree -> tree.toStringPreOrder(), "2, 3");
    }

    @org.junit.jupiter.api.Test
    public void toStringPreOrder_fullTree() {
        toString_fullTree(tree -> tree.toStringPreOrder(), "5, 2, 1, 3, 8, 7, 10");
    }

    @org.junit.jupiter.api.Test
    public void toStringInOrder_emptyTree() {
        toString_emptyTree(tree -> tree.toStringInOrder());
    }

    @org.junit.jupiter.api.Test
    public void toStringInOrder_onlyRoot() {
        toString_onlyRoot(tree -> tree.toStringInOrder());
    }

    @org.junit.jupiter.api.Test
    public void toStringInOrder_rootAndLeft() {
        toString_rootAndLeft(tree -> tree.toStringInOrder(), "1, 2");
    }

    @org.junit.jupiter.api.Test
    public void toStringInOrder_rootAndRight() {
        toString_rootAndRight(tree -> tree.toStringInOrder(), "2, 3");
    }

    @org.junit.jupiter.api.Test
    public void toStringInOrder_fullTree() {
        toString_fullTree(tree -> tree.toStringInOrder(), "1, 2, 3, 5, 7, 8, 10");
    }

    @org.junit.jupiter.api.Test
    public void toStringPostOrder_emptyTree() {
        toString_emptyTree(tree -> tree.toStringPostOrder());
    }

    @org.junit.jupiter.api.Test
    public void toStringPostOrder_onlyRoot() {
        toString_onlyRoot(tree -> tree.toStringPostOrder());
    }

    @org.junit.jupiter.api.Test
    public void toStringPostOrder_rootAndLeft() {
        toString_rootAndLeft(tree -> tree.toStringPostOrder(), "1, 2");
    }

    @org.junit.jupiter.api.Test
    public void toStringPostOrder_rootAndRight() {
        toString_rootAndRight(tree -> tree.toStringPostOrder(), "3, 2");
    }

    @org.junit.jupiter.api.Test
    public void toStringPostOrder_fullTree() {
        toString_fullTree(tree -> tree.toStringPostOrder(), "1, 3, 2, 7, 10, 8, 5");
    }

    private void toString_emptyTree(Function<BinarySearchTree<Integer>, String> function) {
        var tree = new BinarySearchTree<Integer>();
        assertEquals("", function.apply(tree));
    }

    private void toString_onlyRoot(Function<BinarySearchTree<Integer>, String> function) {
        try {
            var tree = new BinarySearchTree<Integer>();
            tree.add(1);
            assertEquals("1", function.apply(tree));
        } catch (Exception exception) {
            fail(exception);
        }
    }

    private void toString_rootAndLeft(
            Function<BinarySearchTree<Integer>, String> function,
            String expectedResult) {
        try {
            var tree = new BinarySearchTree<Integer>();
            tree.add(2);
            tree.add(1);
            assertEquals(expectedResult, function.apply(tree));
        } catch (Exception exception) {
            fail(exception);
        }
    }

    private void toString_rootAndRight(
            Function<BinarySearchTree<Integer>, String> function,
            String expectedResult) {
        try {
            var tree = new BinarySearchTree<Integer>();
            tree.add(2);
            tree.add(3);
            assertEquals(expectedResult, function.apply(tree));
        } catch (Exception exception) {
            fail(exception);
        }
    }

    private void toString_fullTree(
            Function<BinarySearchTree<Integer>, String> function,
            String expectedResult) {
        try {
            var tree = createTestTree();
            assertEquals(expectedResult, function.apply(tree));
        } catch (Exception exception) {
            fail(exception);
        }
    }

    private static BinarySearchTree<Integer> createTestTree() {
        //       5
        //   2       8
        // 1   3   7   10

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

        return tree;
    }
}
