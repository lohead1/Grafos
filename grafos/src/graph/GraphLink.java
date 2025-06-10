package graph;

import ClassAux.ParDistancia;
import ClassAux.ParPadre;
import ImpQueue.PriorityQueueLinkSort;
import ImpQueue.QueueLink;
import ImpStack.StackLink;
import ListLinked.ListaEnlazada;

public class GraphLink<E extends Comparable<E>> {
	/* Lista que contiene todos los vértices del grafo */
	protected ListaEnlazada<Vertex<E>> listVertex;
	
	/* Indica si el grafo es dirigido (true) o no dirigido (false) */
	boolean isDirected;
	
	/**
	 * Constructor que inicializa un grafo vacío.
	 * 
	 * @param isDirected true para grafo dirigido, false para no dirigido
	 */
	public GraphLink(boolean isDirected) {
		listVertex = new ListaEnlazada<Vertex<E>>();
		this.isDirected = isDirected;
	}
	
	/**
	 * Inserta un nuevo vértice al grafo si no existe previamente.
	 * Validación: No permite vértices con datos null ni duplicados.
	 * 
	 * @param data Dato del vértice a insertar
	 */
	public void insertVertex(E data) {
		/* Validación: no permitir datos null */
		if (data == null) return;
		
		/* Crear nuevo vértice con el dato proporcionado */
		Vertex<E> newVertex = new Vertex<>(data);
		
		/* Insertar solo si no existe un vértice con el mismo dato */
		if (!listVertex.contains(newVertex)) {
			listVertex.insertLast(newVertex);
		}
	}

	/**
	 * Inserta una arista sin peso entre dos vértices.
	 * Utiliza peso por defecto de 1.
	 * 
	 * @param verOri Vértice origen
	 * @param verDes Vértice destino
	 */
	public void insertEdge(E verOri, E verDes) {
		/* Delegar a método con peso, usando peso por defecto = 1 */
		insertEdgeWeight(verOri, verDes, 1);
	}

	/**
	 * Inserta una arista con peso específico entre dos vértices.
	 * Para grafos no dirigidos, crea automáticamente la arista inversa.
	 * 
	 * @param verOri Vértice origen
	 * @param verDes Vértice destino
	 * @param weight Peso de la arista
	 */
	public void insertEdgeWeight(E verOri, E verDes, int weight) {
		/* Buscar los objetos vértice correspondientes a los datos */
		Vertex<E> origen = searchVertexObject(verOri);
		Vertex<E> destino = searchVertexObject(verDes);

		/* Validar que ambos vértices existan */
		if (origen == null || destino == null) {
			System.out.println("uno o los dos vertices no existen");
			return;
		}

		/* Crear arista desde origen hacia destino con el peso dado */
		Edge<E> objarista = new Edge<>(destino, weight);

		/* Insertar arista solo si no existe previamente */
		if (!origen.listAdj.contains(objarista)) {
			origen.listAdj.insertLast(objarista);
		}

		/* Si es grafo no dirigido, agregar la arista inversa */
		if (!isDirected) {
			Edge<E> objarista2 = new Edge<>(origen, weight);
			if (!destino.listAdj.contains(objarista2)) {
				destino.listAdj.insertLast(objarista2);
			}
		}
	}

	/**
	 * Busca un vértice por su dato y retorna el objeto vértice completo.
	 * Método privado auxiliar para operaciones internas.
	 * 
	 * @param verOri Dato del vértice a buscar
	 * @return Objeto Vertex si se encuentra, null en caso contrario
	 */
	private Vertex<E> searchVertexObject(E verOri) {
		/* Recorrer todos los vértices del grafo */
		for (Vertex<E> v : listVertex) {
			/* Comparar usando equals() para encontrar coincidencia */
			if (v.getData().equals(verOri)) {
				return v;
			}
		}
		/* Retornar null si no se encuentra el vértice */
		return null;
	}

	/**
	 * Verifica si un vértice existe en el grafo.
	 * Método público para consulta externa.
	 * 
	 * @param data Dato del vértice a verificar
	 * @return true si existe, false en caso contrario
	 */
	public boolean searchVertex(E data) {
		/* Utilizar método auxiliar y verificar si retorna un objeto válido */
		return searchVertexObject(data) != null;
	}

	/**
	 * Verifica si existe una arista directa entre dos vértices.
	 * Solo busca en una dirección (desde origen hacia destino).
	 * 
	 * @param verOri Vértice origen
	 * @param verDes Vértice destino
	 * @return true si existe la arista, false en caso contrario
	 */
	public boolean searchEdge(E verOri, E verDes) {
		/* Buscar objetos vértice correspondientes */
		Vertex<E> origin = searchVertexObject(verOri);
		Vertex<E> destination = searchVertexObject(verDes);

		/* Validar existencia de ambos vértices */
		if (origin == null || destination == null) return false;

		/* Recorrer lista de adyacencia del vértice origen */
		for (Edge<E> edge : origin.listAdj) {
			/* Verificar si alguna arista apunta al vértice destino */
			if (edge.getrefDest().equals(destination)) return true;
		}

		return false;
	}

