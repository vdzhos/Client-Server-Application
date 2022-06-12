package enums;

import java.util.HashMap;
import java.util.Map;

public enum Command {

    CREATE(0),
    UPDATE(1),
    DELETE(2);

    public final int code;

    Command(int code){
        this.code = code;
    }

    private static final Map<Integer, Command> map = new HashMap<>();

    static {
        for (Command command : Command.values()) {
            map.put(command.code, command);
        }
    }

    public static Command valueOf(int code) {
        return map.get(code);
    }

    public int getCode() {
        return code;
    }


}
