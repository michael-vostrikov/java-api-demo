package com.app.userapi.service.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class CreateProductDto {
    @Size(max = 100)
    public final String name;
}
