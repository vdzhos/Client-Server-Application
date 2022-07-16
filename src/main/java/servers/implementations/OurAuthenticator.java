package servers.implementations;

import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;
import utils.FailureStatusCodes;
import utils.JwtUtils;

import java.util.Map;

public class OurAuthenticator extends Authenticator {

    @Override
    public Result authenticate(HttpExchange exchange) {
        try {
            if(!exchange.getRequestMethod().equals("OPTIONS")){
                String token = exchange.getRequestHeaders().getFirst("JWT");
                if (token == null){
                    return new Failure(FailureStatusCodes.FORBIDDEN);
                }

                Map<String, String> parseJWT = JwtUtils.parseJWT(token);

                String username = parseJWT.get(JwtUtils.USERNAME_PARAM);
                String issuer = parseJWT.get(JwtUtils.ISSUER_PARAM);
                if(!issuer.equals(JwtUtils.ISSUER)) {
                    return new Failure(FailureStatusCodes.FORBIDDEN);
                } else {
                    return new Success(new HttpPrincipal(username, issuer));
                }
            } else{
                return new Success(new HttpPrincipal("", ""));
            }
        } catch(Exception e) {
            e.printStackTrace();
            Headers headers = exchange.getResponseHeaders();
            headers.add("Access-Control-Allow-Origin", "*");
            headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            headers.add("Access-Control-Allow-Headers", "Jwt");
            return new Failure(FailureStatusCodes.FORBIDDEN);
        }

    }

}
