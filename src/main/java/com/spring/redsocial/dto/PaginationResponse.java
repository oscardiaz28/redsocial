package com.spring.redsocial.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginationResponse {
    private Long totalElements;
    private Integer totalPages;
    private Integer currentPage;
    private Integer itemsPerPage;
    private Object content;
}
