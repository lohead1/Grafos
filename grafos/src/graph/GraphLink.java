package graph;

import ClassAux.ParDistancia;
import ClassAux.ParPadre;
import ImpQueue.PriorityQueueLinkSort;
import ImpQueue.QueueLink;
import ImpStack.StackLink;
import ListLinked.ListaEnlazada;

public class GraphLink<E extends Comparable<E>> {
	protected ListaEnlazada<Vertex<E>> listVertex;
	
	boolean isDirected;

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

		if (origen == null) {
	        throw new IllegalArgumentException("El vértice de origen " + verOri + " no existe.");
	    }
	    if (destino == null) {
	        throw new IllegalArgumentException("El vértice de destino " + verDes + " no existe.");
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
	 * 
	 * @param data Dato del vértice a buscar
	 * @return Objeto Vertex si se encuentra, null en caso contrario
	 */
	private Vertex<E> searchVertexObject(E data) {
		/* Recorrer todos los vértices del grafo */
		for (Vertex<E> v : listVertex) {
			/* Comparar usando equals() para encontrar coincidencia */
			if (v.getData().equals(data)) {
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

	public boolean removeVertex(E verOri) {
	    Vertex<E> obj = searchVertexObject(verOri);
	    if (obj == null) return false;

	    // Limpiar todas las aristas del vértice eliminado
	    obj.listAdj.destroyList();

	    // Eliminar referencias desde otros vértices
	    for (Vertex<E> v : listVertex) {
	        if (!v.equals(obj)) {
	            v.listAdj.remove(new Edge<>(obj));
	        }
	    }

	    return listVertex.remove(obj);
	}
	/**
	 * Realiza recorrido DFS profundidad desde un vértice dado.
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
			
			/* Recurrir solo en vecinos no visitados 
			 * */
			if (!visitados.contains(vecino)) {
				dfsRecursivo(vecino, visitados);
			}
		}
	}

	/**
	 * Realiza recorrido BFS anchura desde un vértice dado.
	 * Implementación iterativa usando cola que visita por niveles.
	 * 
	 * @param data Vértice desde donde iniciar el recorrido
	 * @return true si se realizó el recorrido, false si el vértice no existe
	 */
	public boolean bfs(E data) {
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
	 * retorna el camino más corto en número de aristas.
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
	    	//sacamos vertice con menor prioridad
	        Vertex<E> actual = null;
	        try {
	            actual = colaPrioridad.dequeue();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        
	        //si marca como visitado se agrega a la lista
	        if (visitados.contains(actual)) continue;
	        visitados.insertLast(actual);
	        
	        /* si es vertice actual es el destino, sale */
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
	            if (visitados.contains(vecino)) continue; // Saltar si ya visitado
	            
	            /* Calcular nueva distancia a través del vértice actual */
	            int nuevaDistancia = distanciaActual + edge.getWeight();
	            
	            /* Actualizar si la nueva distancia es mejor */
	            for (ParDistancia<E> pd : distancias) {
	                if (pd.getVertice().equals(vecino.getData())) {
	                    if (nuevaDistancia < pd.getDistancia()) {
	                        pd.setDistancia(nuevaDistancia);
	                        pd.setPadre(actual.getData());
	                        /* Usar updatePriority para actualizar la cola */
	                        colaPrioridad.updatePriority(vecino, nuevaDistancia);
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
	 * Calcula el grado de un nodo específico
	 * El grado es la cantidad de aristas conectadas al nodo
	 * 
	 * @param data Dato del vértice
	 * @return Grado del vértice, -1 si no existe
	 */
	public int getNodeDegree(E data) {
	    Vertex<E> vertex = searchVertexObject(data);
	    if (vertex == null) return -1;
	    
	    return vertex.listAdj.length();
	}

	/**
	 * Obtiene todos los grados de los nodos del grafo
	 * 
	 * @return Lista enlazada con pares (nodo, grado)
	 */
	public ListaEnlazada<String> getAllNodeDegrees() {
	    ListaEnlazada<String> degrees = new ListaEnlazada<>();
	    
	    for (Vertex<E> vertex : listVertex) {
	        int degree = vertex.listAdj.length();
	        degrees.insertLast("G(" + vertex.getData() + ") = " + degree);
	    }
	    
	    return degrees;
	}
	/**
	 * Un camino tiene exactamente 2 nodos de grado 1 (extremos) y n-2 nodos de grado 2
	 * Fórmula matemática: Para Px con x nodos:
	 * - Número de aristas = x - 1
	 * - 2 nodos con grado 1, (x-2) nodos con grado 2
	 * Verifica si el grafo es de tipo CAMINO (Px)
	 * 
	 * @return true si es un camino, false en caso contrario
	 */
	public boolean isPath() {
	    // Solo aplica a grafos no dirigidos
	    if (isDirected || listVertex.length() < 2) return false;
	    
	    int n = listVertex.length();
	    int nodesWithDegree1 = 0;  // Contador de nodos con grado 1
	    int nodesWithDegree2 = 0;  // Contador de nodos con grado 2
	    int totalEdges = 0;        // Contador total de aristas
	    
	    for (Vertex<E> vertex : listVertex) {
	        int degree = vertex.listAdj.length();
	        totalEdges += degree;
	        
	        if (degree == 1) {
	            nodesWithDegree1++;
	        } else if (degree == 2) {
	            nodesWithDegree2++;
	        } else {
	            return false; // Si hay nodos con grado diferente a 1 o 2, no es camino
	        }
	    }	   
	    totalEdges /= 2;

	    return (nodesWithDegree1 == 2) && 
	           (nodesWithDegree2 == n - 2) && 
	           (totalEdges == n - 1) &&
	           isConexo();
	}
	/**
	 * Un ciclo tiene todos los nodos con grado exactamente 2
	 * Fórmula matemática: Para Cx con x nodos:
	 * - Número de aristas = x
	 * - Todos los nodos tienen grado 2
	 * Verifica si el grafo es de tipo CICLO (Cx)
	 * @return true si es un ciclo, false en caso contrario
	 */
	public boolean isCycle() {
	    // Solo aplica a grafos no dirigidos y debe tener al menos 3 nodos
	    if (isDirected || listVertex.length() < 3) return false;
	    
	    int n = listVertex.length();
	    int totalEdges = 0;
	    
	    // Verificar que todos los nodos tengan grado exactamente 2
	    for (Vertex<E> vertex : listVertex) {
	        int degree = vertex.listAdj.length();
	        
	        // Si algún nodo no tiene grado 2, no es un ciclo
	        if (degree != 2) return false;
	        
	        totalEdges += degree;
	    }	    
	    totalEdges /= 2;	   	
	    return (totalEdges == n) && isConexo();
	}
	/**
	 * Una rueda tiene un nodo central de grado (n-1) y (n-1) nodos de grado 3
	 * que forman un ciclo conectado al centro
	 * Fórmula matemática: Para Wx con x nodos:
	 * - 1 nodo central con grado (x-1)
	 * - (x-1) nodos periféricos con grado 3
	 * - Número de aristas = 2(x-1)
	 * Verifica si el grafo es de tipo RUEDA (Wx)
	 * 
	 * @return true si es una rueda, false en caso contrario
	 */
	public boolean isWheel() {
	    // Solo aplica a grafos no dirigidos y debe tener al menos 4 nodos
	    if (isDirected || listVertex.length() < 4) return false;
	    
	    int n = listVertex.length();
	    int nodesWithDegreeN1 = 0; // Nodos con grado n-1 (centro)
	    int nodesWithDegree3 = 0;  // Nodos con grado 3 (periferia)
	    int totalEdges = 0;
	    Vertex<E> centerVertex = null;
	    
	    // Analizar grados de todos los nodos
	    for (Vertex<E> vertex : listVertex) {
	        int degree = vertex.listAdj.length();
	        totalEdges += degree;
	        
	        if (degree == n - 1) {
	            nodesWithDegreeN1++;
	            centerVertex = vertex; // Guardar referencia al nodo central
	        } else if (degree == 3) {
	            nodesWithDegree3++;
	        } else {
	            return false; // Grado inválido para rueda
	        }
	    }
	    
	    totalEdges /= 2; // Cada arista se cuenta dos veces
	    
	    // Verificar estructura básica matemática de rueda:
	    // 1. Exactamente 1 nodo con grado (n-1) - nodo central
	    // 2. Exactamente (n-1) nodos con grado 3 - nodos periféricos
	    // 3. Total de aristas = 2(n-1)
	    if (nodesWithDegreeN1 != 1 || nodesWithDegree3 != n - 1 || totalEdges != 2 * (n - 1)) {
	        return false;
	    }
	    
	    // Verificar que los nodos periféricos formen un ciclo
	    // Crear lista de nodos periféricos (excluyendo el centro)
	    ListaEnlazada<Vertex<E>> peripheralNodes = new ListaEnlazada<>();
	    for (Vertex<E> vertex : listVertex) {
	        if (!vertex.equals(centerVertex)) {
	            peripheralNodes.insertLast(vertex);
	        }
	    }
	    
	    // Verificar que cada nodo periférico esté conectado exactamente a:
	    // 1. El nodo central (1 conexión)
	    // 2. Exactamente 2 otros nodos periféricos (formando el ciclo exterior)
	    for (Vertex<E> pNode : peripheralNodes) {
	        boolean connectedToCenter = false;
	        int peripheralConnections = 0;
	        
	        // Examinar todas las conexiones del nodo periférico
	        for (Edge<E> edge : pNode.listAdj) {
	            if (edge.getrefDest().equals(centerVertex)) {
	                connectedToCenter = true;
	            } else if (peripheralNodes.contains(edge.getrefDest())) {
	                peripheralConnections++;
	            }
	        }
	        
	        // Cada nodo periférico debe estar conectado al centro y a exactamente 2 periféricos
	        if (!connectedToCenter || peripheralConnections != 2) {
	            return false;
	        }
	    }
	    
	    // Verificar que el grafo sea conexo
	    return isConexo();
	}
	/**
	 * Un grafo completo tiene todos los nodos conectados entre sí
	 * Fórmula matemática: Para Kx con x nodos:
	 * - Cada nodo tiene grado (x-1)
	 * - Número total de aristas = x(x-1)/2
	 * Verifica si el grafo es COMPLETO (Kx) 
	 * @return true si es completo, false en caso contrario
	 */
	public boolean isComplete() {
	    // Solo aplica a grafos no dirigidos
	    if (isDirected || listVertex.length() < 1) return false;
	    
	    int n = listVertex.length();
	    int expectedDegree = n - 1; // En grafo completo, cada nodo se conecta con todos los demás
	    int totalEdges = 0;
	    
	    // Verificar que todos los nodos tengan grado (n-1)
	    for (Vertex<E> vertex : listVertex) {
	        int degree = vertex.listAdj.length();
	        
	        // Si algún nodo no tiene el grado esperado, no es completo
	        if (degree != expectedDegree) return false;
	        
	        totalEdges += degree;
	    }
	    
	    // En grafo no dirigido, cada arista se cuenta dos veces
	    totalEdges /= 2;
	    
	    // Aplicar fórmula matemática del grafo completo:
	    // En un grafo completo Kn, el número de aristas = n(n-1)/2
	    // Esto se deriva de la combinatoria: C(n,2) = n!/(2!(n-2)!) = n(n-1)/2
	    int expectedEdges = (n * (n - 1)) / 2;
	    
	    // Verificar condiciones matemáticas:
	    // 1. Cada nodo tiene grado (n-1) - ya verificado arriba
	    // 2. Total de aristas = n(n-1)/2
	    // 3. El grafo debe ser conexo (siempre cierto si es completo)
	    return (totalEdges == expectedEdges) && isConexo();
	}
	/**
	 * Obtiene la matriz de adyacencia del grafo como array bidimensional de enteros.
	 * En la matriz: 0 = no hay arista, peso = hay arista con ese peso
	 * 
	 * @return Matriz de adyacencia como int[][]
	 */
	public int[][] getAdjacencyMatrix() {
	    int n = listVertex.length();
	    int[][] matrix = new int[n][n];
	    
	    // Crear array de vértices para mapeo directo por índice
	    E[] vertexArray = (E[]) new Comparable[n];
	    int index = 0;
	    for (Vertex<E> vertex : listVertex) {
	        vertexArray[index++] = vertex.getData();
	    }
	    
	    // Llenar la matriz
	    for (int i = 0; i < n; i++) {
	        E vertexData = vertexArray[i];
	        Vertex<E> vertex = searchVertexObject(vertexData);
	        
	        // Recorrer las aristas del vértice actual
	        for (Edge<E> edge : vertex.listAdj) {
	            E destData = edge.getrefDest().getData();
	            
	            // Encontrar índice del vértice destino
	            for (int j = 0; j < n; j++) {
	                if (vertexArray[j].equals(destData)) {
	                    matrix[i][j] = edge.getWeight();
	                    break;
	                }
	            }
	        }
	    }
	    
	    return matrix;
	}

	/**
	 * Obtiene la matriz de adyacencia binaria del grafo.
	 * En la matriz: 0 = no hay arista, 1 = hay arista (sin considerar peso)
	 * 
	 * @return Matriz de adyacencia binaria como int[][]
	 */
	public int[][] getBinaryAdjacencyMatrix() {
	    int n = listVertex.length();
	    int[][] matrix = new int[n][n];
	    
	    // Crear array de vértices para mapeo directo por índice
	    E[] vertexArray = (E[]) new Comparable[n];
	    int index = 0;
	    for (Vertex<E> vertex : listVertex) {
	        vertexArray[index++] = vertex.getData();
	    }
	    
	    // Llenar la matriz
	    for (int i = 0; i < n; i++) {
	        E vertexData = vertexArray[i];
	        Vertex<E> vertex = searchVertexObject(vertexData);
	        
	        // Recorrer las aristas del vértice actual
	        for (Edge<E> edge : vertex.listAdj) {
	            E destData = edge.getrefDest().getData();
	            
	            // Encontrar índice del vértice destino
	            for (int j = 0; j < n; j++) {
	                if (vertexArray[j].equals(destData)) {
	                    matrix[i][j] = 1; // Solo marca presencia, no peso
	                    break;
	                }
	            }
	        }
	    }
	    
	    return matrix;
	}

	/**
	 * Obtiene el orden de los vértices usado en la matriz de adyacencia.
	 * Útil para interpretar las posiciones en la matriz.
	 * 
	 * @return Array con el orden de los vértices
	 */
	public E[] getVertexOrder() {
	    int n = listVertex.length();
	    E[] vertexArray = (E[]) new Comparable[n];
	    int index = 0;
	    for (Vertex<E> vertex : listVertex) {
	        vertexArray[index++] = vertex.getData();
	    }
	    return vertexArray;
	}

	/**
	 * Imprime la matriz de adyacencia con pesos de forma formateada.
	 * Incluye etiquetas de filas y columnas para mejor legibilidad.
	 */
	public void printAdjacencyMatrix() {
	    int[][] matrix = getAdjacencyMatrix();
	    E[] order = getVertexOrder();
	    int n = matrix.length;
	    
	    if (n == 0) {
	        System.out.println("El grafo está vacío");
	        return;
	    }
	    
	    System.out.println("\n=== MATRIZ DE ADYACENCIA (con pesos) ===");
	    
	    // Imprimir encabezado de columnas
	    System.out.print("     ");
	    for (int j = 0; j < n; j++) {
	        System.out.printf("%4s", order[j]);
	    }
	    System.out.println();
	    
	    // Imprimir filas con etiquetas
	    for (int i = 0; i < n; i++) {
	        System.out.printf("%3s: ", order[i]);
	        for (int j = 0; j < n; j++) {
	            System.out.printf("%4d", matrix[i][j]);
	        }
	        System.out.println();
	    }
	    System.out.println();
	}

	/**
	 * Imprime la matriz de adyacencia binaria de forma formateada.
	 * Solo muestra 0s y 1s, ignorando los pesos.
	 */
	public void printBinaryAdjacencyMatrix() {
	    int[][] matrix = getBinaryAdjacencyMatrix();
	    E[] order = getVertexOrder();
	    int n = matrix.length;
	    
	    if (n == 0) {
	        System.out.println("El grafo está vacío");
	        return;
	    }
	    
	    System.out.println("\n=== MATRIZ DE ADYACENCIA BINARIA ===");
	    
	    // Imprimir encabezado de columnas
	    System.out.print("     ");
	    for (int j = 0; j < n; j++) {
	        System.out.printf("%3s", order[j]);
	    }
	    System.out.println();
	    
	    // Imprimir filas con etiquetas
	    for (int i = 0; i < n; i++) {
	        System.out.printf("%3s: ", order[i]);
	        for (int j = 0; j < n; j++) {
	            System.out.printf("%3d", matrix[i][j]);
	        }
	        System.out.println();
	    }
	    System.out.println();
	}

	/**
	 * Retorna la matriz de adyacencia como String formateado.
	 * Útil para logging o almacenamiento.
	 * 
	 * @param includePesos true para incluir pesos, false para matriz binaria
	 * @return String con la matriz formateada
	 */
	public String adjacencyMatrixToString(boolean includePesos) {
	    int[][] matrix = includePesos ? getAdjacencyMatrix() : getBinaryAdjacencyMatrix();
	    E[] order = getVertexOrder();
	    int n = matrix.length;
	    
	    if (n == 0) return "Grafo vacío";
	    
	    StringBuilder sb = new StringBuilder();
	    String title = includePesos ? "MATRIZ DE ADYACENCIA (con pesos)" : "MATRIZ DE ADYACENCIA BINARIA";
	    sb.append("=== ").append(title).append(" ===\n");
	    
	    // Encabezado de columnas
	    sb.append("     ");
	    for (int j = 0; j < n; j++) {
	        sb.append(String.format(includePesos ? "%4s" : "%3s", order[j]));
	    }
	    sb.append("\n");
	    
	    // Filas con datos
	    for (int i = 0; i < n; i++) {
	        sb.append(String.format("%3s: ", order[i]));
	        for (int j = 0; j < n; j++) {
	            sb.append(String.format(includePesos ? "%4d" : "%3d", matrix[i][j]));
	        }
	        sb.append("\n");
	    }
	    
	    return sb.toString();
	}

	/**
	 * Verifica si dos vértices son adyacentes consultando la matriz.
	 * Alternativa al método searchEdge() usando representación matricial.
	 * 
	 * @param vertex1 Primer vértice
	 * @param vertex2 Segundo vértice
	 * @return true si son adyacentes, false en caso contrario
	 */
	public boolean areAdjacentMatrix(E vertex1, E vertex2) {
	    int[][] matrix = getBinaryAdjacencyMatrix();
	    E[] order = getVertexOrder();
	    
	    int index1 = -1, index2 = -1;
	    
	    // Encontrar índices de los vértices
	    for (int i = 0; i < order.length; i++) {
	        if (order[i].equals(vertex1)) index1 = i;
	        if (order[i].equals(vertex2)) index2 = i;
	    }
	    
	    if (index1 == -1 || index2 == -1) return false;
	    
	    // Para grafo dirigido: verificar solo una dirección
	    // Para grafo no dirigido: la matriz es simétrica
	    return matrix[index1][index2] == 1;
	}

	/**
	 * Obtiene el peso de la arista entre dos vértices usando la matriz.
	 * 
	 * @param vertex1 Vértice origen
	 * @param vertex2 Vértice destino
	 * @return Peso de la arista, 0 si no existe, -1 si algún vértice no existe
	 */
	public int getEdgeWeightMatrix(E vertex1, E vertex2) {
	    int[][] matrix = getAdjacencyMatrix();
	    E[] order = getVertexOrder();
	    
	    int index1 = -1, index2 = -1;
	    
	    // Encontrar índices de los vértices
	    for (int i = 0; i < order.length; i++) {
	        if (order[i].equals(vertex1)) index1 = i;
	        if (order[i].equals(vertex2)) index2 = i;
	    }
	    
	    if (index1 == -1 || index2 == -1) return -1;
	    
	    return matrix[index1][index2];
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
