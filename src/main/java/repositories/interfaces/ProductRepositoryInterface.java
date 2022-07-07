package repositories.interfaces;

import model.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductRepositoryInterface {

    Product create(Product product) throws Exception;
    Product read(Long id) throws Exception;
    Product update(Product group) throws Exception;
    void delete(Long id) throws Exception;

    int getQuantity(Long id) throws Exception;
    int decreaseQuantity(Long id, int quantity) throws Exception;
    int increaseQuantity(Long id, int quantity) throws Exception;
    double updatePrice(Long id, double price) throws Exception;

//    List<Product> listByCriteria(Long groupId, );

}
