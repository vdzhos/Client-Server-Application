import model.Storage;
import utils.PacketUtils;

public class Main {

    public static void main(String[] args) {
        Storage storage = Storage.getInstance();

        for (int i = 0; i < 3; i++) {
            System.out.println(PacketUtils.generatePacket(i));
            System.out.println("\n----------------------------------------------------------\n");
        }
    }

}
