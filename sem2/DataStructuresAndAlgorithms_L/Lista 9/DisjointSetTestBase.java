import org.junit.jupiter.api.Test;

import java.util.function.Function;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public abstract class DisjointSetTestBase {
    private static int size = 16;

    private final Function<Integer, IDisjointSetStructure> factory;

    protected DisjointSetTestBase(Function<Integer, IDisjointSetStructure> factory) {
        this.factory = factory;
    }

    @Test
    public void oneElementSets() throws ItemOutOfRangeException {
        var structure = factory.apply(size);

        for (var i = 0; i < size; i++) {
            assertEquals(i, structure.findSet(i));
        }
    }

    @Test
    public void twoElementSets() throws ItemOutOfRangeException {
        var structure = factory.apply(size);

        for (var i = 0; i < size; i += 2) {
            structure.union(i, i + 1);
        }

        for (var i = 0; i < size; i += 2) {
            assertEquals(structure.findSet(i), structure.findSet(i + 1));
        }

        assertEquals(size / 2, countDistinctSets(structure));
    }

    @Test
    public void fourElementSets() throws ItemOutOfRangeException {
        var structure = factory.apply(size);

        for (var i = 0; i < size; i += 2) {
            structure.union(i, i + 1);
        }

        for (var i = 0; i < size; i += 4) {
            structure.union(i, i + 2);
        }

        for (var i = 0; i < size; i += 4) {
            assertEquals(structure.findSet(i), structure.findSet(i + 3));
        }

        assertEquals(size / 4, countDistinctSets(structure));
    }

    @Test
    public void eightElementSets() throws ItemOutOfRangeException {
        var structure = factory.apply(size);

        for (var i = 0; i < size; i += 2) {
            structure.union(i, i + 1);
        }

        for (var i = 0; i < size; i += 4) {
            structure.union(i, i + 2);
        }

        for (var i = 0; i < size; i += 8) {
            structure.union(i, i + 4);
        }

        for (var i = 0; i < size; i += 8) {
            assertEquals(structure.findSet(i), structure.findSet(i+7));
        }

        assertEquals(size / 8, countDistinctSets(structure));
    }

    @Test
    public void oneSixteenElementsSet_composedByEqualMerges() throws ItemOutOfRangeException {
        var structure = factory.apply(size);

        for (var i = 0; i < size; i += 2) {
            structure.union(i, i + 1);
        }

        for (var i = 0; i < size; i += 4) {
            structure.union(i, i + 2);
        }

        for (var i = 0; i < size; i += 8) {
            structure.union(i, i + 4);
        }

        structure.union(0, 8);

        assertEquals(1, countDistinctSets(structure));
    }

    @Test
    public void oneSixteenElementsSet_composedLinearly() throws ItemOutOfRangeException {
        var structure = factory.apply(size);

        for (var i = 0; i < size - 1; i += 1) {
            structure.union(i, i + 1);
        }

        assertEquals(1, countDistinctSets(structure));
    }

    @Test
    public void findSet_negativeItem() {
        var structure = factory.apply(size);

        assertThrows(ItemOutOfRangeException.class, () -> {
            structure.findSet(-3);
        });
    }

    @Test
    public void findSet_itemGreaterThanSize() {
        var structure = factory.apply(size);

        assertThrows(ItemOutOfRangeException.class, () -> {
            structure.findSet(size);
        });
    }

    @Test
    public void union_item1Negative() {
        var structure = factory.apply(size);

        assertThrows(ItemOutOfRangeException.class, () -> {
            structure.union(-3, 2);
        });
    }

    @Test
    public void union_item1GreaterThanSize() {
        var structure = factory.apply(size);

        assertThrows(ItemOutOfRangeException.class, () -> {
            structure.union(size, 2);
        });
    }

    @Test
    public void union_item2Negative() {
        var structure = factory.apply(size);

        assertThrows(ItemOutOfRangeException.class, () -> {
            structure.union(3, -2);
        });
    }

    @Test
    public void union_item2GreaterThanSize() {
        var structure = factory.apply(size);

        assertThrows(ItemOutOfRangeException.class, () -> {
            structure.union(3, size);
        });
    }

    private static long countDistinctSets(IDisjointSetStructure structure) {
        return IntStream.rangeClosed(0, size - 1)
                .map(item -> findSet(structure, item))
                .distinct()
                .count();
    }

    private static int findSet(IDisjointSetStructure structure, int item) {
        try {
            return structure.findSet(item);
        } catch (ItemOutOfRangeException exception) {
            fail(exception);
            return -1;
        }
    }
}