	/**
	 * Elimina una arista entre dos vértices específicos.
	 * Para grafos no dirigidos, elimina en ambas direcciones.
	 * 
	 * @param verOri Vértice origen
	 * @param verDes Vértice destino
	 */
	public void removeEdge(E verOri, E verDes) {
		/* Buscar objetos vértice correspondientes */
		Vertex<E> origen = searchVertexObject(verOri);
		Vertex<E> destino = searchVertexObject(verDes);

		/* Validar existencia de ambos vértices */
		if (origen == null || destino == null) {
			System.out.println("uno o ambos vertices no exiten");
			return;
		}

		/* Eliminar arista desde origen hacia destino */
		origen.listAdj.remove(new Edge<>(destino));
		
		/* Si es grafo no dirigido, eliminar también la arista inversa */
		if (!isDirected) {
			destino.listAdj.remove(new Edge<>(origen));
		}
	}

	/**
	 * Elimina un vértice y todas sus conexiones del grafo.
	 * Proceso: 1) Elimina referencias desde otros vértices
	 *          2) Remueve el vértice de la lista principal
	 * 
	 * @param verOri Dato del vértice a eliminar
	 * @return true si se eliminó exitosamente, false si no existía
	 */
	public boolean removeVertex(E verOri) {
		/* Buscar el objeto vértice a eliminar */
		Vertex<E> obj = searchVertexObject(verOri);

		if (obj == null) return false;

		/* Eliminar todas las referencias a este vértice desde otros vértices */
		for (Vertex<E> v : listVertex) {
			if (!v.equals(obj)) {
				/* Eliminar aristas que apuntan al vértice a eliminar */
				v.listAdj.remove(new Edge<>(obj));
				
				/* Para grafos no dirigidos, limpiar también la lista del vértice eliminado */
				if (!isDirected) {
					obj.listAdj.remove(new Edge<>(v));
				}
			}
		}
		
		/* Finalmente, eliminar el vértice de la lista principal */
		return listVertex.remove(obj);
	}

	/**
	 * Realiza recorrido DFS (Depth-First Search) desde un vértice dado.
	 * Implementación recursiva que visita en profundidad.
	 * 
	 * @param data Vértice desde donde iniciar el recorrido
	 * @return true si se realizó el recorrido, false si el vértice no existe
	 */
	public boolean dfs(E data) {
		/* Buscar vértice de inicio */
		Vertex<E> verInicio = searchVertexObject(data);
		if (verInicio == null) return false;

		/* Inicializar lista de vértices visitados */
		ListaEnlazada<Vertex<E>> visitados = new ListaEnlazada<>();
		
		System.out.print("DFS desde " + data + ": ");
		
		/* Llamar al método recursivo auxiliar */
		dfsRecursivo(verInicio, visitados);
		return true;
	}

	/**
	 * Método auxiliar recursivo para implementar DFS.
	 * Marca el vértice actual como visitado y recurre en sus vecinos no visitados.
	 * 
	 * @param verActual Vértice siendo procesado actualmente
	 * @param visitados Lista de vértices ya visitados
	 */
	private void dfsRecursivo(Vertex<E> verActual, ListaEnlazada<Vertex<E>> visitados) {
		/* Marcar vértice actual como visitado */
		visitados.insertLast(verActual);
		System.out.print(verActual.getData() + " ");

		/* Recorrer todos los vecinos del vértice actual */
		for (Edge<E> edge : verActual.listAdj) {
			Vertex<E> vecino = edge.getrefDest();
			
			/* Recurrir solo en vecinos no visitados */
			if (!visitados.contains(vecino)) {
				dfsRecursivo(vecino, visitados);
			}
		}
	}

