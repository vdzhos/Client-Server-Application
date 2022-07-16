package services.implementations;

import database.ProductCriteriaQuery;
import database.ProductCriteriaQueryBuilder;
import exceptions.DataConflictException;
import exceptions.ExceptionWithStatusCode;
import exceptions.InternalException;
import exceptions.NoSuchGroupException;
import model.Product;
import repositories.implementations.GroupRepository;
import repositories.implementations.ProductRepository;
import repositories.interfaces.GroupRepositoryInterface;
import repositories.interfaces.ProductRepositoryInterface;
import services.interfaces.ProductServiceInterface;

import java.util.List;

public class ProductService implements ProductServiceInterface {

    private ProductRepositoryInterface productRepository;
    private GroupRepositoryInterface groupRepository;

    public ProductService() {
        this.productRepository = new ProductRepository();
        this.groupRepository = new GroupRepository();
    }

    @Override
    public Product addProduct(Product product) throws ExceptionWithStatusCode {
        if(product.getPrice() <= 0) throw new DataConflictException("Price of the product must be greater than zero!");
        if(product.getQuantity() < 0) throw new DataConflictException("Quantity of the product must not be negative!");
        return productRepository.create(product);
    }

    @Override
    public Product getProduct(Long id) throws ExceptionWithStatusCode {
        return productRepository.read(id);
    }

    @Override
    public Product updateProduct(Product product) throws ExceptionWithStatusCode {
        if(product.getPrice() <= 0) throw new DataConflictException("Price of the product must be greater than zero!");
        if(product.getQuantity() < 0) throw new DataConflictException("Quantity of the product must not be negative!");
        return productRepository.update(product);
    }

    @Override
    public void deleteProduct(Long id) throws ExceptionWithStatusCode {
        productRepository.delete(id);
    }

    @Override
    public int getProductQuantity(Long id) throws ExceptionWithStatusCode {
        return productRepository.getQuantity(id);
    }

    @Override
    public int decreaseProductQuantity(Long id, int quantity) throws ExceptionWithStatusCode {
        if(quantity < 0) throw new DataConflictException("Quantity of the product must not be negative!");
        return productRepository.decreaseQuantity(id,quantity);
    }

    @Override
    public int increaseProductQuantity(Long id, int quantity) throws ExceptionWithStatusCode {
        if(quantity < 0) throw new DataConflictException("Quantity of the product must not be negative!");
        return productRepository.increaseQuantity(id,quantity);
    }

    @Override
    public double updateProductPrice(Long id, double price) throws Exception {
        if(price <= 0) throw new Exception("Price of the product must be greater than zero!");
        return productRepository.updatePrice(id,price);
    }

    @Override
    public List<Product> listProductsByCriteria(ProductCriteriaQuery criteria) throws ExceptionWithStatusCode {
        ProductCriteriaQuery params;
        if(criteria==null) {
            params = new ProductCriteriaQueryBuilder().build();
        } else {
            params = criteria;
        }
        return productRepository.listByCriteria(params);
    }

    @Override
    public double getTotalPrice(Long id) throws InternalException, NoSuchGroupException {
        if(id != null && !groupRepository.existsById(id)) {
            throw new NoSuchGroupException(id);
        }
        return productRepository.getTotalPrice(id);
    }
}
