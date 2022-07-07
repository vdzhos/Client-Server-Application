package repositories.implementations;

import database.DataBase;
import repositories.interfaces.ProductRepositoryInterface;

public class ProductRepository implements ProductRepositoryInterface {

    private DataBase dataBase;

    public ProductRepository() {
        dataBase = DataBase.getInstance();
    }



}
