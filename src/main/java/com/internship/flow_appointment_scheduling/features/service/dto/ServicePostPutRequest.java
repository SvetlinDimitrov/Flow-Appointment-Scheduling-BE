package com.internship.flow_appointment_scheduling.features.service.dto;

import com.internship.flow_appointment_scheduling.features.service.annotations.valid_work_space_name.ValidWorkSpaceName;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ServicePostPutRequest(

    @NotBlank(message = "Name is mandatory")
    @Size(min = 3, max = 255, message = "Name must be between 3 and 255 characters")
    String name,

    @NotBlank(message = "Description is mandatory")
    String description,

    @NotNull(message = "Price is mandatory")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    BigDecimal price,

    @NotBlank(message = "WorkSpace name is mandatory")
    @ValidWorkSpaceName
    String workSpaceName) {

}
