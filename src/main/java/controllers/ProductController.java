package controllers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.ExceptionWithStatusCode;
import exceptions.IncorrectPathException;
import exceptions.InternalException;
import model.Product;
import org.json.JSONObject;
import services.interfaces.ProductServiceInterface;
import utils.EndPoints;
import utils.HttpsUtils;
import utils.SuccessStatusCodes;
import utils.Utils;

import java.io.IOException;
import java.io.InputStream;
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
        } else if(method.equals("OPTIONS")){
            processOPTIONS(exchange);
        } else{
            throw new RuntimeException("There is no such method handler!");
        }
    }

    private void processGET(HttpExchange exchange) throws IOException{
        String uri = exchange.getRequestURI().getPath();
        try{
            if(Pattern.matches(EndPoints.PRODUCTS_REGEX, uri)){
                getProductsByCriteria(exchange);
            }else if(Pattern.matches(EndPoints.PRODUCTS_WITH_ID_REGEX, uri)){
                getSingleProduct(exchange, uri);
            } else {
                throw new IncorrectPathException(uri);
            }
        } catch (ExceptionWithStatusCode e){
            HttpsUtils.sendResponse(exchange,HttpsUtils.jsonResponseToBytes("error",e.getMessage()),e.getStatusCode());
        } catch (IOException e){
            HttpsUtils.sendResponse(exchange,HttpsUtils.jsonResponseToBytes("error",e.getMessage()), InternalException.STATUS_CODE);
        }
    }

    private void getSingleProduct(HttpExchange exchange, String uri) throws ExceptionWithStatusCode, IOException {
        long id = HttpsUtils.getId(uri,2);
        Product product = productService.getProduct(id);
        byte[] body = HttpsUtils.jsonResponseToBytes("product",new JSONObject(product));
        HttpsUtils.sendResponse(exchange,body, SuccessStatusCodes.OK);
    }

    private void getProductsByCriteria(HttpExchange exchange) throws ExceptionWithStatusCode, IOException {
        String query = exchange.getRequestURI().getQuery();
        HttpsUtils.verifyQuery(query);
        List<Product> list = productService.listProductsByCriteria(HttpsUtils.parseQuery(query));
        byte[] body = HttpsUtils.jsonResponseToBytes("products",list);
        HttpsUtils.sendResponse(exchange,body, SuccessStatusCodes.OK);
    }

    private void processPOST(HttpExchange exchange) throws IOException {
        String uri = exchange.getRequestURI().getPath();
        try{
            if(Pattern.matches(EndPoints.PRODUCTS_REGEX, uri)){
                InputStream in = exchange.getRequestBody();
                String jsonString = new String(in.readAllBytes());
                JSONObject json = new JSONObject(jsonString);
                Product product = Utils.jsonToProduct(json,null);
                Product res = productService.addProduct(product);
                byte[] body = HttpsUtils.jsonResponseToBytes("product",new JSONObject(res));
                HttpsUtils.sendResponse(exchange,body, SuccessStatusCodes.CREATED);
            } else {
                throw new IncorrectPathException(uri);
            }
        } catch (ExceptionWithStatusCode e){
            HttpsUtils.sendResponse(exchange,HttpsUtils.jsonResponseToBytes("error",e.getMessage()),e.getStatusCode());
        } catch (IOException e){
            HttpsUtils.sendResponse(exchange,HttpsUtils.jsonResponseToBytes("error",e.getMessage()),InternalException.STATUS_CODE);
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
                Product product = Utils.jsonToProduct(json,id);
                Product res = productService.updateProduct(product);
                byte[] body = HttpsUtils.jsonResponseToBytes("product", new JSONObject(res));
                HttpsUtils.sendResponse(exchange,body, SuccessStatusCodes.OK);
            } else {
                throw new IncorrectPathException(uri);
            }
        } catch (ExceptionWithStatusCode e) {
            HttpsUtils.sendResponse(exchange,HttpsUtils.jsonResponseToBytes("error",e.getMessage()),e.getStatusCode());
        } catch (IOException e){
            HttpsUtils.sendResponse(exchange,HttpsUtils.jsonResponseToBytes("error",e.getMessage()),InternalException.STATUS_CODE);
        }
    }

    private void processDELETE(HttpExchange exchange) throws IOException {
        String uri = exchange.getRequestURI().getPath();
        try{
            if(Pattern.matches(EndPoints.PRODUCTS_WITH_ID_REGEX, uri)){
                long id = HttpsUtils.getId(uri, 2);
                productService.deleteProduct(id);
                HttpsUtils.sendResponse(exchange, new byte[0], SuccessStatusCodes.NO_CONTENT);
            } else {
                throw new IncorrectPathException(uri);
            }
        } catch (ExceptionWithStatusCode e) {
            HttpsUtils.sendResponse(exchange,HttpsUtils.jsonResponseToBytes("error",e.getMessage()),e.getStatusCode());
        } catch (IOException e){
            HttpsUtils.sendResponse(exchange,HttpsUtils.jsonResponseToBytes("error",e.getMessage()),InternalException.STATUS_CODE);
        }
    }

    private void processOPTIONS(HttpExchange exchange) throws IOException {
        byte[] body = (new JSONObject()).toString().getBytes();
        HttpsUtils.sendResponse(exchange,body,SuccessStatusCodes.OK);
    }

}
