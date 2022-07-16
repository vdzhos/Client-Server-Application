package utils;

import exceptions.DataConflictException;
import model.Product;
import model.ProductGroup;
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

    public static Product jsonToProduct(JSONObject json, Long id) throws DataConflictException {
        try{
            String name = json.getString("name");
            String description = json.getString("description");
            String manufacturer = json.getString("manufacturer");
            Double price = json.getDouble("price");
            Integer quantity = json.getInt("quantity");
            Long groupId = json.getLong("groupId");
            return new Product(id,name,description,manufacturer,price,quantity,groupId);
        }catch (JSONException e){
            throw new DataConflictException("Necessary data is wrong/missing!");
        }
    }

    public static int jsonGetQuantity(JSONObject json) throws DataConflictException {
        try{
            return json.getInt("quantity");
        }catch (JSONException e){
            throw new DataConflictException("Necessary data is wrong/missing!");
        }
    }

    public static ProductGroup jsonToGroup(JSONObject json, Long id) throws DataConflictException {
        try{
            String name = json.getString("name");
            String description = json.getString("description");
            return new ProductGroup(id, name, description);
        }catch (JSONException e){
            throw new DataConflictException("Necessary data is wrong/missing!");
        }
    }

}
