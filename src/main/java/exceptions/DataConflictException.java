package exceptions;

import utils.FailureStatusCodes;

public class DataConflictException extends ExceptionWithStatusCode{

    public static final int STATUS_CODE = FailureStatusCodes.CONFLICT;

    public DataConflictException(String message) {
        super(message);
    }

    @Override
    public int getStatusCode() {
        return STATUS_CODE;
    }

}
