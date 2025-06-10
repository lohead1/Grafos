package graph;

public class EdgeObj<V, E> implements Comparable<EdgeObj<V, E>> {
    private VertexObj<V, E> endVertex1;
    private VertexObj<V, E> endVertex2;
    private E weight;
    private int position;

    public EdgeObj(VertexObj<V, E> v1, VertexObj<V, E> v2, E weight, int position) {
        this.endVertex1 = v1;
        this.endVertex2 = v2;
        this.weight = weight;
        this.position = position;
    }

    public VertexObj<V, E> getEndVertex1() {
        return endVertex1;
    }

    public VertexObj<V, E> getEndVertex2() {
        return endVertex2;
    }

    public E getWeight() {
        return weight;
    }

    @Override
    public int compareTo(EdgeObj<V, E> other) {
        return Integer.compare(this.position, other.position);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof EdgeObj)) return false;
        EdgeObj<V, E> other = (EdgeObj<V, E>) obj;
        return (endVertex1.equals(other.endVertex1) && endVertex2.equals(other.endVertex2)) ||
               (endVertex1.equals(other.endVertex2) && endVertex2.equals(other.endVertex1));
    }

    @Override
    public String toString() {
        return "(" + endVertex1 + " - " + endVertex2 + ")";
    }
}
