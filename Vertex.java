package graph;

public class Vertex<E extends Comparable<E>> implements Comparable<Vertex<E>> {
    private E data;
    private ListLinked<Edge<E>> listAdj;

    public Vertex(E data){
        this.data=data;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Vertex<?>){
            Vertex<E> v=(Vertex<E>) obj;
            return this.data.equals(v.data);
        }
        return super.equals(obj);
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "data=" + data +
                ", listAdj=" + listAdj +
                '}';
    }

    @Override
    public int compareTo(Vertex<E> o) {
        return 0;
    }
}
