package utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {

    private static final String API_KEY = "33743677397A24432646294A404E635266556A586E5A7234753778214125442A472D4B6150645367566B59703373357638792F423F4528482B4D625165546857";

    public static final String ISSUER = "ua.com.supra.drift";
    public static final String USERNAME_PARAM = "username";
    public static final String ISSUER_PARAM = "issuer";
    public static final String JWT_HEADER_NAME = "JWT";



    private static final int TOTAL_MILLIS = 900000;

    public static String createJWT(String username) {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(API_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        JwtBuilder builder = Jwts.builder()
                .setSubject(username)
                .setIssuer(ISSUER)
                .setIssuedAt(now)
                .signWith(signatureAlgorithm, signingKey);

        if (TOTAL_MILLIS >= 0) {
            long expMillis = nowMillis + TOTAL_MILLIS;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        return builder.compact();
    }


    public static Map<String, String> parseJWT(String jwt) {

        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(API_KEY))
                .parseClaimsJws(jwt).getBody();

        Map<String, String> parsedJwt = new HashMap<>();
        parsedJwt.put(USERNAME_PARAM, claims.getSubject());
        parsedJwt.put(ISSUER_PARAM, claims.getIssuer());

        return parsedJwt;
    }

}
