package com.spring.redsocial.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignupRequest {
    private String name;
    private String nick;
    private String email;
    private String password;
    private String biografia;
}
