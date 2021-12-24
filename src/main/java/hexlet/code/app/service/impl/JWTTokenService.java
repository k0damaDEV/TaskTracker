package hexlet.code.app.service.impl;

import hexlet.code.app.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.impl.compression.GzipCompressionCodec;
import liquibase.pro.packaged.D;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class JWTTokenService implements TokenService, Clock {

    private final String secretKey;
    private final String issuer;
    private final Long expirationSec;
    private final Long clockSkewSec;

    public JWTTokenService(@Value("${jwt.issuer:prj_5}") final String issuer,
                           @Value("${jwt.expiration-sec:86400}") final Long expirationSec,
                           @Value("${jwt.clock-skew-sec:300}") final Long clockSkewSec,
                           @Value("${jwt.secret:secret}")final String secret) {
        this.secretKey = TextCodec.BASE64.encode(secret);
        this.issuer = issuer;
        this.expirationSec = expirationSec;
        this.clockSkewSec = clockSkewSec;
    }


    @Override
    public String expiring(Map<String, Object> attributes) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compressWith(new GzipCompressionCodec())
                .setClaims(getClaims(attributes, expirationSec))
                .compact();
    }

    @Override
    public Map<String, Object> verify(String token) {
        return Jwts.parser()
                .requireIssuer(issuer)
                .setClock(this)
                .setAllowedClockSkewSeconds(clockSkewSec)
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public Date now() {
        return new Date();
    }

    private Claims getClaims(final Map<String, Object> attributes, final Long expiresInSec) {
        final Claims claims = Jwts.claims();
        claims.setIssuer(issuer);
        claims.setIssuedAt(now());
        claims.putAll(attributes);

        if (expiresInSec > 0) {
            claims.setExpiration(new Date(System.currentTimeMillis() + expiresInSec * 1000));
        }
        return claims;
    }
}
