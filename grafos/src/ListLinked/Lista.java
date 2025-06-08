package ListLinked;

public interface Lista <E extends Comparable<E>> extends Iterable<E> {
    public boolean isEmptyList();
    public int length();
    public void destroyList();
    public int search(E obj);
    public void insertFirst(E obj);
    public void insertLast(E obj);
    public boolean remove(E obj);
    public void reverse(); 
    public boolean contains(E obj);
    public E getMax();
}
