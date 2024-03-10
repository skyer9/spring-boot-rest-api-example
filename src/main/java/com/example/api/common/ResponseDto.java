package com.example.api.common;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
public class ResponseDto<T> {
    private HttpStatus status;
    private String message;
    private T data;

    public static<T> ResponseDto<T> res(final HttpStatus status, final String message) {
        return res(status, message, null);
    }

    public static<T> ResponseDto<T> res(final HttpStatus status, final String message, final T data) {
        return ResponseDto.<T>builder()
                .status(status)
                .message(message)
                .data(data)
                .build();
    }
}
