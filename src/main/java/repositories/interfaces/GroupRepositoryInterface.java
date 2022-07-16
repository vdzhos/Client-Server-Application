package repositories.interfaces;

import database.ProductCriteriaQuery;
import database.ProductGroupCriteriaQuery;
import exceptions.ExceptionWithStatusCode;
import exceptions.NoSuchGroupException;
import model.Product;
import model.ProductGroup;

import java.util.List;

public interface GroupRepositoryInterface {

    ProductGroup create(ProductGroup group) throws ExceptionWithStatusCode;
    ProductGroup read(Long id) throws ExceptionWithStatusCode;
    ProductGroup update(ProductGroup group) throws ExceptionWithStatusCode;
    void delete(Long id) throws ExceptionWithStatusCode;

    List<ProductGroup> listByCriteria(ProductGroupCriteriaQuery criteria) throws Exception;

}
