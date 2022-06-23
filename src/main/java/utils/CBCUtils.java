package utils;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.NoSuchAlgorithmException;

public class CBCUtils {

    public static SecretKey key;
    private static final byte[] IV = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    public static IvParameterSpec ivSpec;

    static {
        try {
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(128);
            key = generator.generateKey();
            ivSpec = new IvParameterSpec(IV);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

}
