package microservice.user.dtos.userDTO;

import jakarta.validation.constraints.NotBlank;

public record DatosAutenticacionUsuario(
        @NotBlank(message = "El email no debe estar vacio")
        String email,
        @NotBlank(message = "El password no debe estar vacio")
        String password
) {
}
