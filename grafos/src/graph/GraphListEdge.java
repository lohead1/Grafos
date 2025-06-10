package graph;

import ClassAux.ParDistancia;
import ClassAux.ParPadre;
import ImpQueue.Queue;
import ImpQueue.QueueLink;
import ImpStack.StackLink;
import ListLinked.ListaEnlazada;

public class GraphListEdge<V extends Comparable<V>, E extends Comparable<E>> {
    private ListaEnlazada<VertexObj<V, E>> secVertex;
    private ListaEnlazada<EdgeObj<V, E>> secEdge;
    private boolean isDirected;

    // Constructores
    public GraphListEdge() {
        this(false); // Por defecto no dirigido
    }

    public GraphListEdge(boolean isDirected) {
        this.secVertex = new ListaEnlazada<>();
        this.secEdge = new ListaEnlazada<>();
        this.isDirected = isDirected;
    }

    public boolean insertVertex(V v) {
        if (v == null) return false;
        
        if (!searchVertex(v)) {
            secVertex.insertLast(new VertexObj<>(v, secVertex.length()));
            return true;
        }
        return false; // Ya existe
    }
    public boolean insertEdge(V v1, V v2) {
        return insertEdgeWeight(v1, v2, null);
    }
    public boolean insertEdgeWeight(V v1, V v2, E weight) {
        if (v1 == null || v2 == null) return false;
        
        VertexObj<V, E> vert1 = getVertex(v1);
        VertexObj<V, E> vert2 = getVertex(v2);

        if (vert1 == null || vert2 == null) {
            System.out.println("Uno o ambos vértices no existen");
            return false;
        }
        
        if (searchEdge(v1, v2)) {
            System.out.println("La arista ya existe");
            return false;
        }

        secEdge.insertLast(new EdgeObj<>(vert1, vert2, weight, secEdge.length()));
        return true;
    }

    public boolean searchVertex(V v) {
        return getVertex(v) != null;
    }

