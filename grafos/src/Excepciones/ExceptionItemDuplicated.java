package Excepciones;

@SuppressWarnings("serial")
public class ExceptionItemDuplicated extends RuntimeException {
    public ExceptionItemDuplicated(String mnsj) {
        super(mnsj);
    }
    public ExceptionItemDuplicated(){}
}
