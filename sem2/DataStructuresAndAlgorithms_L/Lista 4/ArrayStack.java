package com.company;

import com.company.exceptions.FullStackException;

import java.util.EmptyStackException;

public class ArrayStack<T> implements IStack<T> {

    private T[] array;
    private int topIndex;
    private final int capacity;

    @SuppressWarnings("unchecked")
    public ArrayStack(int capacity) {
        this.array = (T[])(new Object[capacity]);
        this.capacity = capacity;
        this.topIndex = 0;
    }

    @Override
    public boolean isEmpty() {
        return topIndex == 0;
    }

    @Override
    public boolean isFull() {
        return topIndex == capacity;
    }

    @Override
    public T top() throws EmptyStackException {
        if (isEmpty()) throw new EmptyStackException();
        return array[topIndex - 1];
    }

    @Override
    public T pop() throws EmptyStackException {
        if (isEmpty()) throw new EmptyStackException();
        return array[--topIndex];
    }

    @Override
    public void push(T value) throws FullStackException {
        if (isFull()) throw new FullStackException();
        array[topIndex++] = value;
    }

    @Override
    public int size() {
        return topIndex;
    }
}
