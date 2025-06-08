package graph;

import ListLinked.ListaEnlazada;
/**
 * Clase que representa un vértice en un grafo
 * @param <E> Tipo de dato que almacena el vértice
 */

public class Vertex<E> implements Comparable<Vertex<E>>{
	private E data;
	protected ListaEnlazada<Edge<E>> listAdj; //lista que va aguardar las arista

	public Vertex(E data) {
		this.data = data;
		listAdj = new ListaEnlazada<>();
	}
	
	public E getData() {
		return data;
	}
	
	//compara dos vertices por su contenido
	public boolean equals(Object o) {
		if(o instanceof Vertex<?>) {
			Vertex<E> v = (Vertex<E>) o;
			return this.data.equals(v.data);
		}
		return false;
	}
	
    public int compareTo(Vertex<E> o) {
        if (this.data instanceof Comparable) {
            return ((Comparable<E>) this.data).compareTo(o.data);
        } else {
            throw new IllegalArgumentException("El tipo E debe implementar Comparable<E>");
        }
    }
    
	public String toString() {
		return this.data+ "-->"+ this.listAdj.toString();
	}

}
