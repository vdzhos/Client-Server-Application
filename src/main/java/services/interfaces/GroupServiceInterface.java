package services.interfaces;

import database.ProductGroupCriteriaQuery;
import exceptions.ExceptionWithStatusCode;
import model.ProductGroup;

import java.util.List;

public interface GroupServiceInterface {

    ProductGroup addGroup(ProductGroup group) throws ExceptionWithStatusCode;
    ProductGroup getGroup(Long id) throws ExceptionWithStatusCode;
    ProductGroup updateGroup(ProductGroup group) throws ExceptionWithStatusCode;
    void deleteGroup(Long id) throws ExceptionWithStatusCode;

    List<ProductGroup> listGroupsByCriteria(ProductGroupCriteriaQuery criteria) throws ExceptionWithStatusCode;
}
