package exceptions;

import utils.FailureStatusCodes;

public class InternalException extends ExceptionWithStatusCode{

    public static final int STATUS_CODE = FailureStatusCodes.INTERNAL_ERROR;

    public InternalException(String message) {
        super(message);
    }

    @Override
    public int getStatusCode() {
        return STATUS_CODE;
    }
}
