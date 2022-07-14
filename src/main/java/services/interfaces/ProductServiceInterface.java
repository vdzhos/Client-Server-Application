package services.interfaces;

import database.ProductCriteriaQuery;
import exceptions.ExceptionWithStatusCode;
import exceptions.NoSuchProductException;
import model.Product;
import java.util.List;

public interface ProductServiceInterface {

    Product addProduct(Product product) throws ExceptionWithStatusCode;
    Product getProduct(Long id) throws ExceptionWithStatusCode;
    Product updateProduct(Product product) throws ExceptionWithStatusCode;
    void deleteProduct(Long id) throws ExceptionWithStatusCode;

    int getProductQuantity(Long id) throws Exception;
    int decreaseProductQuantity(Long id, int quantity) throws Exception;
    int increaseProductQuantity(Long id, int quantity) throws Exception;
    double updateProductPrice(Long id, double price) throws Exception;

    List<Product> listProductsByCriteria(ProductCriteriaQuery criteria) throws ExceptionWithStatusCode;

}
