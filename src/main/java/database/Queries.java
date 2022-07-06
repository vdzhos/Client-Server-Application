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

}
