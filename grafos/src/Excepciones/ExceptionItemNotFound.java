package Excepciones;

@SuppressWarnings("serial")
public class ExceptionItemNotFound extends RuntimeException {
    public ExceptionItemNotFound(String mnsj) {
        super(mnsj);
    }
    public ExceptionItemNotFound(){}
}
