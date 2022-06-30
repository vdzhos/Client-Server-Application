import model.Storage;

public class Main {

    public static void main(String[] args) {
        Storage storage = Storage.getInstance();
        System.out.println(storage);
        System.out.println("\n\n=================================================\n\n");
        ReceiverInterface receiver = ReceiverImpl.getInstance();
        try {
            receiver.startReceiving();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\n\n=================================================\n\n");
        System.out.println(storage);

        System.out.println("End of main...");
    }

}
