package microservice.user.services;


import microservice.user.dtos.userDTO.DatosAutenticacionUsuario;
import microservice.user.dtos.userDTO.JWTTokenDTO;

public interface AutenticationService {

    JWTTokenDTO autenticar(DatosAutenticacionUsuario datosAutenticacionUsuario);
}
