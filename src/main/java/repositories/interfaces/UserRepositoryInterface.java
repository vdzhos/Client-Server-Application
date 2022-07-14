package repositories.interfaces;

import exceptions.ExceptionWithStatusCode;
import exceptions.InternalException;
import exceptions.NoSuchUserException;
import model.User;

public interface UserRepositoryInterface {

   User getUserByUsername(String username) throws ExceptionWithStatusCode;

}
