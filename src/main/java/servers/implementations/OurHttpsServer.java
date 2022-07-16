package servers.implementations;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;
import controllers.GroupController;
import controllers.LoginController;
import controllers.ProductController;
import controllers.StatisticsController;
import repositories.implementations.UserRepository;
import repositories.interfaces.UserRepositoryInterface;
import services.implementations.GroupService;
import services.implementations.ProductService;
import utils.EndPoints;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.util.concurrent.Executors;

public class OurHttpsServer {

    private HttpsServer server;

    public OurHttpsServer() throws Exception {

        server = HttpsServer.create(new InetSocketAddress(8080),0);
        SSLContext sslContext = SSLContext.getInstance("TLS");

        // initialise the keystore
        char[] keyStorePassword = "123123".toCharArray();
        KeyStore ks = KeyStore.getInstance("JKS");
        FileInputStream fis = new FileInputStream("localhost.jks");
        ks.load(fis, keyStorePassword);

        // setup the key manager factory
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, keyStorePassword);

        // setup the trust manager factory
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);

        // setup the HTTPS context and parameters
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        server.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
            public void configure(HttpsParameters params) {
                try {
                    // initialise the SSL context
                    SSLContext context = getSSLContext();
                    SSLEngine engine = context.createSSLEngine();
                    params.setNeedClientAuth(false);
                    params.setCipherSuites(engine.getEnabledCipherSuites());
                    params.setProtocols(engine.getEnabledProtocols());

                    // Set the SSL parameters
                    SSLParameters sslParameters = context.getSupportedSSLParameters();
                    params.setSSLParameters(sslParameters);

                } catch (Exception ex) {
                    System.out.println("Failed to create HTTPS port");
                }
            }
        });

        ProductService productService = new ProductService();
        GroupService groupService = new GroupService();
        UserRepositoryInterface userRepository = new UserRepository();

        OurAuthenticator authenticator = new OurAuthenticator();

        HttpContext c1 = server.createContext(EndPoints.PRODUCTS,new ProductController(productService));
        c1.setAuthenticator(authenticator);
        HttpContext c2 = server.createContext(EndPoints.GROUPS,new GroupController(groupService));
        c2.setAuthenticator(authenticator);
        HttpContext c3 = server.createContext(EndPoints.STATISTICS,new StatisticsController(productService));
        c3.setAuthenticator(authenticator);
        server.createContext(EndPoints.LOGIN,new LoginController(userRepository));

        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
    }

    public static void main(String[] args) throws Exception {
        new OurHttpsServer();
        System.out.println("Server started!");
    }

}
