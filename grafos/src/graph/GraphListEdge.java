package graph;

import java.util.HashSet;
import java.util.Set;

import ImpQueue.Queue;
import ImpQueue.QueueLink;
import ListLinked.ListaEnlazada;

public class GraphListEdge<V,E>{
	ListaEnlazada<VertexObj<V, E>> secVertex;
    ListaEnlazada<EdgeObj<V, E>> secEdge;

    public GraphListEdge() {
        this.secVertex = new ListaEnlazada<>();
        this.secEdge = new ListaEnlazada<>();
    }

    // a) Insertar vértice
    public void insertVertex(V v) {
        if (!searchVertex(v)) {
            secVertex.insertLast(new VertexObj<>(v, secVertex.length()));
        }
    }

    // b) Insertar arista
    public void insertEdge(V v, V z) {
        VertexObj<V, E> vert1 = getVertex(v);
        VertexObj<V, E> vert2 = getVertex(z);

        if (vert1 == null || vert2 == null) return;
        if (searchEdge(v, z)) return;

        secEdge.insertLast(new EdgeObj<>(vert1, vert2, null, secEdge.length()));
    }

    // c) Buscar vértice
    public boolean searchVertex(V v) {
        return getVertex(v) != null;
    }

    // d) Buscar arista
    public boolean searchEdge(V v, V z) {
        VertexObj<V, E> vert1 = getVertex(v);
        VertexObj<V, E> vert2 = getVertex(z);

        if (vert1 == null || vert2 == null) return false;

        for (VertexObj<V, E> vtx : secVertex) {
            for (EdgeObj<V, E> edge : secEdge) {
                if ((edge.getEndVertex1().equals(vert1) && edge.getEndVertex2().equals(vert2)) ||
                    (edge.getEndVertex1().equals(vert2) && edge.getEndVertex2().equals(vert1))) {
                    return true;
                }
            }
        }
        return false;
    }

    // e) BFS (recorrido en anchura)
    public boolean bfs(V v) {
        VertexObj<V, E> start = getVertex(v);
        if (start == null) return false;

        Set<VertexObj<V, E>> visited = new HashSet<>();
        Queue<VertexObj<V, E>> queue = new QueueLink<>();

        queue.enqueue(start);
        visited.add(start);

        System.out.print("Recorrido BFS desde " + v + ": ");

        while (!queue.isEmpty()) {
            VertexObj<V, E> current = queue.dequeue();
            System.out.print(current.getInfo() + " ");

            for (EdgeObj<V, E> edge : secEdge) {
                VertexObj<V, E> neighbor = null;

                if (edge.getEndVertex1().equals(current)) {
                    neighbor = edge.getEndVertex2();
                } else if (edge.getEndVertex2().equals(current)) {
                    neighbor = edge.getEndVertex1();
                }

                if (neighbor != null && !visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.enqueue(neighbor);
                }
            }
        }

        System.out.println();
        return true;
    }

    // Método auxiliar para buscar un vértice por valor
    private VertexObj<V, E> getVertex(V data) {
        for (VertexObj<V, E> v : secVertex) {
            if (v.getInfo().equals(data)) return v;
        }
        return null;
    }

    // Getters para pruebas o depuración
    public ListaEnlazada<VertexObj<V, E>> getVertices() {
        return secVertex;
    }

    public ListaEnlazada<EdgeObj<V, E>> getEdges() {
        return secEdge;
    }
}
