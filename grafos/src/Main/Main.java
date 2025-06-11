package Main;

import ImpStack.StackLink;
import ListLinked.ListaEnlazada;
import graph.GraphLink;

public class Main {
	public static void main(String[] args) {
        // Crear un grafo no dirigido
        GraphLink<String> grafo = new GraphLink<>(false);

        // Insertar vértices
        grafo.insertVertex("A");
        grafo.insertVertex("B");
        grafo.insertVertex("C");
        grafo.insertVertex("D");
        grafo.insertVertex("E");

        // Insertar aristas con peso
        grafo.insertEdgeWeight("A", "B", 4);
        grafo.insertEdgeWeight("A", "C", 2);
        grafo.insertEdgeWeight("B", "D", 5);
        grafo.insertEdgeWeight("C", "D", 1);
        grafo.insertEdgeWeight("D", "E", 3);

        // Mostrar recorridos
        System.out.println("\n--- DFS ---");
        grafo.dfs("A");

        System.out.println("\n\n--- BFS ---");
        grafo.bfs("A");

        // Ver si el grafo es conexo
        System.out.println("\n¿Es conexo?: " + grafo.isConexo());

        // Mostrar camino más corto (en número de aristas) entre A y E
        System.out.println("\nCamino corto entre A y E (BFS):");
        ListaEnlazada<String> camino = grafo.shortPath("A", "E");
        for (String v : camino) {
            System.out.print(v + " ");
        }

        // Mostrar camino más corto por Dijkstra
        System.out.println("\n\nCamino más corto entre A y E (Dijkstra):");
        StackLink<String> caminoDijkstra = grafo.Dijkstra("A", "E");
        while (!caminoDijkstra.isEmpty()) {
            try {
                System.out.print(caminoDijkstra.pop() + " ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Verificar existencia de arista
        System.out.println("\n\n¿Existe arista entre A y B?: " + grafo.searchEdge("A", "B"));
        System.out.println("¿Existe arista entre A y E?: " + grafo.searchEdge("A", "E"));

        // Eliminar arista y vértice
        grafo.removeEdge("A", "B");
        System.out.println("\nEliminando arista entre A y B...");
        System.out.println("¿Existe arista entre A y B?: " + grafo.searchEdge("A", "B"));

        grafo.removeVertex("C");
        System.out.println("\nEliminando vértice C...");
        System.out.println("¿Existe vértice C?: " + grafo.searchVertex("C"));
    }
}
