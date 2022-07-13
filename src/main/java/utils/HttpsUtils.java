package utils;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import database.ProductCriteriaQuery;
import database.ProductCriteriaQueryBuilder;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpsUtils {

    public static Long getId(String uri, int idIndex) throws Exception{
        String[] split = Arrays.stream(uri.split("/")).filter(it -> !it.isEmpty()).toArray(String[]::new);
        if(split.length > idIndex+1) throw new Exception("Incorrect path: " + uri + "!");
        String id = split[idIndex];
        try {
            return Long.parseLong(id);
        }catch (NumberFormatException e){
            throw new Exception("Incorrect path: " + uri + "!");
        }
    }

    public static void verifyQuery(String query) throws Exception{
        boolean res = query == null || Pattern.matches("(?:&?[^=&]*=[^=&]*)*",query);
        if(!res) throw new Exception("Incorrect query: " + query + "!");
    }

    public static ProductCriteriaQuery parseQuery(String query) throws Exception {
        HashMap<String,String> params = new HashMap<>();
        if(query!=null){
            String[] split = query.split("&");
            for (String param : split) {
                String[] paramSplit = param.split("=");
                String value = paramSplit.length > 1 ? paramSplit[1] : "";
                params.put(paramSplit[0],value);
            }
        }
        ProductCriteriaQueryBuilder builder = new ProductCriteriaQueryBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.setProperty(entry.getKey(),entry.getValue());
        }
        return builder.build();
    }

    public static byte[] jsonResponseToBytes(String key, Object obj){
        JSONObject json = new JSONObject();
        json.put(key,obj);
        return json.toString().getBytes();
    }

    public static void sendResponse(HttpExchange exchange, byte[] body, int status) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(status, body.length);
        OutputStream os = exchange.getResponseBody();
        os.write(body);
        os.close();
    }

}
