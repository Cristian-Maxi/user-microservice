package microservice.user.services.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import microservice.user.models.UserEntity;
import microservice.user.repositories.UserEntityRepository;
import microservice.user.security.variablesEnv.SecretKeyConfig;
import microservice.user.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private SecretKeyConfig secretKeyConfig;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Override
    public String generateToken(Authentication authentication) {
        try{
            String username = authentication.getName();

            UserEntity userEntity = userEntityRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

            List<String> authorities = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            System.out.println("Generando token...");
            Algorithm algorithm = Algorithm.HMAC256(secretKeyConfig.getSECRET_KEY());
            return JWT.create()
                    .withIssuer("ToDo List")
                    .withSubject(username)
                    .withClaim("id", userEntity.getId())
                    .withClaim("authorities", authorities)
                    .withExpiresAt(Date.from(generateExpirationDate()))
                    .sign(algorithm);

        }catch (JWTCreationException e){
            throw new RuntimeException("Error al crear el token");
        }
    }

    @Override
    public String getUsernameFromToken(String token) {
        if(token == null){
            throw new RuntimeException("Token nulo");
        }
        DecodedJWT verifier;
        try{
            Algorithm algorithm = Algorithm.HMAC256(secretKeyConfig.getSECRET_KEY());
            verifier = JWT.require(algorithm)
                    .withIssuer("ToDo List")
                    .build()
                    .verify(token);
            verifier.getSubject();
        }catch (JWTCreationException e){
            throw new RuntimeException("Error al verificar el token");
        }
        if (verifier.getSubject() == null){
            throw new RuntimeException("Verificador invalido");
        }
        return verifier.getSubject();
    }

    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-05:00"));
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getExpiresAt();
    }
}
