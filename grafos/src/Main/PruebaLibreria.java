package Main;

import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm.SingleSourcePaths;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

public class PruebaLibreria {
	 public static void main(String[] args) {
	        // Crear un grafo dirigido y ponderado
	        Graph<String, DefaultWeightedEdge> grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

	        // Agregar ciudades como vértices
	        grafo.addVertex("Lima");
	        grafo.addVertex("Arequipa");
	        grafo.addVertex("Cusco");
	        grafo.addVertex("Trujillo");
	        grafo.addVertex("Puno");

	        // Agregar rutas con distancias (pesos)
	        grafo.setEdgeWeight(grafo.addEdge("Lima", "Arequipa"), 1000);
	        grafo.setEdgeWeight(grafo.addEdge("Lima", "Cusco"), 1100);
	        grafo.setEdgeWeight(grafo.addEdge("Cusco", "Puno"), 400);
	        grafo.setEdgeWeight(grafo.addEdge("Arequipa", "Puno"), 500);
	        grafo.setEdgeWeight(grafo.addEdge("Trujillo", "Lima"), 570);
	        grafo.setEdgeWeight(grafo.addEdge("Puno", "Trujillo"), 1400);

	        // Mostrar los vértices
	        System.out.println("🏙️Ciudades (vértices):");
	        for (String ciudad : grafo.vertexSet()) {
	            System.out.println(" - " + ciudad);
	        }

	        // Mostrar las rutas con distancia
	        System.out.println("\n🛣 Rutas (aristas con distancia):");
	        for (DefaultWeightedEdge e : grafo.edgeSet()) {
	            String origen = grafo.getEdgeSource(e);
	            String destino = grafo.getEdgeTarget(e);
	            double peso = grafo.getEdgeWeight(e);
	            System.out.println(origen + " -> " + destino + " = " + peso + " km");
	        }

	        // Recorrido BFS desde Lima
	        System.out.println("\n🚌 Recorrido BFS desde Lima:");
	        BreadthFirstIterator<String, DefaultWeightedEdge> bfs =
	            new BreadthFirstIterator<>(grafo, "Lima");
	        while (bfs.hasNext()) {
	            System.out.println("📌 Visitado: " + bfs.next());
	        }

	        // Camino más corto usando Dijkstra
	        System.out.println("\n📏 Camino más corto de Lima a Puno:");
	        DijkstraShortestPath<String, DefaultWeightedEdge> dijkstra =
	            new DijkstraShortestPath<>(grafo);
	        SingleSourcePaths<String, DefaultWeightedEdge> caminos = dijkstra.getPaths("Lima");
	        System.out.println("Ruta: " + caminos.getPath("Puno"));
	        System.out.println("Distancia total: " + caminos.getPath("Puno").getWeight() + " km");
	    }

}
