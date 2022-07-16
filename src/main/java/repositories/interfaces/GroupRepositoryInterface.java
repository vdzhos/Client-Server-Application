package repositories.interfaces;

import database.ProductGroupCriteriaQuery;
import exceptions.ExceptionWithStatusCode;
import exceptions.InternalException;
import model.ProductGroup;

import java.util.List;

public interface GroupRepositoryInterface {

    ProductGroup create(ProductGroup group) throws ExceptionWithStatusCode;
    ProductGroup read(Long id) throws ExceptionWithStatusCode;
    ProductGroup update(ProductGroup group) throws ExceptionWithStatusCode;
    void delete(Long id) throws ExceptionWithStatusCode;

    List<ProductGroup> listByCriteria(ProductGroupCriteriaQuery criteria) throws Exception;

    boolean existsById(Long id) throws InternalException;
}
