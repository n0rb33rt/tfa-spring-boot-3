package com.norbert.tfa.authentication.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
public class LoginRequest{
        @JsonProperty("email")
        protected String email;

        @JsonProperty("password")
        protected String password;
}
