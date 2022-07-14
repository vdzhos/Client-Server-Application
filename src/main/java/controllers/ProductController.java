package controllers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Product;
import org.json.JSONObject;
import services.interfaces.ProductServiceInterface;
import utils.EndPoints;
import utils.HttpsUtils;
import utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.regex.Pattern;

public class ProductController implements HttpHandler {

    private ProductServiceInterface productService;

    public ProductController(ProductServiceInterface productService) {
        this.productService = productService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if(method.equals("GET")){
            processGET(exchange);
        } else if(method.equals("POST")){
            processPOST(exchange);
        } else if(method.equals("PUT")){
            processPUT(exchange);
        } else if(method.equals("DELETE")){
            processDELETE(exchange);
        } else {
            throw new RuntimeException("There is no such method handler!");
        }
    }

    private void processGET(HttpExchange exchange) throws IOException{
        URI uri = exchange.getRequestURI();
        try{
            if(Pattern.matches(EndPoints.PRODUCTS + "[/ ]*",uri.getPath())){
                getProductsByCriteria(exchange);
            }else {
                //TODO("400 response status if wrong path")
                getSingleProduct(exchange, uri);
            }
        }catch (IOException e){
            HttpsUtils.sendResponse(exchange,HttpsUtils.jsonResponseToBytes("error",e.getMessage()),500);
        }catch (Exception e){
            HttpsUtils.sendResponse(exchange,HttpsUtils.jsonResponseToBytes("error",e.getMessage()),404);
        }
    }

    private void getSingleProduct(HttpExchange exchange, URI uri) throws Exception {
        long id = HttpsUtils.getId(uri.getPath(),2);
        Product product = productService.getProduct(id);
        byte[] body = HttpsUtils.jsonResponseToBytes("product",new JSONObject(product));
        HttpsUtils.sendResponse(exchange,body,200);
    }

    private void getProductsByCriteria(HttpExchange exchange) throws Exception {
        String query = exchange.getRequestURI().getQuery();
        HttpsUtils.verifyQuery(query);
        List<Product> list = productService.listProductsByCriteria(HttpsUtils.parseQuery(query));
        byte[] body = HttpsUtils.jsonResponseToBytes("products",list);
        HttpsUtils.sendResponse(exchange,body,200);
    }

    private void processPOST(HttpExchange exchange) throws IOException {
        String uri = exchange.getRequestURI().getPath();
        try{
            if(Pattern.matches(EndPoints.PRODUCTS + "[/ ]*",uri)){
                InputStream in = exchange.getRequestBody();
                String jsonString = new String(in.readAllBytes());
                JSONObject json = new JSONObject(jsonString);
                Product product = Utils.jsonToProduct(json,false);
                Product res = productService.addProduct(product);
                byte[] body = HttpsUtils.jsonResponseToBytes("product",new JSONObject(res));
                HttpsUtils.sendResponse(exchange,body,200);
            } else {
                //TODO("400 response status if wrong path")
            }
        }catch (IOException e){
            HttpsUtils.sendResponse(exchange,HttpsUtils.jsonResponseToBytes("error",e.getMessage()),500);
        }catch (Exception e){
            System.out.println(e.getMessage());
            HttpsUtils.sendResponse(exchange,HttpsUtils.jsonResponseToBytes("error",e.getMessage()),409);
        }
    }

    private void processPUT(HttpExchange exchange) {

    }

    private void processDELETE(HttpExchange exchange) {

    }

}
