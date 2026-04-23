package com.thasya.inventory.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StockOperationResponse {

    private String message;
    private Long itemId;
    private Integer quantity;
    private Integer reserved;
    private Integer available;
    private LocalDateTime timestamp;
}

