package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Invalid email format")
    private String email;

    @Column(nullable = false, name = "disabledByAdmin")
    private boolean isDisabledByAdmin = false;

    @OneToMany(mappedBy = "user")
    private List<UserLogin> userLogin;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private boolean deActive = false;

    @Column(nullable = false)
    private boolean isGlobalAdmin = false;

    @Column(nullable = false, name = "enable Registration")
    private boolean isEnabledByRegistration = false;

    @OneToMany(mappedBy = "user")
    private List<Article> article;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private VerificationToken verificationToken;

    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    @Column(name = "reset_password_token_expiry")
    private LocalDateTime resetPasswordTokenExpiry;

}
