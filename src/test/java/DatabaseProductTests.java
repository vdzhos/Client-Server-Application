import database.DataBase;
import database.ProductCriteriaQuery;
import database.ProductCriteriaQueryBuilder;
import database.Queries;
import model.Product;
import model.ProductGroup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositories.implementations.GroupRepository;
import repositories.implementations.ProductRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseProductTests {

    private static ProductRepository productRepository;
    private static GroupRepository groupRepository;

    private static DataBase db;

    @BeforeAll
    static void beforeAll() {
        productRepository = new ProductRepository();
        groupRepository = new GroupRepository();
        db = DataBase.getInstance();
    }

    @BeforeEach
    void beforeEach() throws SQLException {
        Statement st = db.createStatement();
        st.execute(Queries.DELETE_ALL_FROM_PRODUCT);
        st.execute(Queries.DELETE_ALL_FROM_GROUP);
        st.execute("INSERT INTO product_group (name) VALUES ('group1')");
        st.execute("INSERT INTO product_group (name) VALUES ('group2')");
        ResultSet gr1 = st.executeQuery("SELECT id FROM product_group WHERE name = 'group1'");
        gr1.next();
        long id1 = gr1.getLong("id");
        gr1.close();
        ResultSet gr2 = st.executeQuery("SELECT id FROM product_group WHERE name = 'group2'");
        gr2.next();
        long id2 = gr2.getLong("id");
        gr2.close();
        st.execute("INSERT INTO product (name, price, quantity, groupId) " +
                "VALUES ('product1',150.20,215," + id1 + ")");
        st.execute("INSERT INTO product (name, price, quantity, groupId) " +
                "VALUES ('product2',253.50,134," + id1 + ")");
        st.execute("INSERT INTO product (name, price, quantity, groupId) " +
                "VALUES ('prod3',178.99,59," + id1 + ")");
        st.execute("INSERT INTO product (name, price, quantity, groupId) " +
                "VALUES ('prod4',29.00,1566," + id2 + ")");
        st.execute("INSERT INTO product (name, price, quantity, groupId) " +
                "VALUES ('prod5',99.99,100," + id2 + ")");
        st.execute("INSERT INTO product (name, price, quantity, groupId) " +
                "VALUES ('product6',500.50,12," + id2 + ")");

        st.close();
    }

    @Test
    void testListByCriteriaWithParams() {
        try (Statement st = db.createStatement()) {
            long id1;
            try (ResultSet gr1 = st.executeQuery("SELECT id FROM product_group WHERE name = 'group1'")) {
                gr1.next();
                id1 = gr1.getLong("id");
            }
            long id2;
            try (ResultSet gr2 = st.executeQuery("SELECT id FROM product_group WHERE name = 'group2'")) {
                gr2.next();
                id2 = gr2.getLong("id");
            }
            List<Long> groupIds = new ArrayList<>();
            groupIds.add(id1);
            groupIds.add(id2);
            ProductCriteriaQuery params = new ProductCriteriaQueryBuilder()
                    .setTextInName("product")
                    .setGroupIds(groupIds)
                    .setUpperPrice(400.00)
                    .build();
            System.out.println(params.getQuery());
            List<Product> result = productRepository.listByCriteria(params);
            Assertions.assertEquals(2, result.size());
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    void testListByCriteriaWithoutParams() {
        ProductCriteriaQuery params = new ProductCriteriaQueryBuilder()
                .build();
        System.out.println(params.getQuery());
        List<Product> result = null;
        try {
            result = productRepository.listByCriteria(params);
        } catch (Exception e) {
            Assertions.fail();
        }
        Assertions.assertEquals(6, result.size());
    }

    private Product createProductWithName(String name) {
        double price = 10.2;
        int quantity = 10;
        long groupId = 1;
        String groupName = "group1";
        try (Statement st = db.createStatement()) {
            try (ResultSet gr1 = st.executeQuery("SELECT id FROM product_group WHERE name = '" + groupName + "'")) {
                gr1.next();
                groupId = gr1.getLong("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Product(null, name, price, quantity, groupId);
    }

    private Long getProductIdByName(String name) {
        Long id = null;
        try (Statement st = db.createStatement()) {
            try (ResultSet gr1 = st.executeQuery("SELECT id FROM product WHERE name = '" + name + "'")) {
                gr1.next();
                id = gr1.getLong("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    @Test
    void testCreateProductSuccess() {
        String name = "product7";
        Product pr = createProductWithName(name);

        try {
            Product product = productRepository.create(pr);
            Assertions.assertEquals(product.getName(), name);
            Assertions.assertNotEquals(null, product.getId());
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    void testCreateProductFailure() {
        String name = "product1";
        Product pr = createProductWithName(name);

        try {
            productRepository.create(pr);
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    void testReadProductSuccess() {
        String name = "product1";
        Long id = getProductIdByName(name);

        try {
            Product product = productRepository.read(id);
            Assertions.assertEquals(id, product.getId());
            Assertions.assertEquals(name, product.getName());
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    void testReadProductFailure() {
        try {
            productRepository.read(-1L);
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    void testUpdateProductSuccess() {
        String name = "product1";
        Long id = getProductIdByName(name);
        try {
            Product product = productRepository.read(id);
            product.setName("asfgdsdfhhjhg");
            Product updated = productRepository.update(product);
            Assertions.assertEquals(product, updated);
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    void testUpdateProductFailure() {
        String name = "product1";
        Long id = getProductIdByName(name);
        try {
            Product product = productRepository.read(id);
            product.setId(-1L);
            product.setName("asfgdsdfhhjhg");
            productRepository.update(product);
        } catch (Exception e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    void testDeleteProductSuccess() {
        String name = "product1";
        Long id = getProductIdByName(name);

        try {
            productRepository.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail();
        }

        try {
            productRepository.read(id);
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    void testDeleteProductFailure() {
        try {
            productRepository.delete(-1L);
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertTrue(true);
        }
    }

}
