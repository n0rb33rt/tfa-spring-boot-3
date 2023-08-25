package com.norbert.tfa.user;

import com.norbert.tfa.jwt.JwtToken;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Table(name = "\"user\"")
@Entity(name = "User")
@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @SequenceGenerator(
            name = "user_id_seq",
            sequenceName = "user_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_id_seq"
    )
    @Column(
            name = "id",
            updatable = false,
            columnDefinition = "BIGINT"
    )
    private Long id;

    @Column(
            name = "first_name",
            length = 24,
            nullable = false,
            columnDefinition = "VARCHAR(25)"
    )
    private String firstName;

    @Column(
            name = "last_name",
            length = 24,
            nullable = false,
            columnDefinition = "VARCHAR(25)"
    )
    private String lastName;

    @Column(
            name = "email",
            length = 318,
            nullable = false,
            columnDefinition = "VARCHAR(319)"
    )
    private String email;

    @Column(
            name = "password",
            length = 60,
            nullable = false,
            columnDefinition = "VARCHAR(61)"
    )
    private String password;

    @Column(
            name = "tfa_enabled",
            nullable = false,
            columnDefinition = "BOOLEAN"
    )
    private boolean tfaEnabled;

    @Column(
            name = "tfa_secret",
            length = 32,
            columnDefinition = "VARCHAR(33)"
    )
    private String tfaSecret;

    @Column(
            name = "role",
            nullable = false,
            length = 5,
            columnDefinition = "VARCHAR(6)"
    )
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private List<JwtToken> jwtTokens;
}
