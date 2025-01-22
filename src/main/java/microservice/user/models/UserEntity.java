package microservice.user.models;

import jakarta.persistence.*;
import lombok.*;
import microservice.user.enums.RoleEnum;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "user_entity")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;
    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    public UserEntity(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }
}