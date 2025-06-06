package com.internship.flow_appointment_scheduling.features.work_space.annotations.valid_work_space_name;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = WorkSpaceNameValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidWorkSpaceName {
  String message() default "WorkSpace name does not exist";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
