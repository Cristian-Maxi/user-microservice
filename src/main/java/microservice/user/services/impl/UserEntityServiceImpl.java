package microservice.user.services.impl;

import jakarta.persistence.EntityNotFoundException;
import microservice.user.config.RabbitMQConfig;
import microservice.user.dtos.userDTO.UserEntityRequestDTO;
import microservice.user.dtos.userDTO.UserEntityResponseDTO;
import microservice.user.dtos.userDTO.UserEntityUpdateDTO;
import microservice.user.enums.RoleEnum;
import microservice.user.exceptions.ApplicationException;
import microservice.user.mappers.UserEntityMapper;
import microservice.user.models.UserEntity;
import microservice.user.repositories.UserEntityRepository;
import microservice.user.services.UserEntityService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import utils.UserRegisteredEvent;

import java.util.List;

@Service
public class UserEntityServiceImpl implements UserEntityService {

    @Autowired
    private UserEntityMapper userEntityMapper;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserEntityResponseDTO createUser(UserEntityRequestDTO userEntityRequestDTO) {
        if (userEntityRepository.existsByEmail(userEntityRequestDTO.email())) {
            throw new ApplicationException("email", "El email ya existe en la base de datos");
        }
        String encodedPassword = passwordEncoder.encode(userEntityRequestDTO.password());
        UserEntity userEntity = userEntityMapper.toEntity(userEntityRequestDTO);
        userEntity.setPassword(encodedPassword);
        userEntity.setRole(userEntity.getEmail().contains("@admin") ? RoleEnum.ADMIN : RoleEnum.USER);
        userEntityRepository.save(userEntity);

        UserRegisteredEvent event = new UserRegisteredEvent(userEntity.getEmail(), userEntity.getUsername());
        amqpTemplate.convertAndSend(RabbitMQConfig.USER_EXCHANGE, RabbitMQConfig.USER_ROUTING_KEY, event);

        return userEntityMapper.toResponseDTO(userEntity);
    }

    @Override
    public List<UserEntityResponseDTO> getAll() {
        List<UserEntity> userEntities = userEntityRepository.findAll();
        return userEntityMapper.toResponseListDTO(userEntities);
    }

    @Override
    public UserEntityResponseDTO update(UserEntityUpdateDTO userEntityUpdateDTO) {
        UserEntity userEntity = userEntityRepository.findById(userEntityUpdateDTO.id())
                .orElseThrow(() -> new EntityNotFoundException("El ID del usuario no encontrado"));
        if(userEntityUpdateDTO.username() != null && !userEntityUpdateDTO.username().isBlank()) {
            userEntity.setUsername(userEntityUpdateDTO.username());
        }
        userEntityRepository.save(userEntity);
        return userEntityMapper.toResponseDTO(userEntity);
    }

    @Override
    public void delete(Long id) {
        UserEntity userEntity = userEntityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el ID del usuario ingresado"));
        userEntityRepository.delete(userEntity);
    }

    @Override
    public boolean existById(Long id) {
        return userEntityRepository.existsById(id);
    }

    @Override
    public Long findIdByEmail(String email) {
        UserEntity userEntity = userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuario con email " + email + " no encontrado."));
        return userEntity.getId();
    }
}