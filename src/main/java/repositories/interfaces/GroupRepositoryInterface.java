package repositories.interfaces;

import model.ProductGroup;

public interface GroupRepositoryInterface {

    ProductGroup create(ProductGroup group) throws Exception;
    ProductGroup read(Long id) throws Exception;
    ProductGroup update(ProductGroup group) throws Exception;
    void delete(Long id) throws Exception;

}
