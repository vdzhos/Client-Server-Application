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

public class DataBaseTests {

    private static ProductRepository productRepository;
    private static GroupRepository groupRepository;

    private static DataBase db;

    @BeforeAll
    static void beforeAll(){
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
                "VALUES ('product1',150.20,215,"+id1+")");
        st.execute("INSERT INTO product (name, price, quantity, groupId) " +
                "VALUES ('product2',253.50,134,"+id1+")");
        st.execute("INSERT INTO product (name, price, quantity, groupId) " +
                "VALUES ('prod3',178.99,59,"+id1+")");
        st.execute("INSERT INTO product (name, price, quantity, groupId) " +
                "VALUES ('prod4',29.00,1566,"+id2+")");
        st.execute("INSERT INTO product (name, price, quantity, groupId) " +
                "VALUES ('prod5',99.99,100,"+id2+")");
        st.execute("INSERT INTO product (name, price, quantity, groupId) " +
                "VALUES ('product6',500.50,12,"+id2+")");

        st.close();
    }

    @Test
    void testListByCriteriaWithParams() {
        try (Statement st = db.createStatement()){
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
            Assertions.assertEquals(2,result.size());
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    void testListByCriteriaWithoutParams(){
        ProductCriteriaQuery params = new ProductCriteriaQueryBuilder()
                .build();
        System.out.println(params.getQuery());
        List<Product> result = null;
        try {
            result = productRepository.listByCriteria(params);
        } catch (Exception e) {
            Assertions.fail();
        }
        Assertions.assertEquals(6,result.size());
    }

    @Test
    void testCreateGroupSuccess(){
        String name = "group3";
        ProductGroup pg = new ProductGroup(null,name);
        try {
            ProductGroup group = groupRepository.create(pg);
            Assertions.assertEquals(group.getName(),name);
            Assertions.assertNotEquals(null,group.getId());
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    void testCreateGroupFailure(){
        String name = "group2";
        ProductGroup pg = new ProductGroup(null,name);
        try {
            ProductGroup group = groupRepository.create(pg);
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    void testReadGroupSuccess() {
        try (Statement st = db.createStatement()) {
            String name = "group1";
            long id1;
            try (ResultSet gr1 = st.executeQuery("SELECT id FROM product_group WHERE name = '"+name+"'")) {
                gr1.next();
                id1 = gr1.getLong("id");
            }
            try {
                ProductGroup group = groupRepository.read(id1);
                Assertions.assertEquals(id1,group.getId());
                Assertions.assertEquals(name,group.getName());
            } catch (Exception e) {
                Assertions.fail();
            }
        } catch (SQLException e) {
            Assertions.fail();
        }
    }

    @Test
    void testReadGroupFailure(){
        try {
            ProductGroup group = groupRepository.read(-1L);
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    void testUpdateGroupSuccess() {
        try (Statement st = db.createStatement()) {
            String name = "group1";
            long id1;
            try (ResultSet gr1 = st.executeQuery("SELECT id FROM product_group WHERE name = '"+name+"'")) {
                gr1.next();
                id1 = gr1.getLong("id");
            }
            try {
                ProductGroup group = new ProductGroup(id1,"123123123");
                ProductGroup updated = groupRepository.update(group);
                Assertions.assertEquals(group,updated);
            } catch (Exception e) {
                Assertions.fail();
            }
        } catch (SQLException e) {
            Assertions.fail();
        }
    }

    @Test
    void testUpdateGroupFailure(){
        try {
            ProductGroup group = new ProductGroup(-1L,"123123123");
            ProductGroup updated = groupRepository.update(group);
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    void testDeleteGroupFailedForeignKeyViolation() {
        try (Statement st = db.createStatement()) {
            String name = "group1";
            long id1;
            try (ResultSet gr1 = st.executeQuery("SELECT id FROM product_group WHERE name = '"+name+"'")) {
                gr1.next();
                id1 = gr1.getLong("id");
            }
            try {
                groupRepository.delete(id1);
                try (ResultSet groupSize = st.executeQuery("SELECT COUNT(*) FROM product_group")) {
                    groupSize.next();
                    long size = groupSize.getLong(1);
                    Assertions.fail();
                }
            } catch (Exception e) {
                Assertions.assertTrue(true);
            }
        } catch (SQLException e) {
            Assertions.fail();
        }
    }

    @Test
    void testDeleteGroupFailedNoSuchGroup() {
        try (Statement st = db.createStatement()) {
            st.execute(Queries.DELETE_ALL_FROM_PRODUCT);
            groupRepository.delete(-1L);
            try (ResultSet groupSize = st.executeQuery("SELECT COUNT(*) FROM product_group")) {
                groupSize.next();
                long size = groupSize.getLong(1);
                Assertions.fail();
            }
        } catch (Exception e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    void testDeleteGroupSuccess() {
        try (Statement st = db.createStatement()){
            String name = "group1";
            long id1;
            try (ResultSet gr1 = st.executeQuery("SELECT id FROM product_group WHERE name = '"+name+"'")) {
                gr1.next();
                id1 = gr1.getLong("id");
            }
            try {
                st.execute(Queries.DELETE_ALL_FROM_PRODUCT);
                groupRepository.delete(id1);
                try (ResultSet groupSize = st.executeQuery("SELECT COUNT(*) FROM product_group")) {
                    groupSize.next();
                    long size = groupSize.getLong(1);
                    Assertions.assertEquals(1,size);
                }
            } catch (Exception e) {
                Assertions.fail();
            }
        } catch (SQLException e) {
            Assertions.fail();
        }
    }


}
