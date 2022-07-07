package database;

import java.sql.*;

public class DataBase {

    private static DataBase database;
    private static final String dbName = "Storage";

    private Connection conn;

    public static DataBase getInstance(){
        if(database==null){
            database = new DataBase();
        }
        return database;
    }

    private DataBase() {
        init();
    }

    private void init(){
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
            createTablesIfNotExist();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTablesIfNotExist() throws SQLException {
        Statement st = conn.createStatement();
        st.execute(Queries.ENABLE_FOREIGN_KEY);
        st.execute(Queries.CREATE_TABLE_GROUP);
        st.execute(Queries.CREATE_TABLE_PRODUCT);
        st.close();
    }

    public Statement createStatement() throws SQLException {
        return conn.createStatement();
    }

    public PreparedStatement prepareStatement(String query) throws SQLException {
        return conn.prepareStatement(query);
    }

    public PreparedStatement prepareStatement(String query, int autoGeneratedKeys) throws SQLException {
        return conn.prepareStatement(query, autoGeneratedKeys);
    }

}