    public boolean searchEdge(V v1, V v2) {
        if (v1 == null || v2 == null) return false;
        
        VertexObj<V, E> vert1 = getVertex(v1);
        VertexObj<V, E> vert2 = getVertex(v2);

        if (vert1 == null || vert2 == null) return false;

        for (EdgeObj<V, E> edge : secEdge) {
            if (isDirected) {
                // En grafo dirigido, solo v1->v2
                if (edge.getEndVertex1().equals(vert1) && edge.getEndVertex2().equals(vert2)) {
                    return true;
                }
            } else {
                // En grafo no dirigido, v1-v2 o v2-v1
                if ((edge.getEndVertex1().equals(vert1) && edge.getEndVertex2().equals(vert2)) ||
                    (edge.getEndVertex1().equals(vert2) && edge.getEndVertex2().equals(vert1))) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean removeEdge(V v1, V v2) {
        if (v1 == null || v2 == null) return false;
        
        VertexObj<V, E> vert1 = getVertex(v1);
        VertexObj<V, E> vert2 = getVertex(v2);

        if (vert1 == null || vert2 == null) {
            System.out.println("Uno o ambos vértices no existen");
            return false;
        }

        for (EdgeObj<V, E> edge : secEdge) {
            if (isDirected) {
                if (edge.getEndVertex1().equals(vert1) && edge.getEndVertex2().equals(vert2)) {
                    return secEdge.remove(edge);
                }
            } else {
                if ((edge.getEndVertex1().equals(vert1) && edge.getEndVertex2().equals(vert2)) ||
                    (edge.getEndVertex1().equals(vert2) && edge.getEndVertex2().equals(vert1))) {
                    return secEdge.remove(edge);
                }
            }
        }
        return false;
    }

    public boolean removeVertex(V v) {
        if (v == null) return false;
        
        VertexObj<V, E> vertex = getVertex(v);
        if (vertex == null) return false;

        // Eliminar todas las aristas que involucran este vértice
        ListaEnlazada<EdgeObj<V, E>> edgesToRemove = new ListaEnlazada<>();
        
        for (EdgeObj<V, E> edge : secEdge) {
            if (edge.getEndVertex1().equals(vertex) || edge.getEndVertex2().equals(vertex)) {
                edgesToRemove.insertLast(edge);
            }
        }

        // Eliminar las aristas encontradas
        for (EdgeObj<V, E> edge : edgesToRemove) {
            secEdge.remove(edge);
        }

        // Eliminar el vértice
        return secVertex.remove(vertex);
    }
    /**
     * BFS 
     */
    public boolean bfs(V startVertex) {
        if (startVertex == null) return false;
        
        VertexObj<V, E> start = getVertex(startVertex);
        if (start == null) {
            System.out.println("El vértice de inicio no existe");
            return false;
        }

        ListaEnlazada<VertexObj<V, E>> visited = new ListaEnlazada<>();
        Queue<VertexObj<V, E>> queue = new QueueLink<>();

        try {
            queue.enqueue(start);
            visited.insertLast(start);

            System.out.print("Recorrido BFS desde " + startVertex + ": ");

            while (!queue.isEmpty()) {
                VertexObj<V, E> current = queue.dequeue();
                System.out.print(current.getInfo() + " ");

                // Obtener vecinos
                ListaEnlazada<VertexObj<V, E>> neighbors = getNeighbors(current);
                for (VertexObj<V, E> neighbor : neighbors) {
                    if (!visited.contains(neighbor)) {
                        visited.insertLast(neighbor);
                        queue.enqueue(neighbor);
                    }
                }
            }
            System.out.println();
            return true;
            
        } catch (Exception e) {
            System.out.println("Error durante BFS: " + e.getMessage());
            return false;
        }
    }

    /**
     * DFS
     */
    public boolean dfs(V startVertex) {
        if (startVertex == null) return false;
        
        VertexObj<V, E> start = getVertex(startVertex);
        if (start == null) {
            System.out.println("El vértice de inicio no existe");
            return false;
        }

        ListaEnlazada<VertexObj<V, E>> visited = new ListaEnlazada<>();
        System.out.print("Recorrido DFS desde " + startVertex + ": ");
        
        try {
            dfsRecursive(start, visited);
            System.out.println();
            return true;
        } catch (Exception e) {
            System.out.println("Error durante DFS: " + e.getMessage());
            return false;
        }
    }

    private void dfsRecursive(VertexObj<V, E> current, ListaEnlazada<VertexObj<V, E>> visited) {
        visited.insertLast(current);
        System.out.print(current.getInfo() + " ");

        ListaEnlazada<VertexObj<V, E>> neighbors = getNeighbors(current);
        for (VertexObj<V, E> neighbor : neighbors) {
            if (!visited.contains(neighbor)) {
                dfsRecursive(neighbor, visited);
            }
        }
    }

    /**
     * encontrar camino más corto (menor número de aristas) usando BFS
     */
    public ListaEnlazada<V> shortestPath(V origen, V destino) {
        if (origen == null || destino == null) return new ListaEnlazada<>();
        
        VertexObj<V, E> verOrigen = getVertex(origen);
        VertexObj<V, E> verDestino = getVertex(destino);
        
        if (verOrigen == null || verDestino == null) {
            return new ListaEnlazada<>();
        }

        ListaEnlazada<VertexObj<V, E>> visited = new ListaEnlazada<>();
        Queue<VertexObj<V, E>> queue = new QueueLink<>();
        ListaEnlazada<ParPadre<V>> padres = new ListaEnlazada<>();

        try {
            queue.enqueue(verOrigen);
            visited.insertLast(verOrigen);
            padres.insertLast(new ParPadre<>(origen, null));

            boolean encontrado = false;

            while (!queue.isEmpty() && !encontrado) {
                VertexObj<V, E> current = queue.dequeue();

                if (current.getInfo().equals(destino)) {
                    encontrado = true;
                    break;
                }

                ListaEnlazada<VertexObj<V, E>> neighbors = getNeighbors(current);
                for (VertexObj<V, E> neighbor : neighbors) {
                    if (!visited.contains(neighbor)) {
                        queue.enqueue(neighbor);
                        visited.insertLast(neighbor);
                        padres.insertLast(new ParPadre<>(neighbor.getInfo(), current.getInfo()));
                    }
                }
            }

            // Reconstruir camino
            ListaEnlazada<V> camino = new ListaEnlazada<>();
            if (encontrado) {
                V actual = destino;
                while (actual != null) {
                    camino.insertFirst(actual);
                    
                    V padre = null;
                    for (ParPadre<V> par : padres) {
                        if (par.getHijo().equals(actual)) {
                            padre = par.getPadre();
                            break;
                        }
                    }
                    actual = padre;
                }
            }
            return camino;
            
        } catch (Exception e) {
            System.out.println("Error calculando camino más corto: " + e.getMessage());
            return new ListaEnlazada<>();
        }
    }

    /**
     * Algoritmo de Dijkstra para camino de menor peso
     * Requiere que E implemente Comparable y que los pesos sean numéricos
     */
    public StackLink<V> dijkstra(V origen, V destino) {
        StackLink<V> result = new StackLink<>();
        
        if (origen == null || destino == null) return result;
        
        VertexObj<V, E> verOrigen = getVertex(origen);
        VertexObj<V, E> verDestino = getVertex(destino);
        
        if (verOrigen == null || verDestino == null) return result;

        try {
            // Implementación simplificada asumiendo que E es Integer o compatible
            ListaEnlazada<ParDistancia<V>> distancias = new ListaEnlazada<>();
            ListaEnlazada<VertexObj<V, E>> visitados = new ListaEnlazada<>();
            
            // Inicializar distancias
            for (VertexObj<V, E> v : secVertex) {
                if (v.equals(verOrigen)) {
                    distancias.insertLast(new ParDistancia<>(v.getInfo(), 0, null));
                } else {
                    distancias.insertLast(new ParDistancia<>(v.getInfo(), Integer.MAX_VALUE, null));
                }
            }

            // Proceso principal de Dijkstra
            while (visitados.length() < secVertex.length()) {
                VertexObj<V, E> actual = getMinDistanceVertex(distancias, visitados);
                if (actual == null) break;
                
                visitados.insertLast(actual);
                
                if (actual.equals(verDestino)) break;

                updateDistances(actual, distancias, visitados);
            }

            // Reconstruir camino
            reconstructPath(origen, destino, distancias, result);
            
        } catch (Exception e) {
            System.out.println("Error en Dijkstra: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * Obtener objeto vértice por valor
     */
    private VertexObj<V, E> getVertex(V data) {
        if (data == null) return null;
        
        for (VertexObj<V, E> v : secVertex) {
            if (v.getInfo().equals(data)) return v;
        }
        return null;
    }

    /**
     * Obtener vecinos de un vértice
     */
    private ListaEnlazada<VertexObj<V, E>> getNeighbors(VertexObj<V, E> vertex) {
        ListaEnlazada<VertexObj<V, E>> neighbors = new ListaEnlazada<>();
        
        for (EdgeObj<V, E> edge : secEdge) {
            if (isDirected) {
                // En grafo dirigido, solo salidas
                if (edge.getEndVertex1().equals(vertex)) {
                    neighbors.insertLast(edge.getEndVertex2());
                }
            } else {
                // En grafo no dirigido, ambas direcciones
                if (edge.getEndVertex1().equals(vertex)) {
                    neighbors.insertLast(edge.getEndVertex2());
                } else if (edge.getEndVertex2().equals(vertex)) {
                    neighbors.insertLast(edge.getEndVertex1());
                }
            }
        }
        return neighbors;
    }

    /**
     * Métodos auxiliares para Dijkstra
     */
    private VertexObj<V, E> getMinDistanceVertex(ListaEnlazada<ParDistancia<V>> distancias, 
                                                 ListaEnlazada<VertexObj<V, E>> visitados) {
        int minDist = Integer.MAX_VALUE;
        VertexObj<V, E> minVertex = null;
        
        for (ParDistancia<V> pd : distancias) {
            VertexObj<V, E> vertex = getVertex(pd.getVertice());
            if (vertex != null && !visitados.contains(vertex) && pd.getDistancia() < minDist) {
                minDist = pd.getDistancia();
                minVertex = vertex;
            }
        }
        return minVertex;
    }

    private void updateDistances(VertexObj<V, E> actual, ListaEnlazada<ParDistancia<V>> distancias, 
                                ListaEnlazada<VertexObj<V, E>> visitados) {
        int distActual = 0;
        for (ParDistancia<V> pd : distancias) {
            if (pd.getVertice().equals(actual.getInfo())) {
                distActual = pd.getDistancia();
                break;
            }
        }

        ListaEnlazada<VertexObj<V, E>> neighbors = getNeighbors(actual);
        for (VertexObj<V, E> neighbor : neighbors) {
            if (visitados.contains(neighbor)) continue;
            
            int peso = getEdgeWeight(actual, neighbor);
            int nuevaDist = distActual + peso;
            
            for (ParDistancia<V> pd : distancias) {
                if (pd.getVertice().equals(neighbor.getInfo()) && nuevaDist < pd.getDistancia()) {
                    pd.setDistancia(nuevaDist);
                    pd.setPadre(actual.getInfo());
                    break;
                }
            }
        }
    }

    private int getEdgeWeight(VertexObj<V, E> v1, VertexObj<V, E> v2) {
        for (EdgeObj<V, E> edge : secEdge) {
            boolean match = false;
            if (isDirected) {
                match = edge.getEndVertex1().equals(v1) && edge.getEndVertex2().equals(v2);
            } else {
                match = (edge.getEndVertex1().equals(v1) && edge.getEndVertex2().equals(v2)) ||
                       (edge.getEndVertex1().equals(v2) && edge.getEndVertex2().equals(v1));
            }
            
            if (match) {
                E weight = edge.getWeight();
                if (weight instanceof Integer) {
                    return (Integer) weight;
                }
                return 1; // Peso por defecto
            }
        }
        return 1; // Peso por defecto
    }

    private void reconstructPath(V origen, V destino, ListaEnlazada<ParDistancia<V>> distancias, 
                                StackLink<V> result) {
        boolean existeCamino = false;
        for (ParDistancia<V> pd : distancias) {
            if (pd.getVertice().equals(destino) && pd.getDistancia() != Integer.MAX_VALUE) {
                existeCamino = true;
                break;
            }
        }

        if (existeCamino) {
            V actual = destino;
            while (actual != null) {
                result.push(actual);
                
                V padre = null;
                for (ParDistancia<V> pd : distancias) {
                    if (pd.getVertice().equals(actual)) {
                        padre = pd.getPadre();
                        break;
                    }
                }
                actual = padre;
            }
        }
    }
}
