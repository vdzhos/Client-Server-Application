package exceptions;

public class InternalException extends ExceptionWithStatusCode{

    public static final int STATUS_CODE = 500;

    public InternalException(String message) {
        super(message);
    }

    @Override
    public int getStatusCode() {
        return STATUS_CODE;
    }
}
