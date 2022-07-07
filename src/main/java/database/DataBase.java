package database;

import java.sql.*;

public class DataBase {

    public static DataBase database;
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
//        st.execute("INSERT INTO product_group (name) VALUES ('123123')");
//        ResultSet res = st.executeQuery("SELECT * FROM product_group;");
//        System.out.println(res.next());
        st.close();
    }

    public Statement createStatement() throws SQLException {
        return conn.createStatement();
    }

    public PreparedStatement prepareStatement(String query) throws SQLException {
        return conn.prepareStatement(query);
    }


}
