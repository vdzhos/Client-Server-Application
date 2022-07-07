package services.implementations;

import model.ProductGroup;
import repositories.implementations.GroupRepository;
import services.interfaces.GroupServiceInterface;

public class GroupService implements GroupServiceInterface {

    private GroupRepository repository;

    public GroupService() {
        this.repository = new GroupRepository();
    }

    @Override
    public ProductGroup addGroup(ProductGroup group) throws Exception {
        return repository.create(group);
    }

    @Override
    public ProductGroup getGroup(Long id) throws Exception {
        return repository.read(id);
    }

    @Override
    public ProductGroup updateGroup(ProductGroup group) throws Exception {
        return repository.update(group);
    }

    @Override
    public void deleteGroup(Long id) throws Exception {
        repository.delete(id);
    }

}
