package lu.oop.server.api.utils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.function.Function;


@Component
public class JwtUtil {


    private String secretKey;

    @Autowired
    JwtUtil(Environment env) {
        secretKey = env.getProperty("jwt.secret");
    }


    public Long extractId(String token) {
        return Long.valueOf(extractClaim(token, Claims::getSubject));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        Date expiration = extractExpiration(token);
        if(expiration == null) {
            return false;
        }
        return expiration.before(new Date());
    }

    public String generateToken(Long id) {
        return Jwts.builder()
                .setSubject(id.toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                // Currently there is no need for expiration, set it when it is needed
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Boolean validateToken(String token) {
        final Long extractedId = extractId(token);
        // Currently we only need to check if the token had an that is not null
        return !isTokenExpired(token) && extractedId != null;
    }
}
