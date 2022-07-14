package exceptions;

public class NoSuchProductException extends Exception{

    public NoSuchProductException(Long id) {
        super("There is no product with id = " + id);
    }
}
