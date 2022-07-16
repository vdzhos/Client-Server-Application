package repositories.interfaces;

import database.ProductCriteriaQuery;
import exceptions.ExceptionWithStatusCode;
import exceptions.InternalException;
import model.Product;

import java.util.List;

public interface ProductRepositoryInterface {

    Product create(Product product) throws ExceptionWithStatusCode;
    Product read(Long id) throws ExceptionWithStatusCode;
    Product update(Product group) throws ExceptionWithStatusCode;
    void delete(Long id) throws ExceptionWithStatusCode;

    int getQuantity(Long id) throws Exception;
    int decreaseQuantity(Long id, int quantity) throws Exception;
    int increaseQuantity(Long id, int quantity) throws Exception;
    double updatePrice(Long id, double price) throws Exception;

    List<Product> listByCriteria(ProductCriteriaQuery criteria) throws ExceptionWithStatusCode;

    double getTotalPrice(Long id) throws InternalException;

}
