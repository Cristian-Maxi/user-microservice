package microservice.user.dtos.userDTO;

import microservice.user.enums.RoleEnum;

public record UserEntityResponseDTO(
        Long id,
        String email,
        String username,
        RoleEnum role
) {
}
