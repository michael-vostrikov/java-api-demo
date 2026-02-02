package com.app.userapi.service.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class SaveProductDto {
    public final Integer categoryId;

    @Size(min = 1, max = 100)
    public final String name;

    @Size(max = 2000)
    public final String description;
}
