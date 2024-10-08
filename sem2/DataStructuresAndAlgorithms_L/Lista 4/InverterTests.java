package com.company;

import com.company.exceptions.EmptyQueueException;
import com.company.exceptions.FullQueueException;
import com.company.exceptions.FullStackException;

import static org.junit.jupiter.api.Assertions.*;

public class InverterTests {
    @org.junit.jupiter.api.Test
    void emptyQueue() throws EmptyQueueException, FullQueueException, FullStackException {
        var queue = createQueue(1, 0);
        Inverter.invert(queue);

        assertTrue(queue.isEmpty());
    }

    @org.junit.jupiter.api.Test
    void oneElementQueue() throws EmptyQueueException, FullQueueException, FullStackException {
        var queue = createQueue(1, 1);
        Inverter.invert(queue);

        assertEquals(1, queue.size());
        assertEquals(1, queue.dequeue());
    }

    @org.junit.jupiter.api.Test
    void threeElementQueue() throws EmptyQueueException, FullQueueException, FullStackException {
        var queue = createQueue(3, 3);
        Inverter.invert(queue);

        assertEquals(3, queue.size());
        assertEquals(3, queue.dequeue());
        assertEquals(2, queue.dequeue());
        assertEquals(1, queue.dequeue());
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
