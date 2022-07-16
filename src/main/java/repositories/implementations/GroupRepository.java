package repositories.implementations;

import database.DataBase;
import database.ProductCriteriaQuery;
import database.ProductGroupCriteriaQuery;
import database.Queries;
import exceptions.DataConflictException;
import exceptions.ExceptionWithStatusCode;
import exceptions.InternalException;
import exceptions.NoSuchGroupException;
import model.Product;
import model.ProductGroup;
import org.sqlite.SQLiteErrorCode;
import repositories.interfaces.GroupRepositoryInterface;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GroupRepository implements GroupRepositoryInterface {

    private DataBase dataBase;

    public GroupRepository() {
        dataBase = DataBase.getInstance();
    }

    @Override
    public ProductGroup create(ProductGroup group) throws ExceptionWithStatusCode{
        try (PreparedStatement st = dataBase.prepareStatement(Queries.CREATE_GROUP, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1,group.getName());
            st.setString(2,group.getDescription());
            int rowsAffected = st.executeUpdate();
            if(rowsAffected!=1){
                throw new InternalException("ProductGroup creation failed");
            }
            try (ResultSet generatedKeys = st.getGeneratedKeys()){
                if(generatedKeys.next()){
                    group.setId(generatedKeys.getLong(1));
                }else {
                    throw new InternalException("ProductGroup creation failed!");
                }
            }
            return group;
        } catch (SQLException e) {
            if(e.getErrorCode() == SQLiteErrorCode.SQLITE_CONSTRAINT.code){
                throw new DataConflictException("Constraint violation in group creation!");
            } else{
                throw new InternalException("Group creation failed.");
            }
        }
    }

    @Override
    public ProductGroup read(Long id) throws ExceptionWithStatusCode {
        try (PreparedStatement st = dataBase.prepareStatement(Queries.READ_GROUP)){
            st.setLong(1,id);
            try (ResultSet result = st.executeQuery()) {
                if(result.next()){
                    String name = result.getString("name");
                    String description = result.getString("description");
                    return new ProductGroup(id, name, description);
                }else{
                    throw new NoSuchGroupException(id);
                }
            }
        } catch (SQLException e) {
            throw new InternalException("Reading group failed.");
        }
    }

    @Override
    public ProductGroup update(ProductGroup group) throws ExceptionWithStatusCode {
        try (PreparedStatement st = dataBase.prepareStatement(Queries.UPDATE_GROUP)) {
            st.setString(1,group.getName());
            st.setString(2,group.getDescription());
            st.setLong(3,group.getId());
            int rowsAffected = st.executeUpdate();
            if(rowsAffected!=1){
                throw new NoSuchGroupException(group.getId());
            }
            return group;
        } catch (SQLException e) {
            if(e.getErrorCode() == SQLiteErrorCode.SQLITE_CONSTRAINT.code){
                throw new DataConflictException("Constraint violation in group update!");
            } else{
                throw new InternalException("Group update failed!");
            }
        }
    }

    @Override
    public void delete(Long id) throws ExceptionWithStatusCode {
        try (PreparedStatement st = dataBase.prepareStatement(Queries.DELETE_GROUP)) {
            st.setLong(1,id);
            int rowsAffected = st.executeUpdate();
            if(rowsAffected!=1){
                throw new NoSuchGroupException(id);
            }
        } catch (SQLException e) {
            throw new InternalException("ProductGroup deletion failed!");
        }
    }

    @Override
    public List<ProductGroup> listByCriteria(ProductGroupCriteriaQuery criteria) throws ExceptionWithStatusCode {
        try (Statement statement = dataBase.createStatement()) {
            String query = criteria.getQuery();
            List<ProductGroup> list;
            try (ResultSet result = statement.executeQuery(query)) {
                int size = result.getFetchSize();
                list = new ArrayList<>(size);
                while(result.next()){
                    long id = result.getLong("id");
                    String name = result.getString("name");
                    String description = result.getString("description");
                    list.add(new ProductGroup(id, name, description));
                }
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalException("Failed to get group list by criteria.");
        }
    }



}
