package com.norbert.tfa.authentication.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record TFAConfirmationRequest(
        @JsonProperty("secret_key")
        String secretKey,

        @JsonProperty("code")
        String code
) {
}
