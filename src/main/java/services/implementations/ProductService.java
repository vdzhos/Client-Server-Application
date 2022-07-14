package services.implementations;

import database.ProductCriteriaQuery;
import database.ProductCriteriaQueryBuilder;
import exceptions.DataConflictException;
import exceptions.ExceptionWithStatusCode;
import exceptions.NoSuchProductException;
import model.Product;
import repositories.implementations.ProductRepository;
import services.interfaces.ProductServiceInterface;

import java.util.List;

public class ProductService implements ProductServiceInterface {

    private ProductRepository repository;

    public ProductService() {
        this.repository = new ProductRepository();
    }

    @Override
    public Product addProduct(Product product) throws ExceptionWithStatusCode {
        if(product.getPrice() <= 0) throw new DataConflictException("Price of the product must be greater than zero!");
        if(product.getQuantity() < 0) throw new DataConflictException("Quantity of the product must not be negative!");
        return repository.create(product);
    }

    @Override
    public Product getProduct(Long id) throws ExceptionWithStatusCode {
        return repository.read(id);
    }

    @Override
    public Product updateProduct(Product product) throws ExceptionWithStatusCode {
        if(product.getPrice() <= 0) throw new DataConflictException("Price of the product must be greater than zero!");
        if(product.getQuantity() < 0) throw new DataConflictException("Quantity of the product must not be negative!");
        return repository.update(product);
    }

    @Override
    public void deleteProduct(Long id) throws ExceptionWithStatusCode {
        repository.delete(id);
    }

    @Override
    public int getProductQuantity(Long id) throws Exception {
        return repository.getQuantity(id);
    }

    @Override
    public int decreaseProductQuantity(Long id, int quantity) throws Exception {
        if(quantity < 0) throw new Exception("Quantity of the product must not be negative!");
        return repository.decreaseQuantity(id,quantity);
    }

    @Override
    public int increaseProductQuantity(Long id, int quantity) throws Exception {
        if(quantity < 0) throw new Exception("Quantity of the product must not be negative!");
        return repository.increaseQuantity(id,quantity);
    }

    @Override
    public double updateProductPrice(Long id, double price) throws Exception {
        if(price <= 0) throw new Exception("Price of the product must be greater than zero!");
        return repository.updatePrice(id,price);
    }

    @Override
    public List<Product> listProductsByCriteria(ProductCriteriaQuery criteria) throws ExceptionWithStatusCode {
        ProductCriteriaQuery params;
        if(criteria==null) {
            params = new ProductCriteriaQueryBuilder().build();
        } else {
            params = criteria;
        }
        return repository.listByCriteria(params);
    }
}
