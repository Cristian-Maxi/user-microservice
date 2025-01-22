package microservice.user.controllers;

import jakarta.validation.Valid;
import microservice.user.dtos.ApiResponseDTO;
import microservice.user.dtos.userDTO.UserEntityRequestDTO;
import microservice.user.dtos.userDTO.UserEntityResponseDTO;
import microservice.user.enums.RoleEnum;
import microservice.user.exceptions.ApplicationException;
import microservice.user.services.UserEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}