package com.norbert.tfa.exception;

import lombok.Builder;

import java.time.ZonedDateTime;
@Builder
public record ApiException(
        Integer status,
        String error,
        String message,
        String path,
        ZonedDateTime timestamp
) {
}
