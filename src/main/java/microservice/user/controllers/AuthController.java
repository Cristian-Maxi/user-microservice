package microservice.user.controllers;

import jakarta.validation.Valid;
import microservice.user.dtos.userDTO.DatosAutenticacionUsuario;
import microservice.user.dtos.userDTO.JWTTokenDTO;
import microservice.user.dtos.userDTO.UserEntityRequestDTO;
import microservice.user.dtos.userDTO.UserEntityResponseDTO;
import microservice.user.exceptions.ApplicationException;
import microservice.user.services.AutenticationService;
import microservice.user.services.UserEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserEntityService userEntityService;

    @Autowired
    private AutenticationService autenticationService;

    @PostMapping("/create")
    public ResponseEntity<UserEntityResponseDTO> createUserEntity(@Valid @RequestBody UserEntityRequestDTO userEntityRequestDTO) {
        try{
            UserEntityResponseDTO userEntityResponseDTO = userEntityService.createUser(userEntityRequestDTO);
            return new ResponseEntity<>(userEntityResponseDTO, HttpStatus.CREATED);
        } catch (ApplicationException e) {
            throw new ApplicationException(" Ha ocurrido un error en el campo " + e.getCampo() + ", Descripcion: "+e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<JWTTokenDTO> autenticar(@RequestBody @Valid DatosAutenticacionUsuario datosAutenticacionUsuario){
        JWTTokenDTO jwtTokenDTO = autenticationService.autenticar(datosAutenticacionUsuario);
        return ResponseEntity.ok(jwtTokenDTO);
    }
}
