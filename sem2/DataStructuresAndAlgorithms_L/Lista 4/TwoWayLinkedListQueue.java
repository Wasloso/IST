package com.company;

import com.company.exceptions.*;

public class TwoWayLinkedListQueue<T> implements IQueue<T> {

    private class Element {
        T data;
        Element next;
        Element previous;

        Element(T data) {
            this.data = data;
            this.next = null;
            this.previous = null;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public Element getNext() {
            return next;
        }

        public void setNext(Element next) {
            this.next = next;
            next.setPrevious(this);
        }

        public Element getPrevious() {
            return previous;
        }

        public void setPrevious(Element previous) {
            this.previous = previous;
        }
    }

    Element head;
    Element tail;
    int size;
    int capacity;

    public TwoWayLinkedListQueue(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.head = null;
        this.tail = null;
    }

    @Override
    public boolean isEmpty() {
        return head == null;
    }

    @Override
    public boolean isFull() {
        return size == capacity;
    }

    @Override
    public void enqueue(T value) throws FullQueueException {
        if (isFull()) throw new FullQueueException();
        Element newElement = new Element(value);
        if (isEmpty()) {
            head = tail = newElement;
        } else {
            tail.setNext(newElement);
            tail = newElement;
        }
        size++;
    }

    @Override
    public T first() throws EmptyQueueException {
        if(isEmpty()) throw new EmptyQueueException();
        return head.getData();
    }

    @Override
    public T dequeue() throws EmptyQueueException {
        if (isEmpty()) throw new EmptyQueueException();
        T retData = head.getData();
        head = head.getNext();
        if (!isEmpty())
            head.setPrevious(null);
        size--;
        return retData;
    }

    @Override
    public int size() {
        return size;
    }
}
