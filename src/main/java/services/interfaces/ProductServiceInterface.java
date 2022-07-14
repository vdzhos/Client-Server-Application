package services.interfaces;

import database.ProductCriteriaQuery;
import exceptions.NoSuchProductException;
import model.Product;
import java.util.List;

public interface ProductServiceInterface {

    Product addProduct(Product product) throws Exception;
    Product getProduct(Long id) throws NoSuchProductException;
    Product updateProduct(Product product) throws Exception;
    void deleteProduct(Long id) throws Exception;

    int getProductQuantity(Long id) throws Exception;
    int decreaseProductQuantity(Long id, int quantity) throws Exception;
    int increaseProductQuantity(Long id, int quantity) throws Exception;
    double updateProductPrice(Long id, double price) throws Exception;

    List<Product> listProductsByCriteria(ProductCriteriaQuery criteria) throws Exception;

}
