package com.valentinpopescu.store.exceptions;

import org.springframework.http.HttpStatus;

import java.time.Instant;

public record ApiException(Instant timestamp, int status, String error, String message, String path) {

    public static ApiException of(HttpStatus status, String message, String path) {
        return new ApiException(Instant.now(), status.value(), status.getReasonPhrase(), message, path);
    }
}
