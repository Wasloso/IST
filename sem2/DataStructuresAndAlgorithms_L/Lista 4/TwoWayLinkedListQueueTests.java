package com.company;

import com.company.exceptions.*;

import static org.junit.jupiter.api.Assertions.*;

public class TwoWayLinkedListQueueTests {
    @org.junit.jupiter.api.Test
    void fullTest() throws EmptyQueueException {
        var queue = createQueue(5, 5);

        for (var i = 1; i <= 5; i++) {
            assertEquals(i, queue.first());
            assertEquals(i, queue.dequeue());
        }

        assertEquals(0, queue.size());
    }

    @org.junit.jupiter.api.Test
    void isEmptyOnEmptyQueue() {
        var queue = createQueue(5, 0);
        assertTrue(queue.isEmpty());
    }

    @org.junit.jupiter.api.Test
    void isEmptyOnNotEmptyQueue() {
        var queue = createQueue(5, 3);
        assertFalse(queue.isEmpty());
    }

    @org.junit.jupiter.api.Test
    void isFullOnNotFullQueue() {
        var queue = createQueue(5, 3);
        assertFalse(queue.isFull());
    }

    @org.junit.jupiter.api.Test
    void isFullOnFullQueue() {
        var queue = createQueue(5, 5);
        assertTrue(queue.isFull());
    }

    @org.junit.jupiter.api.Test
    void firstOnNonEmptyQueue() throws EmptyQueueException {
        var queue = createQueue(5, 3);
        assertEquals(1, queue.first());
    }

    @org.junit.jupiter.api.Test
    void firstDoesntDequeue() throws EmptyQueueException {
        var queue = createQueue(5, 3);
        assertEquals(1, queue.first());
        assertEquals(1, queue.first());
    }

    @org.junit.jupiter.api.Test
    void firstOnEmptyQueue() {
        var queue = createQueue(5, 0);
        assertThrows(EmptyQueueException.class, () -> {
            queue.first();
        });
    }

    @org.junit.jupiter.api.Test
    void dequeueOnNonEmptyQueue() throws EmptyQueueException {
        var queue = createQueue(5, 3);
        assertEquals(1, queue.dequeue());
    }

    @org.junit.jupiter.api.Test
    void dequeueTakesDownFirstElement() throws EmptyQueueException {
        var queue = createQueue(5, 3);
        assertEquals(1, queue.dequeue());
        assertEquals(2, queue.dequeue());
    }

    @org.junit.jupiter.api.Test
    void dequeueOnEmptyQueue() {
        var queue = createQueue(5, 0);
        assertThrows(EmptyQueueException.class, () -> {
            queue.dequeue();
        });
    }

    @org.junit.jupiter.api.Test
    void enqueueOnEmptyQueue() throws FullQueueException, EmptyQueueException {
        var queue = createQueue(5, 0);
        queue.enqueue(1);
        assertEquals(1, queue.first());
    }

    @org.junit.jupiter.api.Test
    void enqueueOnNotEmptyQueue() throws FullQueueException, EmptyQueueException {
        var queue = createQueue(5, 3);
        queue.enqueue(4);
        assertEquals(1, queue.first());
    }

    @org.junit.jupiter.api.Test
    void enqueueOnFullQueue() {
        var queue = createQueue(5, 5);
        assertThrows(FullQueueException.class, () -> {
            queue.enqueue(6);
        });
    }

    @org.junit.jupiter.api.Test
    void sizeOfEmptyQueue() {
        var queue = createQueue(5, 0);
        assertEquals(0, queue.size());
    }

    @org.junit.jupiter.api.Test
    void sizeOfNotEmptyQueue() {
        var queue = createQueue(5, 3);
        assertEquals(3, queue.size());
    }

    @org.junit.jupiter.api.Test
    void sizeOfFullQueue() {
        var queue = createQueue(5, 5);
        assertEquals(5, queue.size());
    }

    private IQueue<Integer> createQueue(int capacity, int elements) {
        var queue = new TwoWayLinkedListQueue<Integer>(capacity);

        try {
            for (var i = 1; i <= elements; i++) {
                queue.enqueue(i);
            }
        } catch (Exception exception) {
            fail(exception);
        }

        return queue;
    }
}
