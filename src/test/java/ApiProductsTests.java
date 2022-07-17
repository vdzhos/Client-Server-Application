import database.DataBase;
import database.Queries;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.implementations.ProductService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ApiProductsTests {

    private static ProductService productService;

    private static DataBase db;

    @BeforeAll
    static void beforeAll() throws SQLException {
        productService = new ProductService();
        db = DataBase.getInstance();
        Statement st = db.createStatement();
        st.execute(Queries.DROP_TABLE_PRODUCTS);
        st.execute(Queries.DROP_TABLE_GROUPS);
        st.execute(Queries.CREATE_TABLE_GROUP);
        st.execute(Queries.CREATE_TABLE_PRODUCT);
        st.execute("INSERT INTO product_group (name, description) VALUES ('group1', 'description1')");
        st.execute("INSERT INTO product_group (name, description) VALUES ('group2', 'description1')");
        ResultSet gr1 = st.executeQuery("SELECT id FROM product_group WHERE name = 'group1'");
        gr1.next();
        long id1 = gr1.getLong("id");
        gr1.close();
        ResultSet gr2 = st.executeQuery("SELECT id FROM product_group WHERE name = 'group2'");
        gr2.next();
        long id2 = gr2.getLong("id");
        gr2.close();
        st.execute("INSERT INTO product (name, description, manufacturer, price, quantity, groupId) " +
                "VALUES ('product1','description','manufacturer',150.20,215," + id1 + ")");
        st.execute("INSERT INTO product (name, description, manufacturer, price, quantity, groupId) " +
                "VALUES ('product2','description','manufacturer',253.50,134," + id1 + ")");
        st.execute("INSERT INTO product (name, description, manufacturer, price, quantity, groupId) " +
                "VALUES ('prod3','description','manufacturer',178.99,59," + id1 + ")");
        st.execute("INSERT INTO product (name, description, manufacturer, price, quantity, groupId) " +
                "VALUES ('prod4','description','manufacturer',29.00,1566," + id2 + ")");
        st.execute("INSERT INTO product (name, description, manufacturer, price, quantity, groupId) " +
                "VALUES ('prod5','description','manufacturer',99.99,100," + id2 + ")");
        st.execute("INSERT INTO product (name, description, manufacturer, price, quantity, groupId) " +
                "VALUES ('product6','description','manufacturer',500.50,12," + id2 + ")");

        st.close();
    }

    @Test
    void test(){
        Assertions.assertTrue(true);
    }

}
