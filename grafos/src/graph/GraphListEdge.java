package graph;

import ImpQueue.Queue;
import ImpQueue.QueueLink;
import ListLinked.ListaEnlazada;

public class GraphListEdge<V extends Comparable<V>, E extends Comparable<E>> {
    /** Lista de todos los vértices del grafo */
    private ListaEnlazada<VertexObj<V, E>> secVertex;
    
    /** Lista de todas las aristas del grafo */
    private ListaEnlazada<EdgeObj<V, E>> secEdge;
    
    /** Indica si el grafo es dirigido (true) o no dirigido (false) */
    private boolean isDirected;
    
    /**
     * Constructor por defecto que crea un grafo no dirigido vacío.
     */
    public GraphListEdge() {
        this(false); // Por defecto no dirigido
    }

    /**
     * Constructor que permite especificar si el grafo es dirigido o no.
     * 
     * @param isDirected true para grafo dirigido, false para no dirigido
     */
    public GraphListEdge(boolean isDirected) {
        this.secVertex = new ListaEnlazada<>();
        this.secEdge = new ListaEnlazada<>();
        this.isDirected = isDirected;
    }
    
    /**
     * Inserta un nuevo vértice en el grafo.
     * 
     * @param v El valor del vértice a insertar
     * @return true si se insertó correctamente, false si ya existía o v es null
     */
    public boolean insertVertex(V v) {
        if (v == null) {
            System.out.println("Error: No se puede insertar un vértice null");
            return false;
        }
        
        if (!searchVertex(v)) {
            secVertex.insertLast(new VertexObj<>(v, secVertex.length()));
            System.out.println("Vértice '" + v + "' insertado correctamente");
            return true;
        }
        
        System.out.println("Advertencia: El vértice '" + v + "' ya existe");
        return false;
    }
    
    /**
     * Inserta una arista sin peso entre dos vértices.
     * 
     * @param v1 Vértice origen
     * @param v2 Vértice destino
     * @return true si se insertó correctamente, false en caso contrario
     */
    public boolean insertEdge(V v1, V v2) {
        return insertEdgeWeight(v1, v2, null);
    }
    
    /**
     * Inserta una arista con peso entre dos vértices.
     * En grafos no dirigidos, la arista es bidireccional.
     * En grafos dirigidos, va de v1 hacia v2.
     * 
     * @param v1 Vértice origen
     * @param v2 Vértice destino  
     * @param weight Peso de la arista (puede ser null)
     * @return true si se insertó correctamente, false en caso contrario
     */
    public boolean insertEdgeWeight(V v1, V v2, E weight) {
        if (v1 == null || v2 == null) {
            System.out.println("Error: Los vértices no pueden ser null");
            return false;
        }
        
        VertexObj<V, E> vert1 = getVertex(v1);
        VertexObj<V, E> vert2 = getVertex(v2);

        if (vert1 == null || vert2 == null) {
            System.out.println("Error: Uno o ambos vértices no existen en el grafo");
            return false;
        }
        
        if (searchEdge(v1, v2)) {
            System.out.println("Advertencia: La arista entre '" + v1 + "' y '" + v2 + "' ya existe");
            return false;
        }

        secEdge.insertLast(new EdgeObj<>(vert1, vert2, weight, secEdge.length()));
        String direction = isDirected ? " -> " : " - ";
        System.out.println("Arista '" + v1 + direction + v2 + "' insertada correctamente");
        return true;
    }
    
    /**
     * Verifica si un vértice existe en el grafo.
     * 
     * @param v El valor del vértice a buscar
     * @return true si el vértice existe, false en caso contrario
     */
    public boolean searchVertex(V v) {
        return getVertex(v) != null;
    }

    /**
     * Verifica si existe una arista entre dos vértices.
     * En grafos no dirigidos, busca en ambas direcciones.
     * En grafos dirigidos, solo busca de v1 hacia v2.
     * 
     * @param v1 Vértice origen
     * @param v2 Vértice destino
     * @return true si la arista existe, false en caso contrario
     */
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
    
    /**
     * Elimina una arista entre dos vértices.
     * 
     * @param v1 Vértice origen
     * @param v2 Vértice destino
     * @return true si se eliminó correctamente, false si no existía
     */
    public boolean removeEdge(V v1, V v2) {
        if (v1 == null || v2 == null) {
            System.out.println("Error: Los vértices no pueden ser null");
            return false;
        }
        
        VertexObj<V, E> vert1 = getVertex(v1);
        VertexObj<V, E> vert2 = getVertex(v2);

        if (vert1 == null || vert2 == null) {
            System.out.println("Error: Uno o ambos vértices no existen");
            return false;
        }

        for (EdgeObj<V, E> edge : secEdge) {
            boolean shouldRemove = false;
            
            if (isDirected) {
                // En grafo dirigido, solo v1->v2
                shouldRemove = edge.getEndVertex1().equals(vert1) && edge.getEndVertex2().equals(vert2);
            } else {
                // En grafo no dirigido, cualquier dirección
                shouldRemove = (edge.getEndVertex1().equals(vert1) && edge.getEndVertex2().equals(vert2)) ||
                              (edge.getEndVertex1().equals(vert2) && edge.getEndVertex2().equals(vert1));
            }
            
            if (shouldRemove) {
                boolean removed = secEdge.remove(edge);
                if (removed) {
                    String direction = isDirected ? " -> " : " - ";
                    System.out.println("Arista '" + v1 + direction + v2 + "' eliminada correctamente");
                }
                return removed;
            }
        }
        
        System.out.println("Advertencia: No se encontró la arista entre '" + v1 + "' y '" + v2 + "'");
        return false;
    }

