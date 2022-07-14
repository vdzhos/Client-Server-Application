package utils;

import model.Product;
import org.json.JSONException;
import org.json.JSONObject;

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

    public static Product jsonToProduct(JSONObject json, boolean idNecessary) throws Exception {
        Long id = null;
        try{
            id = json.getLong("id");
        }catch (JSONException e){
            if(idNecessary){
                throw new Exception("Id for product is wrong/missing!");
            }
        }
        try{
            String name = json.getString("name");
            Double price = json.getDouble("price");
            Integer quantity = json.getInt("quantity");
            Long groupId = json.getLong("groupId");
            return new Product(id,name,price,quantity,groupId);
        }catch (JSONException e){
            throw new Exception("Necessary data is wrong/missing!");
        }
    }

}
