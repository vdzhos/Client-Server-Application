package servers.implementations;

import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;

public class OurAuthenticator extends Authenticator {

    @Override
    public Result authenticate(HttpExchange exch) {
        return new Success(new HttpPrincipal("",""));
    }

}
