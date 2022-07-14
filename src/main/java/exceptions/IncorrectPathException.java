package exceptions;

import utils.FailureStatusCodes;

public class IncorrectPathException extends ExceptionWithStatusCode{

    public static final int STATUS_CODE = FailureStatusCodes.BAD_REQUEST;

    public IncorrectPathException(String path) {
        super("Incorrect path: " + path);
    }

    @Override
    public int getStatusCode() {
        return STATUS_CODE;
    }

}
