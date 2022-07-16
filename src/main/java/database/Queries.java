package database;

public class Queries {

    public static final String ENABLE_FOREIGN_KEY = "PRAGMA foreign_keys = ON;";

    public static final String CREATE_TABLE_PRODUCT =
            "create table if not exists product" +
            "(" +
            "id integer primary key autoincrement," +
            "name text not null unique," +
            "description text not null," +
            "manufacturer text not null," +
            "price real not null," +
            "quantity integer not null," +
            "groupId integer not null," +
            "foreign key (groupId)" +
            "   references product_group (id)" +
            "       on update cascade" +
            "       on delete restrict" +
            ");";

    public static final String CREATE_TABLE_GROUP =
            "create table if not exists product_group" +
            "(" +
            "id integer primary key autoincrement," +
            "name text not null unique," +
            "description text not null" +
            ");";

    public static final String CREATE_TABLE_USER =
            "create table if not exists user" +
            "(" +
            "id integer primary key autoincrement," +
            "name text not null unique," +
            "password text not null" +
            ");";

    public static final String CREATE_PRODUCT = "insert into product (name, description, manufacturer, price, quantity, groupId) values(?, ?, ?, ?, ?, ?)";
    public static final String READ_PRODUCT = "select * from product where id = ?";
    public static final String UPDATE_PRODUCT = "update product set name = ?, description = ?, manufacturer = ?, price = ?, quantity = ?, groupId = ? where id = ?";
    public static final String DELETE_PRODUCT = "delete from product where id = ?";

    public static final String GET_PRODUCT_QUANTITY = "select quantity from product where id = ?";
    public static final String CHANGE_PRODUCT_QUANTITY = "update product set quantity = quantity + ? where id = ?";
    public static final String SET_PRODUCT_PRICE = "update product set price = ? where id = ?";

    public static final String GET_PRODUCTS_BY_CRITERIA_JOIN =
            "select product.id, product.name, product.description, product.manufacturer, product.price, " +
                "product.quantity, product.groupId, product_group.name as group_name " +
            "from product inner join product_group on product.groupId = product_group.id";

    public static final String DELETE_ALL_FROM_PRODUCT = "delete from product";

    public static final String DELETE_ALL_FROM_GROUP = "delete from product_group";

    public static final String CREATE_GROUP = "insert into product_group (name, description) values (?, ?);";

    public static final String READ_GROUP = "select * from product_group where id = ?;";

    public static final String UPDATE_GROUP = "update product_group set name = ?, description = ? where id = ?;";

    public static final String DELETE_GROUP = "delete from product_group where id = ?;";

    public static final String GET_USER = "select * from user where name = ?;";

}
