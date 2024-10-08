package com.company;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class TwoWayLinkedList<T> {
    public class Element {
        private T data;
        private Element next;
        private Element prev;

        public Element(T data) {
            this.data = data;
            this.next = null;
            this.prev = null;
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
        }

        public Element getPrev() {
            return prev;
        }

        public void setPrev(Element prev) {
            this.prev = prev;
        }
    }

    Element head;
    Element tail;
    int size = 0;

    private Element elementAtIndex(int index) {
        if (index < 0 || index>=size) throw new IndexOutOfBoundsException();
        Element retElement = head;
        for (int i = 0; i < index; i++) {
            retElement = retElement.getNext();
        }
        return retElement;
    }

    private Element elementAtValue(T value) {
        Element retElement = head;
        for (int i = 0; i < size; i++) {
            if (retElement.getData() == value)
                return retElement;
            retElement = retElement.getNext();
        }
        throw new NoSuchElementException();
    }

    public void add(T value) {
        Element newElement = new Element(value);
        if (head == null) head = tail = newElement;
        else {
            tail.setNext(newElement);
            newElement.setPrev(tail);
            tail = newElement;
        }
        size++;
    }
    public void addElement(Element element){
        add(element.getData());
    }

    public void addAt(int index, T value) throws NoSuchElementException {
        if (index < 0 || index > size) throw new NoSuchElementException();
        Element newElement = new Element(value);
        if (index == 0) {
            newElement.setNext(head);
            head.setPrev(newElement);
            head = newElement;
            return;
        }
        if (index == size) {
            newElement.setPrev(tail);
            tail.setNext(newElement);
            tail = newElement;
            return;
        }
        Element elementAtIndex = elementAtIndex(index);
        newElement.setNext(elementAtIndex);
        newElement.setPrev(elementAtIndex.getPrev());
        elementAtIndex.getPrev().setNext(newElement);
        elementAtIndex.setPrev(newElement);

    }

    public void clear() {
        head = tail = null;
    }

    public boolean contains(T value) {
        Element tmp = head;
        for (int i = 0; i < size; i++) {
            if (tmp.getData().equals(value))
                return true;
            tmp = tmp.getNext();
        }
        return false;
    }

    public T get(int index) throws NoSuchElementException {
        if (index < 0 || index > size) throw new NoSuchElementException();
        return elementAtIndex(index).getData();
    }

    public void set(int index, T value) throws NoSuchElementException {
        if (index < 0 || index > size) throw new NoSuchElementException();
        elementAtIndex(index).setData(value);
    }

    public int indexOf(T value) {
        Element tmp = head;
        for (int i = 0; i < size; i++) {
            if (tmp.getData().equals(value)) return i;
            tmp = tmp.getNext();
        }
        return -1;
    }

    public boolean isEmpty() {
        return (head == null);
    }

    public T removeAt(int index) throws NoSuchElementException {
        if (index < 0 || index > size) throw new NoSuchElementException();
        T retData;
        Element actElement = elementAtIndex(index);
        retData = actElement.getData();
        removeElement(actElement);
        return retData;
    }

    public boolean remove(T value) {
        try {
            Element actElement = elementAtValue(value);
            removeElement(actElement);
            return true;

        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean removeElement(Element element) {
        if (element.equals(head)) {
            head.getNext().setPrev(null);
            head = head.getNext();
            return true;
        } else if (element.equals(tail)) {
            tail.getPrev().setNext(null);
            tail = tail.getPrev();
            return true;
        } else {
            element.getPrev().setNext(element.getNext());
            element.getNext().setPrev(element.getPrev());
            return true;
        }

    }

    private void getList() {
        Element actElement = head;
        for (int i = 0; i < size; i++) {
            System.out.println(actElement.data);
            actElement = actElement.getNext();
        }
    }

    public int size() {
        return size;
    }

    public Iterator<T> iterator() {
        return new TwoWayLinkedListIterator();
    }

    private class TwoWayLinkedListIterator implements Iterator<T> {
        Element pos = head;

        @Override
        public boolean hasNext() {
            if (pos != null) return true;
            return false;
        }

        @Override
        public T next() {
            if (hasNext()) {
                T retValue = pos.getData();
                pos = pos.getNext();
                return retValue;
            } else throw new NoSuchElementException();
        }
    }

    public void insert(
            TwoWayLinkedList<T> anotherList,
            int beforeIndex) throws NoSuchElementException {

        try {
            Element actElement = elementAtIndex(beforeIndex);
            anotherList.tail.setNext(actElement);
            if (actElement.equals(head)) {
                head = anotherList.head;
            } else {
                anotherList.head.setPrev(actElement.getPrev());
                actElement.getPrev().setNext(anotherList.head);
            }
            actElement.setPrev(anotherList.tail);
        }catch (IndexOutOfBoundsException e){
            throw new NoSuchElementException();
        }


    }

    public void insert(
            TwoWayLinkedList<T> anotherList,
            T beforeElement) throws NoSuchElementException {

        Element actElement = elementAtValue(beforeElement);
        anotherList.tail.setNext(actElement);
        if (actElement.equals(head)) {
            head = anotherList.head;


        } else {
            anotherList.head.setPrev(actElement.getPrev());
            actElement.getPrev().setNext(anotherList.head);
        }
        actElement.setPrev(anotherList.tail);
    }
}
