package services.interfaces;

import exceptions.ExceptionWithStatusCode;
import exceptions.NoSuchGroupException;
import model.ProductGroup;

public interface GroupServiceInterface {

    ProductGroup addGroup(ProductGroup group) throws ExceptionWithStatusCode;
    ProductGroup getGroup(Long id) throws ExceptionWithStatusCode;
    ProductGroup updateGroup(ProductGroup group) throws ExceptionWithStatusCode;
    void deleteGroup(Long id) throws ExceptionWithStatusCode;

}
