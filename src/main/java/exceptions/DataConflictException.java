package exceptions;

public class DataConflictException extends ExceptionWithStatusCode{

    public static final int STATUS_CODE = 409;

    public DataConflictException(String message) {
        super(message);
    }

    @Override
    public int getStatusCode() {
        return STATUS_CODE;
    }

}
