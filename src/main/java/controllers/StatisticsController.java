package controllers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.ExceptionWithStatusCode;
import exceptions.IncorrectPathException;
import exceptions.InternalException;
import exceptions.NoSuchGroupException;
import org.json.JSONObject;
import services.interfaces.ProductServiceInterface;
import utils.EndPoints;
import utils.HttpsUtils;
import utils.SuccessStatusCodes;

import java.io.IOException;
import java.util.regex.Pattern;

public class StatisticsController implements HttpHandler {

    private ProductServiceInterface productService;

    public StatisticsController(ProductServiceInterface productService) {
        this.productService = productService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if(method.equals("GET")){
            processGET(exchange);
        } else if(method.equals("OPTIONS")){
            processOPTIONS(exchange);
        } else{
            throw new RuntimeException("There is no such method handler!");
        }
    }

    private void processGET(HttpExchange exchange) throws IOException{
        String uri = exchange.getRequestURI().getPath();
        try{
            if(Pattern.matches(EndPoints.TOTAL_PRICE_REGEX, uri)){
                getTotalPrice(exchange, null);
            }else if(Pattern.matches(EndPoints.TOTAL_PRICE_WITH_ID_REGEX, uri)){
                getTotalPrice(exchange, uri);
            } else {
                throw new IncorrectPathException(uri);
            }
        } catch (ExceptionWithStatusCode e){
            HttpsUtils.sendResponse(exchange,HttpsUtils.jsonResponseToBytes("error",e.getMessage()),e.getStatusCode());
        } catch (IOException e){
            HttpsUtils.sendResponse(exchange,HttpsUtils.jsonResponseToBytes("error",e.getMessage()), InternalException.STATUS_CODE);
        }
    }

    private void getTotalPrice(HttpExchange exchange, String uri) throws ExceptionWithStatusCode, IOException {
        Long id = null;
        if(uri != null) {
            id = HttpsUtils.getId(uri, 3);
        }
        double totalPrice = productService.getTotalPrice(id);
        byte[] body = HttpsUtils.jsonResponseToBytes("total_price", totalPrice);
        HttpsUtils.sendResponse(exchange, body, SuccessStatusCodes.OK);
    }

    private void processOPTIONS(HttpExchange exchange) throws IOException {
        byte[] body = (new JSONObject()).toString().getBytes();
        HttpsUtils.sendResponse(exchange,body,SuccessStatusCodes.OK);
    }

}
