package com.coppel.dto.by;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Value {
    private String operationType;
    private Entity entity;
    private String updateTime;


    // Getters and setters
}
