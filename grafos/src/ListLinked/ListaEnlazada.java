package ListLinked;

import java.util.Iterator;

public class ListaEnlazada <E extends Comparable<E>> implements Iterable<E> {

    private Node<E> first;

    public ListaEnlazada() {
        this.first = null;
    }

    public Node<E> getFirst() {
        return this.first;
    }

    public boolean isEmptyList() {
        return this.first == null;
    }

    public int length() {
        int size = 0;
        Node<E> actual = this.first;
        while (actual != null) {
            actual = actual.next;
            size++;
        }
        return size;
    }

    public void destroyList() {
        this.first = null;
    }

    public int search(E data) {
        if (this.isEmptyList()) return -1;
        int index = 0;
        Node<E> actual = this.first;
        while (actual != null) {
            if (actual.data.compareTo(data) == 0) {
                return index;
            }
            actual = actual.next;
            index++;
        }
        return -1;
    }

    public void insertFirst(E data) {
        Node<E> nodo = new Node<>(data);
        nodo.next = this.first;
        this.first = nodo;
    }

    public void insertLast(E data) {
        Node<E> nodo = new Node<>(data);
        if (this.isEmptyList()) {
            this.first = nodo;
        } else {
            Node<E> actual = this.first;
            while (actual.next != null) {
                actual = actual.next;
            }
            actual.next = nodo;
        }
    }

    public boolean remove(E data) {
        if (this.isEmptyList()) return false;
        if (this.first.data.equals(data)) {
            this.first = this.first.next;
            return true;
        }
        Node<E> actual = this.first;
        while (actual.next != null && !actual.next.data.equals(data)) {
            actual = actual.next;
        }
        if (actual.next == null) return false;
        actual.next = actual.next.next;
        return true;
    }

    public boolean contains(E obj) {
        return this.search(obj) != -1;
    }

    public E getMax() {
        if (this.isEmptyList()) return null;
        Node<E> actual = this.first;
        E max = this.first.data;
        while (actual != null) {
            if (actual.data.compareTo(max) > 0) {
                max = actual.data;
            }
            actual = actual.next;
        }
        return max;
    }

    public void reverse() {
        if (this.isEmptyList()) return;
        ListaEnlazada<E> aux = new ListaEnlazada<>();
        Node<E> actual = this.first;
        while (actual != null) {
            aux.insertFirst(actual.data);
            actual = actual.next;
        }
        this.first = aux.first;
    }

    public boolean equals(ListaEnlazada<E> otraLista) {
        Node<E> actual1 = this.first;
        Node<E> actual2 = otraLista.getFirst();
        while (actual1 != null && actual2 != null) {
            if (!actual1.data.equals(actual2.data)) return false;
            actual1 = actual1.next;
            actual2 = actual2.next;
        }
        return actual1 == null && actual2 == null;
    }

    public ListaEnlazada<E> concatenate(ListaEnlazada<E> otraLista) {
        ListaEnlazada<E> result = new ListaEnlazada<>();
        Node<E> actual = this.first;
        while (actual != null) {
            result.insertLast(actual.data);
            actual = actual.next;
        }
        actual = otraLista.getFirst();
        while (actual != null) {
            result.insertLast(actual.data);
            actual = actual.next;
        }
        return result;
    }

    @Override
    public Iterator<E> iterator() {
        return new IteradorLista();
    }

    private class IteradorLista implements Iterator<E> {
        private Node<E> actual = first;

        @Override
        public boolean hasNext() {
            return actual != null;
        }

        @Override
        public E next() {
            E dato = actual.data;
            actual = actual.next;
            return dato;
        }
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node<E> actual = this.first;
        while (actual != null) {
            sb.append(actual.data);
            if (actual.next != null) {
                sb.append(", ");
            }
            actual = actual.next;
        }
        return sb.toString();
    }

}
