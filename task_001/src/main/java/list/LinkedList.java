package list;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @param <E> Реализация динамического контейнера на основе связного списка
 * @author Fedorov Aleksandr (msg2fedorov@gmail.com)
 */

public class LinkedList<E> implements Iterable {
    private Node<E> firstNode;
    private Node<E> lastNode;
    private int modCount;

    public void add(E element) {
        this.addLast(element);
    }

    public void add(int index, E element) {
        this.checkPositionIndex(index);
        if (index == this.modCount) {
            this.addLast(element);
        } else {
            this.linkBefore(element, nodeExtractor(index));
        }
    }

    private void addLast(E date) {
        Node<E> lastNode = this.lastNode;
        Node<E> newNode = new Node<>(lastNode, date, null);
        this.lastNode = newNode;
        if (lastNode != null) {
            lastNode.next = newNode;
        } else {
            this.firstNode = newNode;
        }
        sizeAdder();
    }

    public E get(int index) {
        Node<E> result = this.firstNode;
        for (int i = 0; i < index; i++) {
            result = result.next;
        }
        return result.date;
    }

    public E dropFirst() {
        E result = this.firstNode.date;
        if (modCount > 1) {
            this.firstNode.next.prev = null;
            this.firstNode = this.firstNode.next;
        } else {
            this.firstNode = null;
            this.lastNode = null;
        }
        sizeDecreaser();
        return result;
    }

    public E dropLast() {
        E result = this.lastNode.date;
        if (modCount > 1) {
            this.lastNode.prev.next = null;
            this.lastNode = this.lastNode.prev;
        } else {
            this.firstNode = null;
            this.lastNode = null;
        }
        sizeDecreaser();
        return result;
    }

    public int getSize() {
        return this.modCount;
    }

    private void sizeAdder() {
        this.modCount++;
    }

    private void sizeDecreaser() {
        this.modCount--;
    }

    private static class Node<E> {
        E date;
        Node<E> prev;
        Node<E> next;

        private Node(Node<E> prev, E date, Node<E> next) {
            this.prev = prev;
            this.date = date;
            this.next = next;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new LinkedListIterator<>(firstNode, modCount);
    }

    private class LinkedListIterator<E> implements Iterator<E> {
        private Node<E> current;
        private int expectedModCount;

        private LinkedListIterator(Node<E> elements, int expectedModCount) {
            this.expectedModCount = expectedModCount;
            this.current = elements;
        }

        @Override
        public boolean hasNext() {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            return current != null && current.next != null;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            E res = current.next.date;
            current = current.next;
            return res;
        }
    }

    private void checkPositionIndex(int index) {
        if (!this.isPositionIndex(index)) {
            throw new IndexOutOfBoundsException(this.outOfBoundsMsg(index));
        }
    }

    private boolean isPositionIndex(int index) {
        return index >= 0 && index <= this.modCount;
    }

    private String outOfBoundsMsg(int index) {
        return "Index: " + index + ", Size: " + this.modCount;
    }

    private void linkBefore(E e, Node<E> succ) {
        Node<E> pred = succ.prev;
        Node<E> newNode = new Node(pred, e, succ);
        succ.prev = newNode;
        if (pred == null) {
            this.firstNode = newNode;
        } else {
            pred.next = newNode;
        }

        ++this.modCount;
    }

    private Node<E> nodeExtractor(int index) {
        Node x;
        int i;
        if (index < this.modCount >> 1) {
            x = this.firstNode;
            for (i = 0; i < index; ++i) {
                x = x.next;
            }
            return x;
        } else {
            x = this.lastNode;
            for (i = this.modCount - 1; i > index; --i) {
                x = x.prev;
            }
            return x;
        }
    }
}