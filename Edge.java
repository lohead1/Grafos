package graph;

import java.util.Objects;

public class Edge<E extends Comparable<E>> implements Comparable<Edge<E>> {
    private int weigh;
    protected Vertex<E>refdes;
    //Conocer peso
    public Edge(Vertex<E>refdes){
        this.refdes=null;
    }
    public Edge(Vertex<E>refdes,int weigh){
        this.refdes=refdes;
        this.weigh=weigh;
    }

    public boolean equals(Object o){
        if(o instanceof Edge<?>){
            Edge<E> e= (Edge<E>) o;
            return this.refdes.equals(e.refdes);
        }
        return false;
    }


    @Override
    public int compareTo(Edge<E> o) {
        return 0;
    }
}
