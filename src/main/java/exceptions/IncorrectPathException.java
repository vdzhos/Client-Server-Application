package exceptions;

public class IncorrectPathException extends Exception{

    public IncorrectPathException(String path) {
        super("Incorrect path: " + path);
    }

}
