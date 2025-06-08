package Excepciones;

@SuppressWarnings("serial")
public class ExceptionIsEmpty extends RuntimeException {
 
    public ExceptionIsEmpty(String mnsj) {
        super(mnsj);
    }
    public ExceptionIsEmpty(){}
}
