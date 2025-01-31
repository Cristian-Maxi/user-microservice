package microservice.user.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public interface TokenService {
    String generateToken(Authentication authentication);
    String getUsernameFromToken(String token);
    boolean validateToken(String token, UserDetails userDetails);
}