package graph;

import ImpQueue.Queue;
import ImpQueue.QueueLink;
import ListLinked.ListaEnlazada;
/**
 * Soporta tanto grafos dirigidos como no dirigidos.
 * 
 * @param <V> Tipo de dato almacenado en los vértices (debe ser comparable)
 * @param <E> Tipo de dato para el peso de las aristas (debe ser comparable)
 */
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

        EdgeObj<V, E> edgeToRemove = null;
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
                edgeToRemove = edge;
                break;
            }
        }
        
        if (edgeToRemove != null) {
            boolean removed = secEdge.remove(edgeToRemove);
            if (removed) {
                String direction = isDirected ? " -> " : " - ";
                System.out.println("Arista '" + v1 + direction + v2 + "' eliminada correctamente");
            }
            return removed;
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
     * Obtiene el grado de un nodo específico.
     * 
     * @param v El valor del vértice
     * @return Grado del vértice, -1 si no existe
     */
    public int getNodeDegree(V v) {
        VertexObj<V, E> vertex = getVertex(v);
        if (vertex == null) return -1;

        int degree = 0;
        for (EdgeObj<V, E> edge : secEdge) {
            if (edge.getEndVertex1().equals(vertex) || edge.getEndVertex2().equals(vertex)) {
                degree++;
            }
        }
        return degree;
    }

    /**
     * Verifica si el grafo es de tipo CAMINO (Px).
     * Un camino tiene exactamente 2 nodos de grado 1 y n-2 nodos de grado 2.
     * 
     * @return true si es un camino, false en caso contrario
     */
    public boolean isDirectedPath() {
        if (!isDirected || secVertex.length() < 2) return false;

        int nodesWithDegree1 = 0;  // Contador de nodos con grado 1
        int nodesWithDegree2 = 0;  // Contador de nodos con grado 2

        for (VertexObj<V, E> vertex : secVertex) {
            int degree = getNodeDegree(vertex.getInfo());
            if (degree == 1) {
                nodesWithDegree1++;
            } else if (degree == 2) {
                nodesWithDegree2++;
            } else {
                return false; // Si hay nodos con grado diferente a 1 o 2, no es camino
            }
        }

        return (nodesWithDegree1 == 2) && (nodesWithDegree2 == (secVertex.length() - 2));
    }

    /**
     * Verifica si el grafo es de tipo CICLO (Cx).
     * Un ciclo tiene todos los nodos con grado exactamente 2.
     * 
     * @return true si es un ciclo, false en caso contrario
     */
    public boolean isDirectedCycle() {
        if (!isDirected || secVertex.length() < 3) return false;

        for (VertexObj<V, E> vertex : secVertex) {
            if (getNodeDegree(vertex.getInfo()) != 2) {
                return false; // Si algún nodo no tiene grado 2, no es un ciclo
            }
        }

        return true;
    }

    /**
     * Verifica si el grafo es de tipo RUEDA (Wx).
     * Una rueda tiene un nodo central de grado (n-1) y (n-1) nodos de grado 3.
     * 
     * @return true si es una rueda, false en caso contrario
     */
    public boolean isDirectedWheel() {
        if (!isDirected || secVertex.length() < 4) return false;

        int n = secVertex.length();
        int nodesWithDegreeN1 = 0; // Nodos con grado n-1 (centro)
        int nodesWithDegree3 = 0;  // Nodos con grado 3 (periferia)

        for (VertexObj<V, E> vertex : secVertex) {
            int degree = getNodeDegree(vertex.getInfo());
            if (degree == n - 1) {
                nodesWithDegreeN1++;
            } else if (degree == 3) {
                nodesWithDegree3++;
            } else {
                return false; // Grado inválido para rueda
            }
        }

        return (nodesWithDegreeN1 == 1) && (nodesWithDegree3 == n - 1);
    }                 
    /**
     * @param vertices Lista enlazada con vértices
     * @param edges Lista enlazada con aristas (EdgeObj con vértices y peso)
     */
    public void defineFromFormal(ListaEnlazada<V> vertices, ListaEnlazada<EdgeObj<V, E>> edges) {
        this.secVertex = new ListaEnlazada<>();
        this.secEdge = new ListaEnlazada<>();
        this.isDirected = false;

        for (V v : vertices) {
            this.insertVertex(v);
        }

        for (EdgeObj<V, E> e : edges) {
            V v1 = e.getEndVertex1().getInfo();
            V v2 = e.getEndVertex2().getInfo();
            E w = e.getWeight();
            this.insertEdgeWeight(v1, v2, w);
        }
    }

    /**
     * La matriz debe ser cuadrada y simétrica.
     * @param vertexOrder Array con orden de vértices (filas y columnas)
     * @param adjacencyMatrix Matriz bidimensional con pesos o null
     */
    public void defineFromAdjacencyMatrix(V[] vertexOrder, E[][] adjacencyMatrix) {
        this.secVertex = new ListaEnlazada<>();
        this.secEdge = new ListaEnlazada<>();
        this.isDirected = false;

        int n = vertexOrder.length;
        if (adjacencyMatrix.length != n) {
            throw new IllegalArgumentException("Matriz no cuadrada");
        }
        for (E[] row : adjacencyMatrix) {
            if (row.length != n) {
                throw new IllegalArgumentException("Matriz no cuadrada");
            }
        }

        for (V v : vertexOrder) {
            this.insertVertex(v);
        }

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                E w = adjacencyMatrix[i][j];
                if (w != null && !w.equals(zeroEquivalent(w))) {
                    this.insertEdgeWeight(vertexOrder[i], vertexOrder[j], w);
                }
            }
        }
    }

    /**
     * @param vertices Lista enlazada de VertexObj
     * @param edges Lista enlazada de EdgeObj
     */
    public void defineFromAdjacencyLists(ListaEnlazada<VertexObj<V,E>> vertices, ListaEnlazada<EdgeObj<V,E>> edges) {
        this.secVertex = new ListaEnlazada<>();
        this.secEdge = new ListaEnlazada<>();
        this.isDirected = false;

        for (VertexObj<V,E> v : vertices) {
            this.insertVertex(v.getInfo());
        }

        for (EdgeObj<V,E> edge : edges) {
            this.insertEdgeWeight(
                edge.getEndVertex1().getInfo(),
                edge.getEndVertex2().getInfo(),
                edge.getWeight()
            );
        }
    }

    /**
     * @param example Valor ejemplo para tipo de peso
     * @return cero o null equivalente
     */
    @SuppressWarnings("unchecked")
    private E zeroEquivalent(E example) {
        if (example == null) return null;
        if (example instanceof Number) {
            return (E) Integer.valueOf(0);
        }
        return null;
    }

    /**
     * Verifica si dos grafos son isomorfos, usando cantidad de vértices, aristas y secuencia de grados.
     */
    public boolean isIsomorphic(GraphListEdge<V, E> otherGraph) {
        if (this.getVertexCount() != otherGraph.getVertexCount() ||
            this.getEdgeCount() != otherGraph.getEdgeCount() ||
            this.isDirected() != otherGraph.isDirected()) {
            System.out.println("Los grafos no son isomorfos: diferencias en vértices, aristas o tipo.");
            return false;
        }

        ListaEnlazada<Integer> thisDegrees = new ListaEnlazada<>();
        for (VertexObj<V, E> v : this.secVertex) {
            thisDegrees.insertLast(this.getNodeDegree(v.getInfo()));
        }
        thisDegrees.contains();

        ListaEnlazada<Integer> otherDegrees = new ListaEnlazada<>();
        for (VertexObj<V, E> v : otherGraph.secVertex) {
            otherDegrees.insertLast(otherGraph.getNodeDegree(v.getInfo()));
        }
        otherDegrees.contains();

        if (!thisDegrees.equals(otherDegrees)) { // asume equals compara contenido
            System.out.println("Los grafos no son isomorfos: secuencia de grados diferente.");
            return false;
        }
        System.out.println("Los grafos SON isomorfos (básico).");
        return true; // Para simplificar, asumimos isomorfismo si secuencia coincide
    }

    /**
     * Verifica planitud básica con regla de Euler y cantidad de vértices/aristas.
     */
    public boolean isPlanar() {
        int v = getVertexCount();
        int e = getEdgeCount();
        if (v <= 4) return true;
        if (e > 3 * v - 6) {
            System.out.println("No es plano: más de 3v-6 aristas.");
            return false;
        }
        System.out.println("Es plano por condición básica de Euler.");
        return true;
    }

    /**
     * Verifica conectividad usando BFS ignorando dirección.
     */
    public boolean isConnected() {
        if (secVertex.length() == 0) return true;
        VertexObj<V, E> start = secVertex.getClass(0);
        ListaEnlazada<VertexObj<V, E>> visited = new ListaEnlazada<>();
        Queue<VertexObj<V, E>> queue = new QueueLink<>();
        queue.enqueue(start);
        visited.insertLast(start);
        while (!queue.isEmpty()) {
            VertexObj<V, E> current = queue.dequeue();
            ListaEnlazada<VertexObj<V, E>> neighbors = getNeighbors(current);
            for (VertexObj<V, E> neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    visited.insertLast(neighbor);
                    queue.enqueue(neighbor);
                }
            }
        }
        return visited.length() == secVertex.length();
    }

    /**
     * Verifica si el grafo es auto-complementario básicos con isomorfismo y condición n mod 4.
     */
    public boolean isSelfComplementary() {
        int n = getVertexCount();
        if (n % 4 != 0 && n % 4 != 1) {
            System.out.println("No es auto-complementario: n mod 4 distinto de 0 o 1.");
            return false;
        }
        GraphListEdge<V, E> complement = createComplement();
        return this.isIsomorphic(complement);
    }

    // Crea complemento básico (sin peso) para isSelfComplementary
    private GraphListEdge<V, E> createComplement() {
        GraphListEdge<V, E> complement = new GraphListEdge<>(this.isDirected());
        for (VertexObj<V, E> v : this.secVertex)
            complement.insertVertex(v.getInfo());
        for (VertexObj<V, E> v1 : this.secVertex) {
            for (VertexObj<V, E> v2 : this.secVertex) {
                if (!v1.equals(v2) && !this.searchEdge(v1.getInfo(), v2.getInfo())) {
                    if (!complement.searchEdge(v1.getInfo(), v2.getInfo())) {
                        complement.insertEdge(v1.getInfo(), v2.getInfo());
                    }
                }
            }
        }
        return complement;
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
