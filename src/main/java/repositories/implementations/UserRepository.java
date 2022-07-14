package repositories.implementations;

import database.DataBase;
import database.ProductCriteriaQuery;
import database.Queries;
import exceptions.*;
import model.Product;
import model.User;
import org.sqlite.SQLiteErrorCode;
import repositories.interfaces.ProductRepositoryInterface;
import repositories.interfaces.UserRepositoryInterface;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements UserRepositoryInterface {

    private DataBase dataBase;

    public UserRepository() {
        dataBase = DataBase.getInstance();
    }


    @Override
    public User getUserByUsername(String username) throws ExceptionWithStatusCode {
        try (PreparedStatement preparedStatement = dataBase.prepareStatement(Queries.GET_USER)) {

            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    String name = resultSet.getString("name");
                    String password = resultSet.getString("password");
                    return new User(id, name, password);
                } else
                    throw new NoSuchUserException(username);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalException("User read failed.");
        }
    }
}