    /**
     * Elimina un vértice y todas las aristas que lo involucran.
     * Este método mantiene la integridad del grafo eliminando automáticamente
     * todas las conexiones del vértice.
     * 
     * @param v El valor del vértice a eliminar
     * @return true si se eliminó correctamente, false si no existía
     */
    public boolean removeVertex(V v) {
        if (v == null) {
            System.out.println("Error: No se puede eliminar un vértice null");
            return false;
        }
        
        VertexObj<V, E> vertex = getVertex(v);
        if (vertex == null) {
            System.out.println("Advertencia: El vértice '" + v + "' no existe");
            return false;
        }

        // Recopilar todas las aristas que involucran este vértice
        ListaEnlazada<EdgeObj<V, E>> edgesToRemove = new ListaEnlazada<>();
        
        for (EdgeObj<V, E> edge : secEdge) {
            if (edge.getEndVertex1().equals(vertex) || edge.getEndVertex2().equals(vertex)) {
                edgesToRemove.insertLast(edge);
            }
        }

        // Eliminar las aristas encontradas
        int edgesRemoved = 0;
        for (EdgeObj<V, E> edge : edgesToRemove) {
            if (secEdge.remove(edge)) {
                edgesRemoved++;
            }
        }

        // Eliminar el vértice
        boolean vertexRemoved = secVertex.remove(vertex);
        
        if (vertexRemoved) {
            System.out.println("Vértice '" + v + "' eliminado correctamente (junto con " + 
                             edgesRemoved + " arista(s))");
        }
        
        return vertexRemoved;
    }
        
    /**
     * Realiza un recorrido en anchura (BFS) desde un vértice dado.
     * BFS explora nivel por nivel, visitando primero todos los vecinos
     * directos antes de pasar al siguiente nivel.
     * 
     * Complejidad: O(V + E) donde V = vértices, E = aristas
     * 
     * @param startVertex Vértice desde donde comenzar el recorrido
     * @return true si el recorrido se completó exitosamente
     */
    public boolean bfs(V startVertex) {
        if (startVertex == null) {
            System.out.println("Error: El vértice de inicio no puede ser null");
            return false;
        }
        
        VertexObj<V, E> start = getVertex(startVertex);
        if (start == null) {
            System.out.println("Error: El vértice de inicio '" + startVertex + "' no existe");
            return false;
        }

        // Usamos listas para tracking de visitados y cola para BFS
        ListaEnlazada<VertexObj<V, E>> visited = new ListaEnlazada<>();
        Queue<VertexObj<V, E>> queue = new QueueLink<>();

        try {
            // Inicializar: marcar el vértice inicial como visitado y agregarlo a la cola
            queue.enqueue(start);
            visited.insertLast(start);

            System.out.print("Recorrido BFS desde '" + startVertex + "': ");

            while (!queue.isEmpty()) {
                // Extraer el siguiente vértice de la cola
                VertexObj<V, E> current = queue.dequeue();
                System.out.print(current.getInfo() + " ");

                // Obtener todos los vecinos del vértice actual
                ListaEnlazada<VertexObj<V, E>> neighbors = getNeighbors(current);
                
                for (VertexObj<V, E> neighbor : neighbors) {
                    // Si el vecino no ha sido visitado, marcarlo y agregarlo a la cola
                    if (!isVertexInList(neighbor, visited)) {
                        visited.insertLast(neighbor);
                        queue.enqueue(neighbor);
                    }
                }
            }
            System.out.println(); // Nueva línea al final
            return true;
            
        } catch (Exception e) {
            System.out.println("\nError durante BFS: " + e.getMessage());
            return false;
        }
    }

    /**
     * Realiza un recorrido en profundidad (DFS) desde un vértice dado.
     * DFS explora tan profundo como sea posible antes de retroceder.
     * 
     * Complejidad: O(V + E) donde V = vértices, E = aristas
     * 
     * @param startVertex Vértice desde donde comenzar el recorrido
     * @return true si el recorrido se completó exitosamente
     */
    public boolean dfs(V startVertex) {
        if (startVertex == null) {
            System.out.println("Error: El vértice de inicio no puede ser null");
            return false;
        }
        
        VertexObj<V, E> start = getVertex(startVertex);
        if (start == null) {
            System.out.println("Error: El vértice de inicio '" + startVertex + "' no existe");
            return false;
        }

        ListaEnlazada<VertexObj<V, E>> visited = new ListaEnlazada<>();
        System.out.print("Recorrido DFS desde '" + startVertex + "': ");
        
        try {
            dfsRecursive(start, visited);
            System.out.println(); // Nueva línea al final
            return true;
        } catch (Exception e) {
            System.out.println("\nError durante DFS: " + e.getMessage());
            return false;
        }
    }

