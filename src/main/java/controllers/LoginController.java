package controllers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsExchange;
import exceptions.ExceptionWithStatusCode;
import exceptions.InternalException;
import model.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;
import repositories.implementations.UserRepository;
import repositories.interfaces.UserRepositoryInterface;
import utils.FailureStatusCodes;
import utils.HttpsUtils;
import utils.JwtUtils;
import utils.SuccessStatusCodes;

import java.io.IOException;
import java.io.InputStream;

public class LoginController implements HttpHandler {

    private UserRepositoryInterface userRepository;

    public LoginController (UserRepositoryInterface userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if(exchange.getRequestMethod().equals("POST")) {
            processLogin(exchange);
        } else {
            throw new RuntimeException("There is no such method handler!");
        }
    }

    private void processLogin(HttpExchange exchange) throws IOException {
        try {
            InputStream in = exchange.getRequestBody();
            String jsonString = new String(in.readAllBytes());
            JSONObject json = new JSONObject(jsonString);


            String username = json.getString("username");
            String password = json.getString("password");

            String hashedPassword = DigestUtils.md5Hex(password);

            User user = userRepository.getUserByUsername(username);
            if(user.getPassword().equals(hashedPassword)) {
                Headers headers = exchange.getResponseHeaders();
                headers.add("Access-Control-Expose-Headers", "Jwt");
                headers.add(JwtUtils.JWT_HEADER_NAME, JwtUtils.createJWT(username));
                HttpsUtils.sendResponse(exchange, new byte[0], SuccessStatusCodes.OK);
            } else {
                byte[] bytes = HttpsUtils.jsonResponseToBytes("error", "Incorrect username or password.");
                HttpsUtils.sendResponse(exchange, bytes, FailureStatusCodes.FORBIDDEN);
            }
        } catch (ExceptionWithStatusCode e) {
            HttpsUtils.sendResponse(exchange,HttpsUtils.jsonResponseToBytes("error",e.getMessage()),e.getStatusCode());
        } catch (IOException e) {
            HttpsUtils.sendResponse(exchange,HttpsUtils.jsonResponseToBytes("error",e.getMessage()), InternalException.STATUS_CODE);
        }


    }

}
