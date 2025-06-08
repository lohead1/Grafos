package Main;

import graph.GraphLink;

public class Main {
    public static void main(String[] args) {
    	// Grafo No Dirigido
        System.out.println("---- GRAFO NO DIRIGIDO ----");
        GraphLink<String> grafoNoDirigido = new GraphLink<>(false);

        grafoNoDirigido.insertVertex("A");
        grafoNoDirigido.insertVertex("B");
        grafoNoDirigido.insertVertex("C");

        grafoNoDirigido.insertarEdge("A", "B", 1);
        grafoNoDirigido.insertarEdge("B", "C", 2);

        System.out.println("Grafo inicial:");
        System.out.println(grafoNoDirigido);

        grafoNoDirigido.removeEdge("A", "B");
        System.out.println("Después de eliminar arista A - B:");
        System.out.println(grafoNoDirigido);

        grafoNoDirigido.removeVertex("C");
        System.out.println("Después de eliminar vértice C:");
        System.out.println(grafoNoDirigido);

        System.out.println("DFS desde A:");
        grafoNoDirigido.dfs("A");

        // Grafo Dirigido
        System.out.println("\n---- GRAFO DIRIGIDO ----");
        GraphLink<String> grafoDirigido = new GraphLink<>(true);

        grafoDirigido.insertVertex("X");
        grafoDirigido.insertVertex("Y");
        grafoDirigido.insertVertex("Z");

        grafoDirigido.insertarEdge("X", "Y", 3);
        grafoDirigido.insertarEdge("Y", "Z", 4);

        System.out.println("Grafo dirigido inicial:");
        System.out.println(grafoDirigido);

        grafoDirigido.removeEdge("X", "Y");
        System.out.println("Después de eliminar arista X → Y:");
        System.out.println(grafoDirigido);

        System.out.println("DFS desde Y:");
        grafoDirigido.dfs("Y");
    }
   
}
