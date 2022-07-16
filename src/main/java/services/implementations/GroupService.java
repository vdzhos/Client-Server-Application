package services.implementations;

import database.ProductCriteriaQuery;
import database.ProductCriteriaQueryBuilder;
import database.ProductGroupCriteriaQuery;
import database.ProductGroupCriteriaQueryBuilder;
import exceptions.ExceptionWithStatusCode;
import exceptions.NoSuchGroupException;
import model.ProductGroup;
import repositories.implementations.GroupRepository;
import services.interfaces.GroupServiceInterface;

import java.util.List;

public class GroupService implements GroupServiceInterface {

    private GroupRepository repository;

    public GroupService() {
        this.repository = new GroupRepository();
    }

    @Override
    public ProductGroup addGroup(ProductGroup group) throws ExceptionWithStatusCode {
        return repository.create(group);
    }

    @Override
    public ProductGroup getGroup(Long id) throws ExceptionWithStatusCode {
        return repository.read(id);
    }

    @Override
    public ProductGroup updateGroup(ProductGroup group) throws ExceptionWithStatusCode {
        return repository.update(group);
    }

    @Override
    public void deleteGroup(Long id) throws ExceptionWithStatusCode {
        repository.delete(id);
    }

    @Override
    public List<ProductGroup> listGroupsByCriteria(ProductGroupCriteriaQuery criteria) throws ExceptionWithStatusCode {
        ProductGroupCriteriaQuery params;
        if(criteria==null) {
            params = new ProductGroupCriteriaQueryBuilder().build();
        } else {
            params = criteria;
        }
        return repository.listByCriteria(params);
    }

}
