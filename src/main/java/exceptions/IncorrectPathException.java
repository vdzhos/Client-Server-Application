package exceptions;

public class IncorrectPathException extends ExceptionWithStatusCode{

    public static final int STATUS_CODE = 400;

    public IncorrectPathException(String path) {
        super("Incorrect path: " + path);
    }

    @Override
    public int getStatusCode() {
        return STATUS_CODE;
    }

}
