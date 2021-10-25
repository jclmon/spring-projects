package com.ms.core.common.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginatedResponse {
    private List<Object> result;
    private Long numberOfItems;
    private int numberOfPages;    
}