	/**
	 * Realiza recorrido BFS (Breadth-First Search) desde un vértice dado.
	 * Implementación iterativa usando cola que visita por niveles.
	 * 
	 * @param data Vértice desde donde iniciar el recorrido
	 * @return true si se realizó el recorrido, false si el vértice no existe
	 */
	public boolean bfs(E data) {
		/* Buscar vértice de inicio */
		Vertex<E> inicio = searchVertexObject(data);
		if (inicio == null) return false;

		/* Inicializar estructuras auxiliares */
		ListaEnlazada<Vertex<E>> visitados = new ListaEnlazada<>();
		QueueLink<Vertex<E>> cola = new QueueLink<>();
		
		/* Agregar vértice inicial a cola y marcarlo como visitado */
		cola.enqueue(inicio);
		visitados.insertLast(inicio);

		System.out.println("bfs desde " + data + ": ");

		/* Procesar cola hasta que esté vacía */
		while (!cola.isEmpty()) {
			Vertex<E> actual = null;
			try {
				/* Extraer siguiente vértice de la cola */
				actual = cola.dequeue();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			System.out.print(actual.getData() + " ");

			/* Procesar todos los vecinos del vértice actual */
			for (Edge<E> edge : actual.listAdj) {
				Vertex<E> vecino = edge.getrefDest();
				
				/* Agregar vecinos no visitados a la cola */
				if (!visitados.contains(vecino)) {
					cola.enqueue(vecino);
					visitados.insertLast(vecino);
				}
			}
		}
		System.out.println();
		return true;
	}

	/**
	 * Encuentra el camino más corto entre dos vértices en número de aristas.
	 * Utiliza BFS con seguimiento de padres para reconstruir el camino.
	 * 
	 * @param origen Vértice de inicio
	 * @param destino Vértice de destino
	 * @return Lista enlazada con el camino (vacía si no existe conexión)
	 */
	public ListaEnlazada<E> bfsPath(E origen, E destino) {
		/* Buscar vértices de origen y destino */
		Vertex<E> verOrigen = searchVertexObject(origen);
		Vertex<E> verDestino = searchVertexObject(destino);

		/* Retornar lista vacía si algún vértice no existe */
		if (verOrigen == null || verDestino == null) return new ListaEnlazada<>();

		/* Inicializar estructuras para BFS con seguimiento de padres */
		ListaEnlazada<Vertex<E>> visitados = new ListaEnlazada<>();
		QueueLink<Vertex<E>> cola = new QueueLink<>();
		ListaEnlazada<ParPadre<E>> padres = new ListaEnlazada<>();

		/* Configurar estado inicial */
		cola.enqueue(verOrigen);
		visitados.insertLast(verOrigen);
		padres.insertLast(new ParPadre<>(origen, null)); /* El origen no tiene padre */

		boolean encontrado = false;

		/* Ejecutar BFS hasta encontrar destino o agotar posibilidades */
		while (!cola.isEmpty() && !encontrado) {
			Vertex<E> actual = null;
			try {
				actual = cola.dequeue();
			} catch (Exception e) {
				e.printStackTrace();
			}

			/* Verificar si llegamos al destino */
			if (actual.getData().equals(destino)) {
				encontrado = true;
				break;
			}

			/* Procesar vecinos del vértice actual */
			for (Edge<E> edge : actual.listAdj) {
				Vertex<E> vecino = edge.getrefDest();
				
				/* Agregar vecinos no visitados y registrar su padre */
				if (!visitados.contains(vecino)) {
					cola.enqueue(vecino);
					visitados.insertLast(vecino);
					padres.insertLast(new ParPadre<>(vecino.getData(), actual.getData()));
				}
			}
		}

		/* Reconstruir camino desde destino hacia origen usando padres */
		ListaEnlazada<E> camino = new ListaEnlazada<>();

		if (encontrado) {
			E actual = destino;

			/* Seguir cadena de padres hasta llegar al origen (padre = null) */
			while (actual != null) {
				camino.insertFirst(actual); /* Insertar al inicio para orden correcto */
				
				/* Buscar padre del vértice actual */
				E padre = null;
				for (ParPadre<E> par : padres) {
					if (par.getHijo().equals(actual)) {
						padre = par.getPadre();
						break;
					}
				}
				actual = padre;
			}
		}
		return camino;
	}

	/**
	 * Alias para bfsPath - retorna el camino más corto en número de aristas.
	 * 
	 * @param origen Vértice de inicio
	 * @param destino Vértice de destino
	 * @return Camino más corto entre los vértices
	 */
	public ListaEnlazada<E> shortPath(E origen, E destino) {
		return bfsPath(origen, destino);
	}

	/**
	 * Determina si el grafo es conexo.
	 * Un grafo es conexo si existe un camino entre cada par de vértices.
	 * Implementación: BFS desde un vértice, verificando que alcance todos.
	 * 
	 * @return true si el grafo es conexo, false en caso contrario
	 */
	public boolean isConexo() {
		/* Grafo vacío se considera conexo */
		if (listVertex.isEmptyList()) return true;

		/* Iniciar BFS desde el primer vértice */
		Vertex<E> inicio = listVertex.getFirst().getData();
		ListaEnlazada<Vertex<E>> visitados = new ListaEnlazada<>();
		QueueLink<Vertex<E>> cola = new QueueLink<>();

		cola.enqueue(inicio);
		visitados.insertLast(inicio);

		/* Ejecutar BFS para alcanzar todos los vértices posibles */
		while (!cola.isEmpty()) {
			Vertex<E> actual = null;
			try {
				actual = cola.dequeue();
			} catch (Exception e) {
				e.printStackTrace();
			}

			/* Agregar vecinos no visitados */
			for (Edge<E> edge : actual.listAdj) {
				Vertex<E> vecino = edge.getrefDest();
				if (!visitados.contains(vecino)) {
					cola.enqueue(vecino);
					visitados.insertLast(vecino);
				}
			}
		}
		
		/* El grafo es conexo si se visitaron todos los vértices */
		return visitados.length() == listVertex.length();
	}

	/**
	 * Algoritmo de Dijkstra para encontrar el camino de menor peso.
	 * Encuentra el camino con peso mínimo entre dos vértices.
	 * 
	 * @param origen Vértice de inicio
	 * @param destino Vértice de destino
	 * @return Pila con el camino de menor peso (vacía si no existe)
	 */
	public StackLink<E> Dijkstra(E origen, E destino) {
		/* Inicializar estructura de distancias para todos los vértices */
		ListaEnlazada<ParDistancia<E>> distancias = new ListaEnlazada<>();
		PriorityQueueLinkSort<Vertex<E>, Integer> colaPrioridad = new PriorityQueueLinkSort<>();

		/* Configurar distancias iniciales */
		for (Vertex<E> v : listVertex) {
			if (v.getData().equals(origen)) {
				/* Distancia del origen a sí mismo es 0 */
				distancias.insertLast(new ParDistancia<>(v.getData(), 0, null));
				colaPrioridad.enqueue(v, 0);
			} else {
				/* Distancia inicial infinita para otros vértices */
				distancias.insertLast(new ParDistancia<>(v.getData(), Integer.MAX_VALUE, null));
			}
		}

		ListaEnlazada<Vertex<E>> visitados = new ListaEnlazada<>();

		/* Procesar vértices en orden de distancia creciente */
		while (!colaPrioridad.isEmpty()) {
			Vertex<E> actual = null;
			try {
				actual = colaPrioridad.dequeue();
			} catch (Exception e) {
				e.printStackTrace();
			}

			/* Evitar procesar vértices ya visitados */
			if (visitados.contains(actual)) continue;
			visitados.insertLast(actual);

			/* Terminar si llegamos al destino */
			if (actual.getData().equals(destino)) break;

			/* Obtener distancia actual del vértice procesado */
			int distanciaActual = 0;
			for (ParDistancia<E> pd : distancias) {
				if (pd.getVertice().equals(actual.getData())) {
					distanciaActual = pd.getDistancia();
					break;
				}
			}

			/* Relajar aristas: actualizar distancias de vecinos si se encuentra mejor camino */
			for (Edge<E> edge : actual.listAdj) {
				Vertex<E> vecino = edge.getrefDest();
				if (visitados.contains(vecino)) continue;

				/* Calcular nueva distancia a través del vértice actual */
				int nuevaDistancia = distanciaActual + edge.getWeight();

				/* Actualizar si la nueva distancia es mejor */
				for (ParDistancia<E> pd : distancias) {
					if (pd.getVertice().equals(vecino.getData())) {
						if (nuevaDistancia < pd.getDistancia()) {
							pd.setDistancia(nuevaDistancia);
							pd.setPadre(actual.getData());
							colaPrioridad.enqueue(vecino, nuevaDistancia);
						}
						break;
					}
				}
			}
		}

		/* Reconstruir camino usando la información de padres */
		StackLink<E> camino = new StackLink<>();
		boolean existeCamino = false;

		/* Verificar si existe camino al destino */
		for (ParDistancia<E> pd : distancias) {
			if (pd.getVertice().equals(destino) && pd.getDistancia() != Integer.MAX_VALUE) {
				existeCamino = true;
				break;
			}
		}

		/* Construir camino siguiendo cadena de padres */
		if (existeCamino) {
			E actual = destino;
			while (actual != null) {
				camino.push(actual); /* Usar pila para obtener orden correcto */
				
				/* Buscar padre del vértice actual */
				E padre = null;
				for (ParDistancia<E> pd : distancias) {
					if (pd.getVertice().equals(actual)) {
						padre = pd.getPadre();
						break;
					}
				}
				actual = padre;
			}
		}
		return camino;
	}

	/**
	 * Retorna representación en cadena de todos los vértices del grafo.
	 * Delega a la implementación toString() de la lista de vértices.
	 * 
	 * @return String con la representación del grafo
	 */
	public String toString() {
		return this.listVertex.toString();
	}
}