    /**
     * Método auxiliar recursivo para DFS.
     * Implementa la lógica recursiva del recorrido en profundidad.
     * 
     * @param current Vértice actual que se está visitando
     * @param visited Lista de vértices ya visitados
     */
    private void dfsRecursive(VertexObj<V, E> current, ListaEnlazada<VertexObj<V, E>> visited) {
        // Marcar el vértice actual como visitado y procesarlo
        visited.insertLast(current);
        System.out.print(current.getInfo() + " ");

        // Obtener todos los vecinos del vértice actual
        ListaEnlazada<VertexObj<V, E>> neighbors = getNeighbors(current);
        
        // Para cada vecino no visitado, hacer llamada recursiva
        for (VertexObj<V, E> neighbor : neighbors) {
            if (!isVertexInList(neighbor, visited)) {
                dfsRecursive(neighbor, visited);
            }
        }
    }
        
    /**
     * Obtiene el número de vértices en el grafo.
     * 
     * @return Cantidad de vértices
     */
    public int getVertexCount() {
        return secVertex.length();
    }
    
    /**
     * Obtiene el número de aristas en el grafo.
     * 
     * @return Cantidad de aristas
     */
    public int getEdgeCount() {
        return secEdge.length();
    }
    
    /**
     * Verifica si el grafo es dirigido.
     * 
     * @return true si es dirigido, false si no es dirigido
     */
    public boolean isDirected() {
        return isDirected;
    }
    
    /**
     * Muestra una representación del grafo en consola.
     * Útil para debugging y visualización.
     */
    public void printGraph() {
        System.out.println("\n=== INFORMACIÓN DEL GRAFO ===");
        System.out.println("Tipo: " + (isDirected ? "Dirigido" : "No dirigido"));
        System.out.println("Vértices: " + getVertexCount());
        System.out.println("Aristas: " + getEdgeCount());
        
        System.out.println("\nVértices:");
        for (VertexObj<V, E> vertex : secVertex) {
            System.out.println("  - " + vertex.getInfo());
        }
        
        System.out.println("\nAristas:");
        for (EdgeObj<V, E> edge : secEdge) {
            String direction = isDirected ? " -> " : " - ";
            String weight = edge.getWeight() != null ? " (peso: " + edge.getWeight() + ")" : "";
            System.out.println("  - " + edge.getEndVertex1().getInfo() + 
                             direction + edge.getEndVertex2().getInfo() + weight);
        }
        System.out.println("===============================\n");
    }

    /**
     * Obtiene el objeto vértice correspondiente a un valor dado.
     * Método interno para la búsqueda eficiente de vértices.
     * 
     * @param data El valor del vértice a buscar
     * @return El objeto VertexObj correspondiente, o null si no existe
     */
    private VertexObj<V, E> getVertex(V data) {
        if (data == null) return null;
        
        for (VertexObj<V, E> v : secVertex) {
            if (v.getInfo().equals(data)) {
                return v;
            }
        }
        return null;
    }

    /**
     * Obtiene todos los vértices vecinos (adyacentes) de un vértice dado.
     * En grafos dirigidos, solo considera las aristas salientes.
     * En grafos no dirigidos, considera conexiones en ambas direcciones.
     * 
     * @param vertex El vértice del cual obtener los vecinos
     * @return Lista de vértices vecinos
     */
    private ListaEnlazada<VertexObj<V, E>> getNeighbors(VertexObj<V, E> vertex) {
        ListaEnlazada<VertexObj<V, E>> neighbors = new ListaEnlazada<>();
        
        for (EdgeObj<V, E> edge : secEdge) {
            if (isDirected) {
                // En grafo dirigido, solo considerar aristas salientes
                if (edge.getEndVertex1().equals(vertex)) {
                    neighbors.insertLast(edge.getEndVertex2());
                }
            } else {
                // En grafo no dirigido, considerar ambas direcciones
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
     * Verifica si un vértice está presente en una lista de vértices.
     * Método auxiliar para reemplazar el contains() que podría no existir.
     * 
     * @param vertex El vértice a buscar
     * @param list La lista donde buscar
     * @return true si el vértice está en la lista, false en caso contrario
     */
    private boolean isVertexInList(VertexObj<V, E> vertex, ListaEnlazada<VertexObj<V, E>> list) {
        for (VertexObj<V, E> v : list) {
            if (v.equals(vertex)) {
                return true;
            }
        }
        return false;
    }
}
