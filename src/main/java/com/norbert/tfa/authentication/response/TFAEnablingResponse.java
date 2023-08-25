package com.norbert.tfa.authentication.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record TFAEnablingResponse(
        @JsonProperty("secret_image_uri")
        String secretImageUri,
        @JsonProperty("secret_key")
        String secretKey
) {
}
