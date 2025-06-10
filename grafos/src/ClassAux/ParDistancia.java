package ClassAux;

public class ParDistancia<E extends Comparable<E>> implements Comparable<ParDistancia<E>> {
    private E vertice;
    private int distancia;
    private E padre;
    
    public ParDistancia(E vertice, int distancia, E padre) {
        this.vertice = vertice;
        this.distancia = distancia;
        this.padre = padre;
    }
    
    public E getVertice() { return vertice; }
    public int getDistancia() { return distancia; }
    public E getPadre() { return padre; }
    
    public void setDistancia(int distancia) { this.distancia = distancia; }
    public void setPadre(E padre) { this.padre = padre; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ParDistancia<?> that = (ParDistancia<?>) obj;
        return vertice.equals(that.vertice);
    }
    
    @Override
    public int compareTo(ParDistancia<E> other) {
        return this.vertice.compareTo(other.vertice);
    }
}