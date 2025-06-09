package graph;

import ClassAux.ParDistancia;
import ClassAux.ParPadre;
import ImpQueue.PriorityQueueLinkSort;
import ImpQueue.QueueLink;
import ImpStack.StackLink;
import ListLinked.ListaEnlazada;


public class GraphLink<E extends Comparable<E>> {
	protected ListaEnlazada<Vertex<E>> listVertex; //lista de todos los vertices del grafo
	boolean isDirected;
	
	public GraphLink(boolean isDirected) {
		listVertex = new ListaEnlazada<Vertex<E>>();
		this.isDirected = isDirected;
	}
	
    public void insertVertex(E data) {
        if (data == null) return;

        Vertex<E> newVertex = new Vertex<>(data);
        if (!listVertex.contains(newVertex)) {
            listVertex.insertLast(newVertex);
        }
    }
    public void insertEdge(E verOri, E verDes) {
        insertEdgeWeight(verOri, verDes, 1); // peso por defecto
    }

    public void insertEdgeWeight(E verOri, E verDes, int weight) {
        Vertex<E> origen = searchVertexObject(verOri);
        Vertex<E> destino = searchVertexObject(verDes);

        if (origen == null || destino == null) {
            System.out.println("uno o los dos vertices no existen");
            return;
        }

        Edge<E> objarista = new Edge<>(destino, weight);

        if (!origen.listAdj.contains(objarista)) {
            origen.listAdj.insertLast(objarista);
        }
        if (!isDirected) {
            Edge<E> objarista2 = new Edge<>(origen, weight);
            if (!destino.listAdj.contains(objarista2)) {
            	destino.listAdj.insertLast(objarista2);
            }
        }   
    }
    
	private Vertex<E> searchVertexObject(E verOri) {
		for (Vertex<E> v : listVertex) {
            if (v.getData().equals(verOri)) {
                return v;
            }
        }
        return null;
	}
	
	 public boolean searchVertex(E data) {
	        return searchVertexObject(data) != null;
	 }
	 
	 public boolean searchEdge(E verOri, E verDes) {
	        Vertex<E> origin = searchVertexObject(verOri);
	        Vertex<E> destination = searchVertexObject(verDes);

	        if (origin == null || destination == null) return false;

	        for (Edge<E> edge : origin.listAdj) {
	            if (edge.getrefDest().equals(destination)) return true;
	        }

	        return false;
	    }
	 
	public void removeEdge(E verOri, E verDes) {
		Vertex<E> origen = searchVertexObject(verOri);
		Vertex<E> destino = searchVertexObject(verDes);

		if(origen == null || destino == null) {
			System.out.println("uno o ambos vertices no exiten");
			return;
		}
		origen.listAdj.remove(new Edge<>(destino));
		//si es no dirigido, tambien elimina de destino a origen
		if(!isDirected) {
			destino.listAdj.remove(new Edge<>(origen));
			
		}
		
	}
	public boolean removeVertex(E verOri) {
		Vertex<E> obj = searchVertexObject(verOri);
		
		if(obj == null) {
			return false;
		}
		for (Vertex<E> v : listVertex) {
			if(!v.equals(obj)) {
				v.listAdj.remove(new Edge<>(obj));
				if(!isDirected) {
					obj.listAdj.remove(new Edge<>(v));
				}
			}
		}
		return listVertex.remove(obj);
		
	}
	public boolean dfs(E data) {
		Vertex<E> verInicio = searchVertexObject(data);
		if(verInicio == null) {
			return false;
		}
		ListaEnlazada<Vertex<E>> visitados = new ListaEnlazada<>();
	    System.out.print("DFS desde " + data + ": ");

		dfsRecursivo(verInicio, visitados);
		return true;
		
	}
	private void dfsRecursivo( Vertex<E> verActual, ListaEnlazada<Vertex<E>>visitados) {
		visitados.insertLast(verActual);
	    System.out.print(verActual.getData() + " "); //ver vertices

		
		for(Edge<E> edge : verActual.listAdj) {
			Vertex<E> vecino = edge.getrefDest();
			if(!visitados.contains(vecino)) {
				dfsRecursivo(vecino, visitados);
			}
		}
	}
	
