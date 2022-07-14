package controllers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.ExceptionWithStatusCode;
import exceptions.IncorrectPathException;
import exceptions.InternalException;
import model.ProductGroup;
import org.json.JSONObject;
import services.interfaces.GroupServiceInterface;
import utils.EndPoints;
import utils.HttpsUtils;
import utils.SuccessfulStatusCodes;
import utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

public class GroupController implements HttpHandler {

    private GroupServiceInterface groupService;

    public GroupController(GroupServiceInterface groupService) {
        this.groupService = groupService;
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

    private void processGET(HttpExchange exchange) throws IOException {
        String uri = exchange.getRequestURI().getPath();
        try{
            if(Pattern.matches(EndPoints.GROUPS_WITH_ID_REGEX, uri)){
                long id = HttpsUtils.getId(uri,2);
                ProductGroup group = groupService.getGroup(id);
                byte[] body = HttpsUtils.jsonResponseToBytes("group",new JSONObject(group));
                HttpsUtils.sendResponse(exchange,body, SuccessfulStatusCodes.OK);
            } else {
                throw new IncorrectPathException(uri);
            }
        } catch (ExceptionWithStatusCode e){
            HttpsUtils.sendResponse(exchange,HttpsUtils.jsonResponseToBytes("error",e.getMessage()),e.getStatusCode());
        } catch (IOException e){
            HttpsUtils.sendResponse(exchange,HttpsUtils.jsonResponseToBytes("error",e.getMessage()),InternalException.STATUS_CODE);
        }
    }

    private void processPOST(HttpExchange exchange) throws IOException {
        String uri = exchange.getRequestURI().getPath();
        try{
            if(Pattern.matches(EndPoints.GROUPS_REGEX, uri)){
                InputStream in = exchange.getRequestBody();
                String jsonString = new String(in.readAllBytes());
                JSONObject json = new JSONObject(jsonString);
                ProductGroup group = Utils.jsonToGroup(json,null);
                ProductGroup res = groupService.addGroup(group);
                byte[] body = HttpsUtils.jsonResponseToBytes("group",new JSONObject(res));
                HttpsUtils.sendResponse(exchange,body,SuccessfulStatusCodes.CREATED);
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
            if(Pattern.matches(EndPoints.GROUPS_WITH_ID_REGEX, uri)){
                long id = HttpsUtils.getId(uri, 2);
                InputStream in = exchange.getRequestBody();
                String jsonString = new String(in.readAllBytes());
                JSONObject json = new JSONObject(jsonString);
                ProductGroup group = Utils.jsonToGroup(json,id);
                ProductGroup res = groupService.updateGroup(group);
                byte[] body = HttpsUtils.jsonResponseToBytes("group", new JSONObject(res));
                HttpsUtils.sendResponse(exchange,body,SuccessfulStatusCodes.OK);
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
            if(Pattern.matches(EndPoints.GROUPS_WITH_ID_REGEX, uri)){
                long id = HttpsUtils.getId(uri, 2);
                groupService.deleteGroup(id);
                HttpsUtils.sendResponse(exchange, new byte[0],SuccessfulStatusCodes.NO_CONTENT);
            } else {
                throw new IncorrectPathException(uri);
            }
        } catch (ExceptionWithStatusCode e) {
            HttpsUtils.sendResponse(exchange,HttpsUtils.jsonResponseToBytes("error",e.getMessage()),e.getStatusCode());
        } catch (IOException e){
            HttpsUtils.sendResponse(exchange,HttpsUtils.jsonResponseToBytes("error",e.getMessage()),InternalException.STATUS_CODE);
        }
    }

}
