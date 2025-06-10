package graph;
/**
 * Clase que representa una arista en un grafo.
 * @param <E> Tipo de dato que almacena el vértice destino.
 */
public class Edge<E> implements Comparable<Edge<E>>{
	private Vertex<E> refDest; //representa el vertice destino o a quien apunta la arista
	private int weight;
	
	public Edge(Vertex<E> refDest) {
		this(refDest, -1);
	}
	public Edge(Vertex<E> refDest, int weight) {
		this.refDest = refDest;
		this.weight = weight;
	}
	public Vertex<E> getrefDest() {
        return refDest;
    }
	public int getWeight(){
		return weight;
	}
	//si dos aristas van en el mismo vertice destino
	public boolean equals(Object o) {
		if (o instanceof Edge<?>) {
			Edge<E> e = (Edge<E>)o;
			return this.refDest.equals(e.refDest);
		}
		return false;
	}
	
	//comparar aristas por sus pesos
    public int compareTo(Edge<E> o) {
        return Integer.compare(this.weight, o.weight);
    }
   
    public String toString() {
        return weight >= 0 ? refDest.getData() + "(" + weight + ")" : refDest.getData().toString();
    }



}
