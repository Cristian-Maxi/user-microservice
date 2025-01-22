package microservice.user.services;

import microservice.user.dtos.userDTO.UserEntityRequestDTO;
import microservice.user.dtos.userDTO.UserEntityResponseDTO;

import java.util.List;

public interface UserEntityService {
    UserEntityResponseDTO createUser(UserEntityRequestDTO userEntityRequestDTO);
    List<UserEntityResponseDTO> getAll();
}