	//ejercicio1
	
	public boolean bfs(E data) {
		Vertex<E> inicio = searchVertexObject(data);
		if(inicio == null) {
			return false;
		}
		ListaEnlazada<Vertex<E>> visitados = new ListaEnlazada<>();
		QueueLink<Vertex<E>> cola = new QueueLink<>();
		cola.enqueue(inicio);
		visitados.insertLast(inicio);
		System.out.println("bfs desde "+ data +": ");
		
		while(!cola.isEmpty()) {
			Vertex<E> actual = null;
			try {
				actual = cola.dequeue();	
			}catch(Exception e) {
				e.printStackTrace();
			}
            System.out.print(actual.getData() + " ");
			
			for(Edge<E> edge : actual.listAdj) {
				Vertex<E> vecino = edge.getrefDest();
				if(!visitados.contains(vecino)) {
					cola.enqueue(vecino);
					visitados.insertLast(vecino);
				}
			}
		}
		System.out.println();
		return true;
	}
	
	// devuelve el camino mas corto en cantidad de aristas, no pesos
	
	public ListaEnlazada<E> bfsPath(E origen, E destino) {
        //Buscar los vértices de origen y destino
        Vertex<E> verOrigen = searchVertexObject(origen);
        Vertex<E> verDestino = searchVertexObject(destino);
        
        if (verOrigen == null || verDestino == null) {
            return new ListaEnlazada<E>(); // Retorna lista vacía si no existen
        }
        
        // 2. Crear lista de visitados
        ListaEnlazada<Vertex<E>> visitados = new ListaEnlazada<>();
        
        // 3. Crear cola para recorrido BFS
        QueueLink<Vertex<E>> cola = new QueueLink<>();
        
        // 4. Crear lista de padres para reconstruir el camino
        ListaEnlazada<ParPadre<E>> padres = new ListaEnlazada<>();
        
        // 5. Comenzar BFS desde el nodo origen
        cola.enqueue(verOrigen);
        visitados.insertLast(verOrigen);
        padres.insertLast(new ParPadre<>(origen, null)); // origen no tiene padre
        
        boolean encontrado = false;
        
        // 6. Mientras la cola no esté vacía
        while (!cola.isEmpty() && !encontrado) {
            Vertex<E> actual = null;
            try {
                actual = cola.dequeue();
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // Si es el destino, se detiene
            if (actual.getData().equals(destino)) {
                encontrado = true;
                break;
            }
            
            // Si no, se agregan sus vecinos no visitados
            for (Edge<E> edge : actual.listAdj) {
                Vertex<E> vecino = edge.getrefDest();
                if (!visitados.contains(vecino)) {
                    cola.enqueue(vecino);
                    visitados.insertLast(vecino);
                    padres.insertLast(new ParPadre<>(vecino.getData(), actual.getData()));
                }
            }
        }
        
        // 7. Reconstruir el camino desde destino hacia origen
        ListaEnlazada<E> camino = new ListaEnlazada<>();
        
        if (encontrado) {
            E actual = destino;
            
            // Recorrer hacia atrás usando la lista de padres
            while (actual != null) {
                camino.insertFirst(actual); // Insertar al principio para invertir el orden
                
                // Buscar el padre del actual
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
        
        // 8. Retornar la lista del camino
        return camino;
    }
	//ejercicio 2
	public ListaEnlazada<E> shortPath(E origen, E destino) {
	    // Este método encuentra el camino más corto en número de aristas (no en peso)

	    // 1. Es exactamente igual al BFS clásico como bfsPath()
	    //    - Pero no toma en cuenta pesos, solo conexiones (cantidad mínima de saltos)

	    // 2. Se usa BFS:
	    //    - Cola (QueueLink<Vertex<E>>)
	    //    - Visitados (ListaEnlazada<Vertex<E>>)
	    //    - Padres (ListaEnlazada<ParPadre<E>>)

	    // 3. Cuando llegas al destino, reconstruyes el camino como en bfsPath()

	    // 4. Devuelves ListaEnlazada<E> con el recorrido
        return bfsPath(origen, destino);
	}
	
	//si el numero de visitados es igual al total de vertices, entonces es conexo
	public boolean isConexo() {
	    if (listVertex.isEmptyList()) return true; // Grafo vacío es conexo

	    Vertex<E> inicio = listVertex.getFirst().getData(); // obtengo el dato del primer nodo
	    ListaEnlazada<Vertex<E>> visitados = new ListaEnlazada<>();
	    QueueLink<Vertex<E>> cola = new QueueLink<>();

	    cola.enqueue(inicio);
	    visitados.insertLast(inicio);

	    while (!cola.isEmpty()) {
	        Vertex<E> actual = null;
	        try {
	            actual = cola.dequeue();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        for (Edge<E> edge : actual.listAdj) {
	            Vertex<E> vecino = edge.getrefDest();
	            if (!visitados.contains(vecino)) {
	                cola.enqueue(vecino);
	                visitados.insertLast(vecino);
	            }
	        }
	    }

	    // El grafo es conexo si visitamos todos los vértices
	    return visitados.length() == listVertex.length();
	}
	
	//devuelve el mejor camino (menor peso)
	public StackLink<E> Dijkstra(E origen, E destino) {
		
		// 1. Crear lista de distancias
        ListaEnlazada<ParDistancia<E>> distancias = new ListaEnlazada<>();
        
        // 2. Crear cola de prioridad
        PriorityQueueLinkSort<Vertex<E>, Integer> colaPrioridad = new PriorityQueueLinkSort<>();
        
        // 3. Inicializar distancias: origen = 0, resto = infinito
        for (Vertex<E> v : listVertex) {
            if (v.getData().equals(origen)) {
                distancias.insertLast(new ParDistancia<>(v.getData(), 0, null));
                colaPrioridad.enqueue(v, 0);
            } else {
                distancias.insertLast(new ParDistancia<>(v.getData(), Integer.MAX_VALUE, null));
            }
        }
        
        ListaEnlazada<Vertex<E>> visitados = new ListaEnlazada<>();
        
        // 4. Mientras la cola no esté vacía
        while (!colaPrioridad.isEmpty()) {
            Vertex<E> actual = null;
            try {
                actual = colaPrioridad.dequeue();
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            if (visitados.contains(actual)) continue;
            visitados.insertLast(actual);
            
            // Si llegamos al destino, podemos parar
            if (actual.getData().equals(destino)) {
                break;
            }
            
            // Obtener distancia actual
            int distanciaActual = 0;
            for (ParDistancia<E> pd : distancias) {
                if (pd.getVertice().equals(actual.getData())) {
                    distanciaActual = pd.getDistancia();
                    break;
                }
            }
            
            // Para cada vecino
            for (Edge<E> edge : actual.listAdj) {
                Vertex<E> vecino = edge.getrefDest();
                
                if (visitados.contains(vecino)) continue;
                
                int nuevaDistancia = distanciaActual + edge.getWeight();
                
                // Buscar y actualizar distancia del vecino si es menor
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
        
        // 5. Reconstruir el camino usando StackLink
        StackLink<E> camino = new StackLink<>();
        
        // Verificar si existe camino al destino
        boolean existeCamino = false;
        for (ParDistancia<E> pd : distancias) {
            if (pd.getVertice().equals(destino) && pd.getDistancia() != Integer.MAX_VALUE) {
                existeCamino = true;
                break;
            }
        }
        
        if (existeCamino) {
            E actual = destino;
            
            // Apilar desde destino hasta origen
            while (actual != null) {
                camino.push(actual);
                
                // Buscar el padre
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
        
        // 6. Retornar el stack con el camino óptimo
        return camino;
	}

	public String toString() {
		return this.listVertex.toString();
	}
	

}
