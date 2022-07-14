package exceptions;

public abstract class ExceptionWithStatusCode extends Exception {

    public ExceptionWithStatusCode(String message) {
        super(message);
    }

    public abstract int getStatusCode();

}