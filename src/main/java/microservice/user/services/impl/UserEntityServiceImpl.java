package microservice.user.services.impl;

import jakarta.persistence.EntityNotFoundException;
import microservice.user.dtos.userDTO.UserEntityRequestDTO;
import microservice.user.dtos.userDTO.UserEntityResponseDTO;
import microservice.user.enums.RoleEnum;
import microservice.user.exceptions.ApplicationException;
import microservice.user.mappers.UserEntityMapper;
import microservice.user.models.UserEntity;
import microservice.user.repositories.UserEntityRepository;
import microservice.user.services.UserEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserEntityServiceImpl implements UserEntityService {

    @Autowired
    private UserEntityMapper userEntityMapper;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Override
    public UserEntityResponseDTO createUser(UserEntityRequestDTO userEntityRequestDTO) {
        if (userEntityRepository.existsByEmail(userEntityRequestDTO.email())) {
            throw new ApplicationException("email", "El email ya existe en la base de datos");
        }
        UserEntity userEntity = userEntityMapper.toEntity(userEntityRequestDTO);
        userEntity.setRole(RoleEnum.USER);
        userEntityRepository.save(userEntity);
        return userEntityMapper.toResponseDTO(userEntity);
    }

    @Override
    public List<UserEntityResponseDTO> getAll() {
        List<UserEntity> userEntities = userEntityRepository.findAll();
        return userEntityMapper.toResponseListDTO(userEntities);
    }

    @Override
    public boolean existById(Long id) {
        return userEntityRepository.existsById(id);
    }

    @Override
    public Long findIdByEmail(String email) {
        UserEntity userEntity = userEntityRepository.findByEmail(email);
        if (userEntity == null) {
            throw new EntityNotFoundException("Usuario con email " + email + " no encontrado.");
        }
        return userEntity.getId();
    }
}