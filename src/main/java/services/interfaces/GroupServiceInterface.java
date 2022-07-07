package services.interfaces;

import model.ProductGroup;

public interface GroupServiceInterface {

    ProductGroup addGroup(ProductGroup group) throws Exception;
    ProductGroup getGroup(Long id) throws Exception;
    ProductGroup updateGroup(ProductGroup group) throws Exception;
    void deleteGroup(Long id) throws Exception;

}
