package com.company;

import java.util.Iterator;
import java.util.NoSuchElementException;

public interface IList<T> extends Iterable<T> {
    void add(T value);
    void addAt(int index, T value) throws NoSuchElementException;
    void clear();
    boolean contains(T value);
    T get(int index) throws NoSuchElementException;
    void set(int index, T value) throws NoSuchElementException;
    int indexOf(T value);
    boolean isEmpty();
    T removeAt(int index) throws NoSuchElementException;
    boolean remove(T value);
    int size();
    Iterator<T> iterator();
}
