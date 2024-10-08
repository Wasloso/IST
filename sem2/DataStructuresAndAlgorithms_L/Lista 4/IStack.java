package com.company;

import com.company.exceptions.FullStackException;

import java.util.EmptyStackException;

// Proszę nie modyfikować tego pliku!
public interface IStack<T> {
    boolean isEmpty();
    boolean isFull();
    T top() throws EmptyStackException;
    T pop() throws EmptyStackException;
    void push(T value) throws FullStackException;
    int size();
}
