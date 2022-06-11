package enums;

public enum Command {

    CREATE(0),
    UPDATE(1),
    DELETE(2);

    public final int code;

    Command(int code){
        this.code = code;
    }

}
