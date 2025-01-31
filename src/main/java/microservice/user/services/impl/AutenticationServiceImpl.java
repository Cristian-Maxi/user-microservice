package microservice.user.services.impl;

import microservice.user.dtos.userDTO.DatosAutenticacionUsuario;
import microservice.user.dtos.userDTO.JWTTokenDTO;
import microservice.user.services.AutenticationService;
import microservice.user.services.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AutenticationServiceImpl implements AutenticationService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AutenticationServiceImpl(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @Override
    public JWTTokenDTO autenticar(DatosAutenticacionUsuario datosAutenticacionUsuario) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(datosAutenticacionUsuario.email(), datosAutenticacionUsuario.password())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenService.generateToken(authentication);
        return new JWTTokenDTO(token);
    }
}
