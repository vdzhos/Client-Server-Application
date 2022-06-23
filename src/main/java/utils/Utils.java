package utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.Set;

public class Utils {

    public static String getRandomStringFromSet(Set<String> names){
        int size = names.size();
        int item = new Random().nextInt(size);
        int i = 0;
        for(String name : names) {
            if (i++ == item) {
                return name;
            }
        }
        return ""; //never happens
    }

    public static double getRandomDouble(int max, int precision){
        double price = new Random().nextDouble()*max;
        return BigDecimal.valueOf(price)
                .setScale(precision, RoundingMode.HALF_UP)
                .doubleValue();
    }

}
