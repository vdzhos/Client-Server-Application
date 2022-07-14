package controllers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.IncorrectPathException;
import exceptions.NoSuchProductException;
import model.Product;
import org.json.JSONObject;
import services.interfaces.ProductServiceInterface;
import utils.EndPoints;
import utils.HttpsUtils;
import utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
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
        String uri = exchange.getRequestURI().getPath();
        try{
            if(Pattern.matches(EndPoints.PRODUCTS_REGEX, uri)){
                getProductsByCriteria(exchange);
            }else {
                getSingleProduct(exchange, uri);
            }
        } catch (IncorrectPathException e){
            HttpsUtils.sendResponse(exchange,HttpsUtils.jsonResponseToBytes("error",e.getMessage()),400);
        } catch (NoSuchProductException e){
            HttpsUtils.sendResponse(exchange,HttpsUtils.jsonResponseToBytes("error",e.getMessage()),404);
        } catch (Exception e){
            HttpsUtils.sendResponse(exchange,HttpsUtils.jsonResponseToBytes("error",e.getMessage()),500);
        }
    }

    private void getSingleProduct(HttpExchange exchange, String uri) throws IncorrectPathException, IOException, NoSuchProductException {
        long id = HttpsUtils.getId(uri,2);
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
            if(Pattern.matches(EndPoints.PRODUCTS_REGEX, uri)){
                InputStream in = exchange.getRequestBody();
                String jsonString = new String(in.readAllBytes());
                JSONObject json = new JSONObject(jsonString);
                Product product = Utils.jsonToProduct(json,false);
                Product res = productService.addProduct(product);
                byte[] body = HttpsUtils.jsonResponseToBytes("product",new JSONObject(res));
                HttpsUtils.sendResponse(exchange,body,201);
            } else {
                throw new IncorrectPathException(uri);
            }
        } catch (IncorrectPathException e){
            HttpsUtils.sendResponse(exchange,HttpsUtils.jsonResponseToBytes("error",e.getMessage()),400);
        } catch (IOException e){
            HttpsUtils.sendResponse(exchange,HttpsUtils.jsonResponseToBytes("error",e.getMessage()),500);
        } catch (Exception e){
            System.out.println(e.getMessage());
            HttpsUtils.sendResponse(exchange,HttpsUtils.jsonResponseToBytes("error",e.getMessage()),409);
        }
    }

    private void processPUT(HttpExchange exchange) throws IOException {
        String uri = exchange.getRequestURI().getPath();
        try{
            if(Pattern.matches(EndPoints.PRODUCTS_WITH_ID_REGEX, uri)){
                long id = HttpsUtils.getId(uri, 2);
                InputStream in = exchange.getRequestBody();
                String jsonString = new String(in.readAllBytes());
                JSONObject json = new JSONObject(jsonString);
                Product product = Utils.jsonToProduct(json,false);
                product.setId(id);
                Product res = productService.updateProduct(product);
                byte[] body = HttpsUtils.jsonResponseToBytes("product", new JSONObject(res));
                HttpsUtils.sendResponse(exchange,body,200);
            } else {
                throw new IncorrectPathException(uri);
            }
        } catch (IncorrectPathException e) {
            HttpsUtils.sendResponse(exchange,HttpsUtils.jsonResponseToBytes("error",e.getMessage()),400);
        } catch (NoSuchProductException e) {
            HttpsUtils.sendResponse(exchange,HttpsUtils.jsonResponseToBytes("error",e.getMessage()),404);
        } catch (IOException e){
            HttpsUtils.sendResponse(exchange,HttpsUtils.jsonResponseToBytes("error",e.getMessage()),500);
        } catch (Exception e){
            System.out.println(e.getMessage());
            HttpsUtils.sendResponse(exchange,HttpsUtils.jsonResponseToBytes("error",e.getMessage()),409);
        }
    }

    private void processDELETE(HttpExchange exchange) throws IOException {
        String uri = exchange.getRequestURI().getPath();
        try{
            if(Pattern.matches(EndPoints.PRODUCTS_WITH_ID_REGEX, uri)){
                long id = HttpsUtils.getId(uri, 2);
                productService.deleteProduct(id);
                HttpsUtils.sendResponse(exchange, new byte[0],204);
            } else {
                throw new IncorrectPathException(uri);
            }
        } catch (IncorrectPathException e) {
            HttpsUtils.sendResponse(exchange,HttpsUtils.jsonResponseToBytes("error",e.getMessage()),400);
        } catch (NoSuchProductException e) {
            HttpsUtils.sendResponse(exchange,HttpsUtils.jsonResponseToBytes("error",e.getMessage()),404);
        } catch (Exception e){
            HttpsUtils.sendResponse(exchange,HttpsUtils.jsonResponseToBytes("error",e.getMessage()),500);
        }
    }

}
