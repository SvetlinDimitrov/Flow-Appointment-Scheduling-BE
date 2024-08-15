package com.internship.flow_appointment_scheduling.infrastructure.mappers;

import com.internship.flow_appointment_scheduling.features.service.dto.ServiceDTO;
import com.internship.flow_appointment_scheduling.features.service.dto.ServiceView;
import com.internship.flow_appointment_scheduling.features.service.entity.Service;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", implementationName = "ServiceMapperImpl")
public interface ServiceMapper {

  ServiceView toView(Service entity);

  Service toEntity(ServiceDTO dto);

  void updateEntity(@MappingTarget Service toUpdate, ServiceDTO dto);
}
