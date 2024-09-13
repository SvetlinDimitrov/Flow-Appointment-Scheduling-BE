package com.internship.flow_appointment_scheduling.features.service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.internship.flow_appointment_scheduling.features.appointments.entity.Appointment;
import com.internship.flow_appointment_scheduling.features.appointments.entity.enums.AppointmentStatus;
import com.internship.flow_appointment_scheduling.features.appointments.service.AppointmentService;
import com.internship.flow_appointment_scheduling.features.service.dto.ServiceDTO;
import com.internship.flow_appointment_scheduling.features.service.dto.ServiceView;
import com.internship.flow_appointment_scheduling.features.service.entity.Service;
import com.internship.flow_appointment_scheduling.features.service.repository.ServiceRepository;
import com.internship.flow_appointment_scheduling.features.user.entity.User;
import com.internship.flow_appointment_scheduling.features.user.service.UserService;
import com.internship.flow_appointment_scheduling.features.work_space.entity.WorkSpace;
import com.internship.flow_appointment_scheduling.features.work_space.service.WorkSpaceService;
import com.internship.flow_appointment_scheduling.infrastructure.exceptions.BadRequestException;
import com.internship.flow_appointment_scheduling.infrastructure.exceptions.NotFoundException;
import com.internship.flow_appointment_scheduling.infrastructure.exceptions.enums.Exceptions;
import com.internship.flow_appointment_scheduling.infrastructure.mappers.service.ServiceMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class ServiceServiceImplTest {

  @Mock
  private ServiceRepository serviceRepository;
  @Mock
  private UserService userService;
  @Mock
  private WorkSpaceService workSpaceService;
  @Mock
  private AppointmentService appointmentService;
  @Mock
  private ServiceMapper serviceMapper;

  private ServiceServiceImpl serviceService;

  @BeforeEach
  void setUp() {
    serviceService = new ServiceServiceImpl(
        serviceRepository,
        userService,
        workSpaceService,
        serviceMapper
    );

    serviceService.setAppointmentService(appointmentService);
  }

  @Test
  void getAll_returnsNotEmptyPage_withStaffEmail() {
    Pageable pageable = mock(Pageable.class);
    Service service = mock(Service.class);
    ServiceView serviceView = mock(ServiceView.class);
    Page<Service> servicePage = new PageImpl<>(Collections.singletonList(service));

    String staffEmail = "staff@example.com";

    when(serviceRepository.findAllByUsersEmail(staffEmail, pageable)).thenReturn(servicePage);
    when(serviceMapper.toView(service)).thenReturn(serviceView);

    Page<ServiceView> result = serviceService.getAll(pageable, staffEmail);

    assertEquals(1, result.getTotalElements());
    assertEquals(serviceView, result.getContent().getFirst());
  }

  @Test
  void getAll_returnsEmptyPage_withoutStaffEmail() {
    Pageable pageable = mock(Pageable.class);
    Page<Service> servicePage = new PageImpl<>(Collections.emptyList());

    when(serviceRepository.findAll(pageable)).thenReturn(servicePage);

    Page<ServiceView> result = serviceService.getAll(pageable, null);

    assertEquals(0, result.getTotalElements());
  }

  @Test
  void getById_returnsServiceView_whenServiceExists() {
    Long serviceId = 1L;
    Service service = mock(Service.class);
    ServiceView serviceView = mock(ServiceView.class);

    when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(service));
    when(serviceMapper.toView(service)).thenReturn(serviceView);

    ServiceView result = serviceService.getById(serviceId);

    assertEquals(serviceView, result);
  }

  @Test
  void getById_throwsNotFoundException_whenServiceDoesNotExist() {
    Long serviceId = 1L;

    when(serviceRepository.findById(serviceId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> serviceService.getById(serviceId));
  }

  @Test
  void assignStaff_addsUserToService_whenUserIsNotAlreadyAssigned() {
    Long serviceId = 1L;
    String staffEmail = "staff@example.com";
    Service service = mock(Service.class);
    User user = mock(User.class);
    ServiceView serviceView = mock(ServiceView.class);

    when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(service));
    when(userService.findByEmail(staffEmail)).thenReturn(user);
    when(service.getUsers()).thenReturn(new ArrayList<>());
    when(serviceRepository.save(service)).thenReturn(service);
    when(serviceMapper.toView(service)).thenReturn(serviceView);

    ServiceView result = serviceService.assignStaff(serviceId, staffEmail);

    assertEquals(serviceView, result);
  }

  @Test
  void assignStaff_throwsBadRequestException_whenUserIsAlreadyAssigned() {
    Long serviceId = 1L;
    String staffEmail = "staff@example.com";
    Service service = mock(Service.class);
    User user = mock(User.class);

    when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(service));
    when(userService.findByEmail(staffEmail)).thenReturn(user);
    when(service.getUsers()).thenReturn(Collections.singletonList(user));
    when(user.getEmail()).thenReturn(staffEmail);

    assertThrows(BadRequestException.class, () -> {
      serviceService.assignStaff(serviceId, staffEmail);
    });
  }

  @Test
  void assignStaff_throwsNotFoundException_whenServiceDoesNotExist() {
    Long serviceId = 1L;
    String staffEmail = "staff@example.com";

    when(serviceRepository.findById(serviceId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> serviceService.assignStaff(serviceId, staffEmail));
  }

  @Test
  void assignStaff_throwsNotFoundException_whenUserDoesNotExist() {
    Long serviceId = 1L;
    String staffEmail = "staff@example.com";
    Service service = mock(Service.class);

    when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(service));
    when(userService.findByEmail(staffEmail)).thenThrow(
        new NotFoundException(Exceptions.USER_NOT_FOUND, staffEmail));

    assertThrows(NotFoundException.class, () -> serviceService.assignStaff(serviceId, staffEmail));
  }

  @Test
  void unassignStaff_removesUserFromService_whenUserIsAssigned() {
    Long serviceId = 1L;
    String staffEmail = "staff@example.com";
    Service service = mock(Service.class);
    User user = mock(User.class);
    ServiceView serviceView = mock(ServiceView.class);

    when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(service));
    when(userService.findByEmail(staffEmail)).thenReturn(user);
    when(service.getUsers()).thenReturn(new ArrayList<>(Collections.singletonList(user)));
    when(serviceRepository.save(service)).thenReturn(service);
    when(serviceMapper.toView(service)).thenReturn(serviceView);

    ServiceView result = serviceService.unassignStaff(serviceId, staffEmail);

    assertEquals(serviceView, result);
    assertEquals(0, service.getUsers().size());
  }

  @Test
  void unassignStaff_throwsBadRequestException_whenUserIsNotAssigned() {
    Long serviceId = 1L;
    String staffEmail = "staff@example.com";
    Service service = mock(Service.class);
    User user = mock(User.class);

    when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(service));
    when(userService.findByEmail(staffEmail)).thenReturn(user);
    when(service.getUsers()).thenReturn(new ArrayList<>());

    assertThrows(BadRequestException.class, () ->
        serviceService.unassignStaff(serviceId, staffEmail)
    );
  }

  @Test
  void unassignStaff_throwsNotFoundException_whenServiceDoesNotExist() {
    Long serviceId = 1L;
    String staffEmail = "staff@example.com";

    when(serviceRepository.findById(serviceId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class,
        () -> serviceService.unassignStaff(serviceId, staffEmail)
    );
  }

  @Test
  void unassignStaff_throwsNotFoundException_whenUserDoesNotExist() {
    Long serviceId = 1L;
    String staffEmail = "staff@example.com";
    Service service = mock(Service.class);

    when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(service));
    when(userService.findByEmail(staffEmail)).thenThrow(
        new NotFoundException(Exceptions.USER_NOT_FOUND, staffEmail));

    assertThrows(NotFoundException.class,
        () -> serviceService.unassignStaff(serviceId, staffEmail)
    );
  }

  @Test
  void create_createsService_whenWorkSpaceExists() {
    ServiceDTO createDto = mock(ServiceDTO.class);
    Service service = mock(Service.class);
    WorkSpace workSpace = mock(WorkSpace.class);
    ServiceView serviceView = mock(ServiceView.class);

    when(serviceMapper.toEntity(createDto)).thenReturn(service);
    when(workSpaceService.findByName(createDto.workSpaceName())).thenReturn(workSpace);
    when(serviceRepository.save(service)).thenReturn(service);
    when(serviceMapper.toView(service)).thenReturn(serviceView);

    ServiceView result = serviceService.create(createDto);

    assertEquals(serviceView, result);
    verify(service).setWorkSpace(workSpace);
  }

  @Test
  void create_throwsNotFoundException_whenWorkSpaceDoesNotExist() {
    ServiceDTO createDto = mock(ServiceDTO.class);
    Service service = mock(Service.class);

    when(serviceMapper.toEntity(createDto)).thenReturn(service);
    when(workSpaceService.findByName(createDto.workSpaceName())).thenThrow(
        new NotFoundException(Exceptions.WORK_SPACE_NOT_FOUND)
    );

    assertThrows(NotFoundException.class, () -> serviceService.create(createDto));
  }

  @Test
  void update_updatesService_whenServiceAndWorkSpaceExist() {
    Long serviceId = 1L;
    ServiceDTO putDto = mock(ServiceDTO.class);
    Service service = mock(Service.class);
    WorkSpace workSpace = mock(WorkSpace.class);
    ServiceView serviceView = mock(ServiceView.class);

    when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(service));
    when(workSpaceService.findByName(putDto.workSpaceName())).thenReturn(workSpace);
    when(serviceRepository.save(service)).thenReturn(service);
    when(serviceMapper.toView(service)).thenReturn(serviceView);

    ServiceView result = serviceService.update(serviceId, putDto);

    assertEquals(serviceView, result);
    verify(service).setWorkSpace(workSpace);
    verify(serviceMapper).updateEntity(service, putDto);
    verify(serviceRepository).save(service);
  }

  @Test
  void update_throwsNotFoundException_whenServiceDoesNotExist() {
    Long serviceId = 1L;
    ServiceDTO putDto = mock(ServiceDTO.class);

    when(serviceRepository.findById(serviceId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> serviceService.update(serviceId, putDto));
  }

  @Test
  void update_throwsNotFoundException_whenWorkSpaceDoesNotExist() {
    Long serviceId = 1L;
    ServiceDTO putDto = mock(ServiceDTO.class);
    Service service = mock(Service.class);

    when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(service));
    when(workSpaceService.findByName(putDto.workSpaceName())).thenThrow(
        new NotFoundException(Exceptions.WORK_SPACE_NOT_FOUND)
    );

    assertThrows(NotFoundException.class, () -> serviceService.update(serviceId, putDto));
  }

  @Test
  void update_cancelsAppointments_whenAvailabilityChangesToFalse() {
    Long serviceId = 1L;
    ServiceDTO putDto = mock(ServiceDTO.class);
    Service service = mock(Service.class);
    WorkSpace workSpace = mock(WorkSpace.class);
    ServiceView serviceView = mock(ServiceView.class);
    Appointment appointment = mock(Appointment.class);

    when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(service));
    when(workSpaceService.findByName(putDto.workSpaceName())).thenReturn(workSpace);
    when(serviceRepository.save(service)).thenReturn(service);
    when(serviceMapper.toView(service)).thenReturn(serviceView);
    when(service.getAvailability()).thenReturn(true);
    when(putDto.availability()).thenReturn(false);
    when(service.getAppointments()).thenReturn(Collections.singletonList(appointment));
    when(appointment.getStatus()).thenReturn(AppointmentStatus.NOT_APPROVED);

    ServiceView result = serviceService.update(serviceId, putDto);

    assertEquals(serviceView, result);
    verify(appointmentService).cancelAppointment(appointment.getId());
    verify(service).setWorkSpace(workSpace);
    verify(serviceMapper).updateEntity(service, putDto);
    verify(serviceRepository).save(service);
  }

  @Test
  void delete_deletesService_whenServiceExists() {
    Long serviceId = 1L;
    Service service = mock(Service.class);
    Appointment appointment = mock(Appointment.class);

    when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(service));
    when(service.getAppointments()).thenReturn(Collections.singletonList(appointment));
    when(appointment.getStatus()).thenReturn(AppointmentStatus.NOT_APPROVED);

    serviceService.delete(serviceId);

    verify(appointmentService).cancelAppointment(appointment.getId());
    verify(serviceRepository).delete(service);
  }

  @Test
  void delete_throwsNotFoundException_whenServiceDoesNotExist() {
    Long serviceId = 1L;

    when(serviceRepository.findById(serviceId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> serviceService.delete(serviceId));
  }
}