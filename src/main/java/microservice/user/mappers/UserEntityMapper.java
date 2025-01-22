package microservice.user.mappers;

import microservice.user.dtos.userDTO.UserEntityRequestDTO;
import microservice.user.dtos.userDTO.UserEntityResponseDTO;
import microservice.user.models.UserEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserEntityMapper {
    public UserEntity toEntity(UserEntityRequestDTO userEntityRequestDTO) {
        return new UserEntity(
                userEntityRequestDTO.email(),
                userEntityRequestDTO.password(),
                userEntityRequestDTO.username()
        );
    }

    public UserEntityResponseDTO toResponseDTO(UserEntity user) {
        return new UserEntityResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getRole()
        );
    }

    public List<UserEntityResponseDTO> toResponseListDTO(List<UserEntity> userEntityList) {
        return userEntityList.stream()
                .map(this::toResponseDTO)
                .toList();
    }
}