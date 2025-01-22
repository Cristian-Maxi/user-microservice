package microservice.user.dtos.userDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserEntityRequestDTO(
        @Email @NotBlank(message = "El email es obligatorio")
        String email,
        @NotBlank(message = "El username es obligatorio")
        String username,
        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
        String password
){
}