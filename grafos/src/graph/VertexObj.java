package graph;

public class VertexObj<V, E> implements Comparable<VertexObj<V, E>> {
    private V info;
    private int position;

    public VertexObj(V info, int position) {
        this.info = info;
        this.position = position;
    }

    public V getInfo() {
        return info;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public int compareTo(VertexObj<V, E> other) {
        return Integer.compare(this.position, other.position);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof VertexObj)) return false;
        VertexObj<V, E> other = (VertexObj<V, E>) obj;
        return info.equals(other.info);
    }

    @Override
    public String toString() {
        return info.toString();
    }
}