package com.company;

import com.company.exceptions.*;

// Proszę nie modyfikować tego pliku!
public interface IQueue<T> {
    boolean isEmpty();
    boolean isFull();
    void enqueue(T value) throws FullQueueException;
    T first() throws EmptyQueueException;
    T dequeue() throws EmptyQueueException;
    int size();
}
