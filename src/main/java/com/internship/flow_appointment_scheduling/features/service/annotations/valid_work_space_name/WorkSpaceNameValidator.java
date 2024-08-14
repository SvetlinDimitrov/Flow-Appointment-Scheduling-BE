package com.internship.flow_appointment_scheduling.features.service.annotations.valid_work_space_name;

import com.internship.flow_appointment_scheduling.features.service.repository.WorkSpaceRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class WorkSpaceNameValidator implements ConstraintValidator<ValidWorkSpaceName, String> {

  @Autowired
  private WorkSpaceRepository workSpaceRepository;

  @Override
  public boolean isValid(String workSpaceName, ConstraintValidatorContext context) {
    return workSpaceRepository.existsByName(workSpaceName);
  }
}