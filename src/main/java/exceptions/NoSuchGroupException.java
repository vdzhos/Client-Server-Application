package exceptions;

public class NoSuchGroupException extends ExceptionWithStatusCode{

    public static final int STATUS_CODE = 404;

    public NoSuchGroupException(Long id) {
        super("There is no group with id = " + id);
    }

    @Override
    public int getStatusCode() {
        return STATUS_CODE;
    }

}
