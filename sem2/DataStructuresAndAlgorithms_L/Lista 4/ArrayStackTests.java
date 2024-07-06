package com.company;

import com.company.exceptions.FullStackException;
import java.util.EmptyStackException;
import static org.junit.jupiter.api.Assertions.*;

public class ArrayStackTests {
    @org.junit.jupiter.api.Test
    void fullTest() {
        var stack = createStack(5, 5);

        for (var i = 5; i > 0; i--) {
            assertEquals(i, stack.top());
            assertEquals(i, stack.pop());
        }

        assertEquals(0, stack.size());
    }

    @org.junit.jupiter.api.Test
    void isEmptyOnEmptyStack() {
        var stack = createStack(5, 0);
        assertTrue(stack.isEmpty());
    }

    @org.junit.jupiter.api.Test
    void isEmptyOnNotEmptyStack() {
        var stack = createStack(5, 3);
        assertFalse(stack.isEmpty());
    }

    @org.junit.jupiter.api.Test
    void isFullOnNotFullStack() {
        var stack = createStack(5, 3);
        assertFalse(stack.isFull());
    }

    @org.junit.jupiter.api.Test
    void isFullOnFullStack() {
        var stack = createStack(5, 5);
        assertTrue(stack.isFull());
    }

    @org.junit.jupiter.api.Test
    void topOnNonEmptyStack() {
        var stack = createStack(5, 3);
        assertEquals(3, stack.top());
    }

    @org.junit.jupiter.api.Test
    void topDoesntPop() {
        var stack = createStack(5, 3);
        assertEquals(3, stack.top());
        assertEquals(3, stack.top());
    }

    @org.junit.jupiter.api.Test
    void topOnEmptyStack() {
        var stack = createStack(5, 0);
        assertThrows(EmptyStackException.class, () -> {
            stack.top();
        });
    }

    @org.junit.jupiter.api.Test
    void popOnNonEmptyStack() {
        var stack = createStack(5, 3);
        assertEquals(3, stack.pop());
    }

    @org.junit.jupiter.api.Test
    void popTakesDownTopElement() {
        var stack = createStack(5, 3);
        assertEquals(3, stack.pop());
        assertEquals(2, stack.pop());
    }

    @org.junit.jupiter.api.Test
    void popOnEmptyStack() {
        var stack = createStack(5, 0);
        assertThrows(EmptyStackException.class, () -> {
            stack.pop();
        });
    }

    @org.junit.jupiter.api.Test
    void pushToEmptyStack() throws FullStackException {
        var stack = createStack(5, 0);
        stack.push(1);
        assertEquals(1, stack.top());
    }

    @org.junit.jupiter.api.Test
    void pushToNotEmptyStack() throws FullStackException {
        var stack = createStack(5, 3);
        stack.push(4);
        assertEquals(4, stack.top());
    }

    @org.junit.jupiter.api.Test
    void pushToFullStack() {
        var stack = createStack(5, 5);
        assertThrows(FullStackException.class, () -> {
            stack.push(6);
        });
    }

    @org.junit.jupiter.api.Test
    void sizeOfEmptyStack() {
        var stack = createStack(5, 0);
        assertEquals(0, stack.size());
    }

    @org.junit.jupiter.api.Test
    void sizeOfNotEmptyStack() {
        var stack = createStack(5, 3);
        assertEquals(3, stack.size());
    }

    @org.junit.jupiter.api.Test
    void sizeOfFullStack() {
        var stack = createStack(5, 5);
        assertEquals(5, stack.size());
    }

    private IStack<Integer> createStack(int capacity, int elements) {
        var stack = new ArrayStack<Integer>(capacity);

        try {
            for (var i = 1; i <= elements; i++) {
                stack.push(i);
            }
        } catch (Exception exception) {
            fail(exception);
        }

        return stack;
    }
}
