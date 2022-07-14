package exceptions;

import utils.FailureStatusCodes;

public class NoSuchUserException extends ExceptionWithStatusCode{

    public static final int STATUS_CODE = FailureStatusCodes.NOT_FOUND;

    public NoSuchUserException(String username) {
        super("There is no user with username = " + username);
    }

    @Override
    public int getStatusCode() {
        return STATUS_CODE;
    }

}
