package ClassAux;

public class ParPadre<E extends Comparable<E>> implements Comparable<ParPadre<E>> {
    private E hijo;
    private E padre;
    
    public ParPadre(E hijo, E padre) {
        this.hijo = hijo;
        this.padre = padre;
    }
    
    public E getHijo() { return hijo; }
    public E getPadre() { return padre; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ParPadre<?> parPadre = (ParPadre<?>) obj;
        return hijo.equals(parPadre.hijo);
    }
    
    @Override
    public int compareTo(ParPadre<E> other) {
        return this.hijo.compareTo(other.hijo);
    }
}