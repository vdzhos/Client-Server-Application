package database;

public class Queries {

    public static final String ENABLE_FOREIGN_KEY = "PRAGMA foreign_keys = ON;";

    public static final String CREATE_TABLE_PRODUCT =
            "create table if not exists product" +
            "(" +
            "id integer primary key autoincrement," +
            "name text not null unique," +
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
            "name text not null unique" +
            ");";

    public static final String CREATE_PRODUCT = "insert into product (name, price, quantity, groupId) values(?, ?, ?, ?)";
    public static final String READ_PRODUCT = "select * from product where id = ?";
    public static final String UPDATE_PRODUCT = "update product set name = ?, price = ?, quantity = ?, groupId = ? where id = ?";
    public static final String DELETE_PRODUCT = "delete from product where id = ?";

    public static final String GET_PRODUCT_QUANTITY = "select quantity from product where id = ?";
    public static final String CHANGE_PRODUCT_QUANTITY = "update product set quantity = quantity + ? where id = ?";
    public static final String SET_PRODUCT_PRICE = "update product set price = ? where id = ?";


    public static final String DELETE_ALL_FROM_PRODUCT = "delete from product";

    public static final String DELETE_ALL_FROM_GROUP = "delete from product_group";

    public static final String CREATE_GROUP = "insert into product_group (name) values (?);";

    public static final String READ_GROUP = "select * from product_group where id = ?;";

    public static final String UPDATE_GROUP = "update product_group set name = ? where id = ?;";

    public static final String DELETE_GROUP = "delete from product_group where id = ?;";

}
