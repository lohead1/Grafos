package graph;

import java.util.Vector;

public class GraphLink<E extends Comparable<E>> {
    protected ListLinked<Vertex<E>> listVertex;
    private boolean esDirigido;

    public GraphLink(boolean esDirigido) {
        listVertex = new ListLinked<Vertex<E>>();
        this.esDirigido = esDirigido;
    }

    public void insertVertex(E data) {
        if (data == null) {
            throw new IllegalArgumentException("el dato no puede ser null");
        }
        Vertex<E> nv = new Vertex<>(data);
        if (listVertex.contains(nv)) {
            listVertex.insertLast(nv);
        } else {
            throw new RuntimeException("el elemento no esta contenido");
        }
    }

    public void insertEdge(E verOri, E verDes) {
        if(verDes==null && verOri==null){
            throw new IllegalArgumentException("los vectores de destino y origen son nulo");
        }
    }


    }
