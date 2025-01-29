package microservice.user.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import microservice.user.dtos.ApiResponseDTO;
import microservice.user.dtos.userDTO.UserEntityRequestDTO;
import microservice.user.dtos.userDTO.UserEntityResponseDTO;
import microservice.user.dtos.userDTO.UserEntityUpdateDTO;
import microservice.user.enums.RoleEnum;
import microservice.user.exceptions.ApplicationException;
import microservice.user.models.UserEntity;
import microservice.user.services.UserEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserEntityController {

    @Autowired
    private UserEntityService userEntityService;

    @PostMapping("/create")
    public ResponseEntity<UserEntityResponseDTO> createUserEntity(@Valid @RequestBody UserEntityRequestDTO userEntityRequestDTO) {
        try{
            UserEntityResponseDTO userEntityResponseDTO = userEntityService.createUser(userEntityRequestDTO);
            return new ResponseEntity<>(userEntityResponseDTO, HttpStatus.CREATED);
        } catch (ApplicationException e) {
            throw new ApplicationException(" Ha ocurrido un error en el campo " + e.getCampo() + ", Descripcion: "+e.getMessage());
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<ApiResponseDTO<UserEntityResponseDTO>> updateUserEntity(@Valid @RequestBody UserEntityUpdateDTO userEntityUpdateDTO) {
        UserEntityResponseDTO userEntityResponseDTO = userEntityService.update(userEntityUpdateDTO);
        String message = "Cliente Actualizado";
        return new ResponseEntity<>(new ApiResponseDTO<>(true, message, userEntityResponseDTO), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUserEntity(@PathVariable Long id) {
        userEntityService.delete(id);
        String message = "Usuario Eliminado exitosamente";
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<ApiResponseDTO<UserEntityResponseDTO>> getAllUsuarios() {
        try {
            List<UserEntityResponseDTO> userEntityResponseDTO = userEntityService.getAll();
            if (userEntityResponseDTO.isEmpty()) {
                return new ResponseEntity<>(new ApiResponseDTO<>(false, "No Hay Usuarios Registrados", userEntityResponseDTO), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponseDTO<>(true, "Usuarios Registrados", userEntityResponseDTO), HttpStatus.OK);
            }
        } catch (ApplicationException e) {
            throw new ApplicationException(" Ha ocurrido un error " + e.getMessage());
        }
    }

    @GetMapping("/getAllRoles")
    public ResponseEntity<ApiResponseDTO<RoleEnum>> getAllRoles() {
        List<RoleEnum> roles = Arrays.asList(RoleEnum.values());
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Roles disponibles", roles));
    }

    @GetMapping("/validateUser/{id}")
    public boolean validateUser(@PathVariable Long id) {
        boolean userExist = userEntityService.existById(id);
        if (userExist) {
            return userExist;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
    }

    @GetMapping("/validateUserByEmail/{email}")
    public Long validateUserByEmail(@PathVariable String email) {
        try {
            return userEntityService.findIdByEmail(email);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}