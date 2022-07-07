package repositories.implementations;

import database.DataBase;
import database.Queries;
import model.ProductGroup;
import repositories.interfaces.GroupRepositoryInterface;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GroupRepository implements GroupRepositoryInterface {

    private DataBase dataBase;

    public GroupRepository() {
        dataBase = DataBase.getInstance();
    }

    @Override
    public ProductGroup create(ProductGroup group) throws Exception{
        try (PreparedStatement st = dataBase.prepareStatement(Queries.CREATE_GROUP, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1,group.getName());
            int rowsAffected = st.executeUpdate();
            if(rowsAffected!=1){
                throw new Exception("ProductGroup creation failed");
            }
            try (ResultSet generatedKeys = st.getGeneratedKeys()){
                if(generatedKeys.next()){
                    group.setId(generatedKeys.getLong(1));
                }else {
                    throw new Exception("ProductGroup creation failed!");
                }
            }
            return group;
        } catch (SQLException e) {
            throw new Exception("ProductGroup creation failed!");
        }
    }

    @Override
    public ProductGroup read(Long id) throws Exception {
        try (PreparedStatement st = dataBase.prepareStatement(Queries.READ_GROUP)){
            st.setLong(1,id);
            try (ResultSet result = st.executeQuery()) {
                if(result.next()){
                    String name = result.getString("name");
                    return new ProductGroup(id,name);
                }else{
                    throw new Exception("ProductGroup read failed");
                }
            }
        } catch (SQLException e) {
            throw new Exception("ProductGroup read failed");
        }
    }

    @Override
    public ProductGroup update(ProductGroup group) throws Exception {
        try (PreparedStatement st = dataBase.prepareStatement(Queries.UPDATE_GROUP)) {
            st.setString(1,group.getName());
            st.setLong(2,group.getId());
            int rowsAffected = st.executeUpdate();
            if(rowsAffected!=1){
                throw new Exception("ProductGroup update failed");
            }
            return group;
        } catch (SQLException e) {
            throw new Exception("ProductGroup update failed!");
        }
    }

    @Override
    public void delete(Long id) throws Exception {
        try (PreparedStatement st = dataBase.prepareStatement(Queries.DELETE_GROUP)) {
            st.setLong(1,id);
            int rowsAffected = st.executeUpdate();
            if(rowsAffected!=1){
                throw new Exception("ProductGroup deletion failed");
            }
        } catch (SQLException e) {
            throw new Exception("ProductGroup deletion failed!");
        }
    }

}
