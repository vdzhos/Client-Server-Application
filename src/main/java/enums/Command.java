package enums;

import java.util.HashMap;
import java.util.Map;

public enum Command {

    GET_PRODUCT_QUANTITY(0),
    DECREASE_PRODUCT_QUANTITY(1),
    INCREASE_PRODUCT_QUANTITY(2),
    ADD_PRODUCT_GROUP(3),
    ADD_PRODUCT_TO_GROUP(4),
    SET_PRODUCT_PRICE(5),
    RESPONSE_OK(6);

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
