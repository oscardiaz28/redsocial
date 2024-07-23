package com.spring.redsocial.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class AuthResponse {
    private Object message;
    private Object data;
    private String token;
    private HttpStatus status;
}
