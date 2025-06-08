package graph;

import ImpQueue.QueueLink;
import ListLinked.ListaEnlazada;


public class GraphLink<E> {
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

    public void insertarEdge(E verOri, E verDes, int weight) {
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
	
	//ejercicios
	
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

	public String toString() {
		return this.listVertex.toString();
	}
	

}
