package repositories.implementations;

import database.DataBase;
import repositories.interfaces.GroupRepositoryInterface;

public class GroupRepository implements GroupRepositoryInterface {

    private DataBase dataBase;

    public GroupRepository() {
        dataBase = DataBase.getInstance();
    }

}
