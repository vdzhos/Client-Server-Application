package repositories.interfaces;

import exceptions.ExceptionWithStatusCode;
import exceptions.NoSuchGroupException;
import model.ProductGroup;

public interface GroupRepositoryInterface {

    ProductGroup create(ProductGroup group) throws ExceptionWithStatusCode;
    ProductGroup read(Long id) throws ExceptionWithStatusCode;
    ProductGroup update(ProductGroup group) throws ExceptionWithStatusCode;
    void delete(Long id) throws ExceptionWithStatusCode;

}
