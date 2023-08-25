package com.norbert.tfa.jwt;

import com.norbert.tfa.user.User;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "jwt_token")
@Entity(name = "JwtToken")
@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class JwtToken {
    @Id
    @SequenceGenerator(
            name = "jwt_token_id_seq",
            sequenceName = "jwt_token_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "jwt_token_id_seq"
    )
    @Column(
            name = "id",
            updatable = false,
            columnDefinition = "BIGINT"
    )
    private Long id;

    @Column(
            name = "token",
            nullable = false,
            updatable = false,
            columnDefinition = "VARCHAR(194)"
    )
    private String token;

    @Column(
            name = "revoked",
            nullable = false,
            columnDefinition = "BOOLEAN"
    )
    private boolean revoked;

    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH}
    )
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "user_id_fk"),
            nullable = false,
            updatable = false,
            columnDefinition = "BIGINT"
    )
    private User user;
}
