CREATE TABLE "user"(
                       id BIGSERIAL,
                       email VARCHAR(319) NOT NULL,
                       password VARCHAR(61) NOT NULL,
                       role VARCHAR(6) NOT NULL,
                       first_name VARCHAR(25) NOT NULL,
                       last_name VARCHAR(25) NOT NULL,
                       tfa_enabled BOOLEAN NOT NULL,
                       tfa_secret VARCHAR(33),
                       CONSTRAINT unique_email UNIQUE(email),
                       CONSTRAINT check_email CHECK(email ~ '^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$'),
                       CONSTRAINT check_role CHECK ( role IN ('USER','ADMIN') ),
                       PRIMARY KEY(id)
);
CREATE TABLE jwt_token(
                          id BIGSERIAL,
                          token VARCHAR(255) NOT NULL,
                          revoked BOOLEAN NOT NULL,
                          user_id BIGINT,
                          CONSTRAINT unique_token UNIQUE (token),
                          CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES "user"(id),
                          PRIMARY KEY(id)
);