package com.spring.redsocial.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Data
public class PublicationResponse {
    private Integer publicationId;
    private Integer userId;
    private String userNick;
    private String image;
    private String text;
    private String file;
    private LocalDateTime createdAt;
}
