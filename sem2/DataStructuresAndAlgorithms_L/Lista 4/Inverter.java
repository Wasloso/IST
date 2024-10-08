package com.company;

import com.company.exceptions.EmptyQueueException;
import com.company.exceptions.FullQueueException;
import com.company.exceptions.FullStackException;

public class Inverter {

    // Odwróć kolejność elementów w kolejce wykorzystując do tego stos zaimplementowany w klasie ArrayStack
    public static <T> void invert(IQueue<T> queue) throws EmptyQueueException, FullQueueException, FullStackException {
        ArrayStack<T> stack = new ArrayStack<>(queue.size());
        while (!queue.isEmpty()){
            stack.push(queue.dequeue());
        }
        while (!stack.isEmpty()){
            queue.enqueue(stack.pop());
        }
    }
}
