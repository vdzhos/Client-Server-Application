package exceptions;

import utils.FailureStatusCodes;

public class NoSuchProductException extends ExceptionWithStatusCode{

    public static final int STATUS_CODE = FailureStatusCodes.NOT_FOUND;

    public NoSuchProductException(Long id) {
        super("There is no product with id = " + id);
    }

    @Override
    public int getStatusCode() {
        return STATUS_CODE;
    }

}
