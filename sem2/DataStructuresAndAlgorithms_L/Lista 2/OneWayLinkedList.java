package com.company;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class OneWayLinkedList<T> implements IList<T> {
    private class Element {
        T data;
        Element nextElement;

        Element(T data) {
            this.data = data;
            nextElement = null;
        }

        @Override
        public String toString() {
            return "Element data : " + data;
        }
    }

    Element head = null;

    @Override
    public void add(T value) {
        Element newElement = new Element(value);
        if (head == null)
            head = newElement;
        else {
            Element lastElement = this.head;
            while (lastElement.nextElement != null) {
                lastElement = lastElement.nextElement;
            }
            lastElement.nextElement = newElement;
        }
    }

    @Override
    public void addAt(int index, T value) throws NoSuchElementException {
        if (index < 0) throw new NoSuchElementException();
        Element newElement = new Element(value);
        if (index == 0) {
            newElement.nextElement = head;
            head = newElement;
        } else {
            Element beforeElement = getElement(index - 1);
            newElement.nextElement = beforeElement.nextElement;
            beforeElement.nextElement = newElement;
        }
    }

    @Override
    public void clear() {
        head = null;
    }

    @Override
    public boolean contains(T value) {
        Element actElement = head;
        if (indexOf(value) != -1) {
            return true;
        } else return false;
    }

    @Override
    public T get(int index) throws NoSuchElementException {
        if (index < 0) throw new IndexOutOfBoundsException();
        Element actElement = head;
        while (index > 0 && actElement != null) {
            index--;
            actElement = actElement.nextElement;
        }
        if (actElement == null) {
            throw new IndexOutOfBoundsException();
        }
        return actElement.data;
    }

    private Element getElement(int index) {
        if (index < 0) throw new NoSuchElementException();
        Element actElement = head;
        while (index > 0 && actElement != null) {
            index--;
            actElement = actElement.nextElement;
        }
        if (actElement == null) {
            throw new NoSuchElementException();
        }
        return actElement;
    }

    @Override
    public void set(int index, T value) throws NoSuchElementException {
        Element actElement = getElement(index);
        actElement.data = value;
    }

    @Override
    public int indexOf(T value) {
        Element actElement = head;
        int index = 0;
        while (actElement != null) {
            if (actElement.data.equals(value)) {
                return index;
            }
            index++;
            actElement = actElement.nextElement;
        }
        return -1;
    }

    @Override
    public boolean isEmpty() {
        if (size() == 0) return true;
        return false;
    }

    @Override
    public T removeAt(int index) throws NoSuchElementException {
        if (index < 0) throw new NoSuchElementException();
        Element actElement = getElement(index);
        if (index == 0) {
            head = actElement.nextElement;
            return actElement.data;
        } else {
            Element beforeElement = getElement(index - 1);
            beforeElement.nextElement = actElement.nextElement;
            return actElement.data;
        }
    }

    @Override
    public boolean remove(T value) {
        Element actElement = head;
        if (head != null && head.data.equals(value)) {
            head = head.nextElement;
            return true;
        }
        while (actElement != null) {
            if (actElement.data.equals(value)) {
                Element beforeElement = getElement(indexOf(value) - 1);
                beforeElement.nextElement = actElement.nextElement;
                return true;
            }
            actElement = actElement.nextElement;
        }
        return false;
    }

    @Override
    public int size() {
        int size = 0;
        Element actElement = head;
        while (actElement != null) {
            size++;
            actElement = actElement.nextElement;
        }
        return size;
    }

    @Override
    public Iterator<T> iterator() {
        return new OneWayLinkedListIterator();
    }

    private class OneWayLinkedListIterator implements Iterator<T> {
        Element actElement = head;

        @Override
        public boolean hasNext() {
            return (actElement!=null);
        }

        @Override
        public T next() {
            if(actElement==null) throw new NoSuchElementException();
            T retData = actElement.data;
            actElement = actElement.nextElement;
            return retData;
        }
    }
}
