package com.internship.flow_appointment_scheduling.features.user.dto;

import com.internship.flow_appointment_scheduling.features.user.annotations.admin_only.AdminOnly;
import com.internship.flow_appointment_scheduling.features.user.entity.enums.UserRoles;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserPutRequest(

    @Size(min = 3, max = 255, message = "First name must not exceed 255 characters and must be at least 3 characters")
    @NotBlank(message = "First name must not be blank")
    String firstName,

    @Size(min = 3, max = 255, message = "Last name must not exceed 255 characters and must be at least 3 characters")
    @NotBlank(message = "Last name must not be blank")
    String lastName,

    @AdminOnly
    UserRoles role

) {

}