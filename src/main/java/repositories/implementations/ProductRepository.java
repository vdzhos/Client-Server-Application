package repositories.implementations;

import database.DataBase;
import database.ProductCriteriaQuery;
import exceptions.DataConflictException;
import exceptions.InternalException;
import exceptions.ExceptionWithStatusCode;
import exceptions.NoSuchProductException;
import model.Product;
import database.Queries;
import org.sqlite.SQLiteErrorCode;
import repositories.interfaces.ProductRepositoryInterface;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository implements ProductRepositoryInterface {

    private DataBase dataBase;

    public ProductRepository() {
        dataBase = DataBase.getInstance();
    }

    @Override
    public List<Product> listByCriteria(ProductCriteriaQuery criteria) throws ExceptionWithStatusCode {
        try (Statement statement = dataBase.createStatement()) {
            String query = criteria.getQuery();
            List<Product> list;
            try (ResultSet result = statement.executeQuery(query)) {
                int size = result.getFetchSize();
                list = new ArrayList<>(size);
                while(result.next()){
                    long id = result.getLong("id");
                    String name = result.getString("name");
                    String description = result.getString("description");
                    String manufacturer = result.getString("manufacturer");
                    double price = result.getDouble("price");
                    int quantity = result.getInt("quantity");
                    long groupId = result.getLong("groupId");
                    String groupName = result.getString("group_name");
                    list.add(new Product(id,name,description,manufacturer,price,quantity,groupId,groupName));
                }
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalException("Failed to get list by criteria.");
        }
    }

    @Override
    public Product create(Product product) throws ExceptionWithStatusCode {
        try (PreparedStatement preparedStatement = dataBase.prepareStatement(Queries.CREATE_PRODUCT, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getDescription());
            preparedStatement.setString(3, product.getManufacturer());
            preparedStatement.setDouble(4, product.getPrice());
            preparedStatement.setInt(5, product.getQuantity());
            preparedStatement.setLong(6, product.getGroupId());

            int insertedRows = preparedStatement.executeUpdate();
            if (insertedRows != 1)
                throw new InternalException("Product creation failed.");

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next())
                    product.setId(generatedKeys.getLong(1));
                else
                    throw new InternalException("Product creation failed.");
            }

        } catch (SQLException e) {
            if(e.getErrorCode() == SQLiteErrorCode.SQLITE_CONSTRAINT.code){
                throw new DataConflictException("Constraint violation in product creation!");
            } else{
                throw new InternalException("Product creation failed.");
            }
        }

        return product;
    }

    @Override
    public Product read(Long id) throws ExceptionWithStatusCode {
        try (PreparedStatement preparedStatement = dataBase.prepareStatement(Queries.READ_PRODUCT)) {

            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Long resId = resultSet.getLong("id");
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    String manufacturer = resultSet.getString("manufacturer");
                    Double price = resultSet.getDouble("price");
                    Integer quantity = resultSet.getInt("quantity");
                    Long groupId = resultSet.getLong("groupId");
                    return new Product(resId, name, description, manufacturer, price, quantity, groupId);
                } else
                    throw new NoSuchProductException(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalException("Reading product failed.");
        }
    }

    @Override
    public Product update(Product product) throws ExceptionWithStatusCode {
        try (PreparedStatement preparedStatement = dataBase.prepareStatement(Queries.UPDATE_PRODUCT)) {

            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getDescription());
            preparedStatement.setString(3, product.getManufacturer());
            preparedStatement.setDouble(4, product.getPrice());
            preparedStatement.setInt(5, product.getQuantity());
            preparedStatement.setLong(6, product.getGroupId());
            preparedStatement.setLong(7, product.getId());

            int updatedRows = preparedStatement.executeUpdate();
            if (updatedRows != 1)
                throw new NoSuchProductException(product.getId());

        } catch (SQLException e) {
            e.printStackTrace();
            if(e.getErrorCode() == SQLiteErrorCode.SQLITE_CONSTRAINT.code){
                throw new DataConflictException("Constraint violation in product update!");
            } else{
                throw new InternalException("Updating product failed.");
            }
        }

        return product;
    }

    @Override
    public void delete(Long id) throws ExceptionWithStatusCode {
        try (PreparedStatement preparedStatement = dataBase.prepareStatement(Queries.DELETE_PRODUCT)) {

            preparedStatement.setLong(1, id);

            int deletedRows = preparedStatement.executeUpdate();
            if (deletedRows != 1)
                throw new NoSuchProductException(id);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalException("Deleting product failed.");
        }
    }

    @Override
    public int getQuantity(Long id) throws ExceptionWithStatusCode {
        try (PreparedStatement preparedStatement = dataBase.prepareStatement(Queries.GET_PRODUCT_QUANTITY)) {

            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next())
                    return resultSet.getInt(1);
                else
                    throw new InternalException("Cannot get product quantity.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalException("Cannot get product quantity.");
        }
    }

    @Override
    public int decreaseQuantity(Long id, int quantity) throws ExceptionWithStatusCode {
        try{
            final boolean oldAutoCommit = dataBase.getAutoCommit();
            final int oldIsolation = dataBase.getIsolationLevel();
            dataBase.setIsolationLevel(Connection.TRANSACTION_SERIALIZABLE);
            dataBase.setAutoCommit(false);
            try (PreparedStatement preparedStatement = dataBase.prepareStatement(Queries.CHANGE_PRODUCT_QUANTITY)) {

                preparedStatement.setInt(1, -quantity);
                preparedStatement.setLong(2, id);

                int updatedRows = preparedStatement.executeUpdate();
                if (updatedRows != 1)
                    throw new InternalException("Cannot decrease product quantity.");

                int newQuantity = getQuantity(id);
                if(newQuantity<0) throw new DataConflictException("Resulting quantity cannot be negative!");

                return newQuantity;
            } catch (SQLException e) {
                e.printStackTrace();
                dataBase.rollback();
                throw new InternalException("Cannot decrease product quantity.");
            } finally {
                dataBase.commit();
                dataBase.setAutoCommit(oldAutoCommit);
                dataBase.setIsolationLevel(oldIsolation);
            }
        }catch (SQLException e){
            throw new InternalException("Cannot increase product quantity.");
        }
    }

    @Override
    public int increaseQuantity(Long id, int quantity) throws ExceptionWithStatusCode {
        try{
            final boolean oldAutoCommit = dataBase.getAutoCommit();
            final int oldIsolation = dataBase.getIsolationLevel();
            dataBase.setIsolationLevel(Connection.TRANSACTION_SERIALIZABLE);
            dataBase.setAutoCommit(false);
            try (PreparedStatement preparedStatement = dataBase.prepareStatement(Queries.CHANGE_PRODUCT_QUANTITY)) {

                preparedStatement.setInt(1, quantity);
                preparedStatement.setLong(2, id);

                int updatedRows = preparedStatement.executeUpdate();
                if (updatedRows != 1)
                    throw new InternalException("Cannot increase product quantity.");

                return getQuantity(id);
            } catch (SQLException e) {
                e.printStackTrace();
                dataBase.rollback();
                throw new InternalException("Cannot increase product quantity.");
            } finally {
                dataBase.commit();
                dataBase.setAutoCommit(oldAutoCommit);
                dataBase.setIsolationLevel(oldIsolation);
            }
        } catch (SQLException e){
            throw new InternalException("Cannot increase product quantity.");
        }
    }

    @Override
    public double updatePrice(Long id, double price) throws Exception {
        final boolean oldAutoCommit = dataBase.getAutoCommit();
        dataBase.setAutoCommit(false);
        try (PreparedStatement preparedStatement = dataBase.prepareStatement(Queries.SET_PRODUCT_PRICE)) {

            preparedStatement.setDouble(1, price);
            preparedStatement.setLong(2, id);

            int updatedRows = preparedStatement.executeUpdate();
            if (updatedRows != 1)
                throw new Exception("Cannot set product price.");

            return read(id).getPrice();
        } catch (SQLException e) {
            e.printStackTrace();
            dataBase.rollback();
            throw new Exception("Cannot set product price.");
        } finally {
            dataBase.commit();
            dataBase.setAutoCommit(oldAutoCommit);
        }
    }

    @Override
    public double getTotalPrice(Long id) throws InternalException {
        try (PreparedStatement preparedStatement = dataBase.prepareStatement(id == null ? Queries.GET_TOTAL_PRODUCT_PRICE
                : Queries.GET_TOTAL_PRODUCT_PRICE_BY_GROUP_ID)) {

            if(id!=null) {
                preparedStatement.setLong(1, id);
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble(1);
                } else
                    throw new InternalException("Total product price getting failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalException("Total product price getting failed.");
        }
    }

}
