package com.norbert.tfa.authentication.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class VerificationRequest extends LoginRequest {
    @JsonProperty("code")
    protected String code;
}
