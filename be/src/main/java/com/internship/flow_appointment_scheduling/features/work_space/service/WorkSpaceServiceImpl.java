package com.internship.flow_appointment_scheduling.features.work_space.service;

import com.internship.flow_appointment_scheduling.features.work_space.entity.WorkSpace;
import com.internship.flow_appointment_scheduling.features.work_space.repository.WorkSpaceRepository;
import com.internship.flow_appointment_scheduling.infrastructure.exceptions.NotFoundException;
import com.internship.flow_appointment_scheduling.infrastructure.exceptions.enums.Exceptions;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkSpaceServiceImpl implements WorkSpaceService {

  private final WorkSpaceRepository workSpaceRepository;

  @Override
  public WorkSpace findByName(String name) {
    return workSpaceRepository
        .findByName(name)
        .orElseThrow(() -> new NotFoundException(Exceptions.WORK_SPACE_NOT_FOUND, name));
  }

  @Override
  public List<String> getAllNames() {
    return workSpaceRepository.findAll().stream().map(WorkSpace::getName).toList();
  }
}
