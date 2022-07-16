package utils;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import database.*;
import exceptions.IncorrectPathException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class HttpsUtils {

    public static Long getId(String uri, int idIndex) throws IncorrectPathException {
        String[] split = Arrays.stream(uri.split("/")).filter(it -> !it.isEmpty()).toArray(String[]::new);
        if(split.length > idIndex+1)
            throw new IncorrectPathException(uri);
        String id = split[idIndex];
        try {
            return Long.parseLong(id);
        }catch (NumberFormatException e){
            throw new IncorrectPathException(uri);
        }
    }

    public static void verifyQuery(String query) throws IncorrectPathException {
        boolean res = query == null || Pattern.matches("(?:&?[^=&]*=[^=&]*)*",query);
        if(!res)
            throw new IncorrectPathException(query);
    }

    public static ProductCriteriaQuery parseProductQuery(String query) throws IncorrectPathException {
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
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.setProperty(entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
            throw new IncorrectPathException(query);
        }
        return builder.build();
    }

    public static ProductGroupCriteriaQuery parseGroupQuery(String query) throws IncorrectPathException {
        HashMap<String,String> params = new HashMap<>();
        if(query!=null){
            String[] split = query.split("&");
            for (String param : split) {
                String[] paramSplit = param.split("=");
                String value = paramSplit.length > 1 ? paramSplit[1] : "";
                params.put(paramSplit[0],value);
            }
        }
        ProductGroupCriteriaQueryBuilder builder = new ProductGroupCriteriaQueryBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.setProperty(entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
            throw new IncorrectPathException(query);
        }
        return builder.build();
    }

    public static byte[] jsonResponseToBytes(String key, Object obj){
        JSONObject json = new JSONObject();
        json.put(key,obj);
        return json.toString().getBytes();
    }

    public static void sendResponse(HttpExchange exchange, byte[] body, int status) throws IOException {
        int length = body.length;
        Headers headers = exchange.getResponseHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        headers.add("Access-Control-Allow-Headers", "Jwt");

        if (length > 0) {
            headers.add("Content-Type", "application/json");
            exchange.sendResponseHeaders(status, body.length);
            OutputStream os = exchange.getResponseBody();
            os.write(body);
            os.close();
        } else {
            exchange.sendResponseHeaders(status, -1);
        }
    }

}
