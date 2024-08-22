package com.internship.flow_appointment_scheduling.features.appointments.utils;

import com.internship.flow_appointment_scheduling.features.appointments.repository.AppointmentRepository;
import com.internship.flow_appointment_scheduling.features.service.entity.Service;
import com.internship.flow_appointment_scheduling.features.user.entity.StaffDetails;
import com.internship.flow_appointment_scheduling.features.user.entity.User;
import com.internship.flow_appointment_scheduling.features.user.entity.enums.UserRoles;
import com.internship.flow_appointment_scheduling.infrastructure.exceptions.BadRequestException;
import com.internship.flow_appointment_scheduling.infrastructure.exceptions.enums.Exceptions;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppointmentValidator {

  private final AppointmentRepository appointmentRepository;

  public void validateAppointment(
      User staff, User client, Service service, LocalDateTime startDate, LocalDateTime endDate) {

     /*
      Steps to create a new appointment:
      1. Check if the client is a client and the staff is a staff.
      2. Check if the staff is available at the given time.
      3. Check if staff and service are available.
      4. Check if staff has the requested service.
      5. Check if the client and staff have overlapping appointments
      (they should be able to have only one appointment at a given time).
      6. Check if the workSpace has the capacity for the given time.
     */

    checkForUserRoles(client, staff);
    checkForStaffWorkingTime(staff, startDate, endDate);
    checkForStaffAndServiceAvailability(staff, service);
    checkForStaffContainingService(staff, service);
    checkForWorkSpaceCapacity(service, startDate, endDate);
    checkForOverlappingAppointments(client, staff, startDate, endDate);
  }


  private void checkForUserRoles(User client, User staff) {
    if (!client.getRole().equals(UserRoles.CLIENT)) {
      throw new BadRequestException(Exceptions.APPOINTMENT_WRONG_CLIENT_ROLE, client.getEmail());
    }

    if (!staff.getRole().equals(UserRoles.EMPLOYEE)) {
      throw new BadRequestException(Exceptions.APPOINTMENT_WRONG_STAFF_ROLE, staff.getEmail());
    }
  }

  private void checkForStaffWorkingTime(
      User staff, LocalDateTime startDate, LocalDateTime endDate) {
    LocalTime startTime = startDate.toLocalTime();
    LocalTime endTime = endDate.toLocalTime();
    StaffDetails staffDetails = staff.getStaffDetails();

    if (startTime.isBefore(staffDetails.getBeginWorkingHour()) ||
        endTime.isAfter(staffDetails.getEndWorkingHour())) {
      throw new BadRequestException(Exceptions.APPOINTMENT_STAFF_NOT_AVAILABLE, staff.getEmail());
    }
  }

  private void checkForOverlappingAppointments(
      User client, User staff, LocalDateTime startDate, LocalDateTime endDate) {
    if (hasOverLappingAppointmentsForGivenUser(client, startDate, endDate)) {
      throw new BadRequestException(Exceptions.APPOINTMENT_OVERLAP);
    }

    if (hasOverLappingAppointmentsForGivenUser(staff, startDate, endDate)) {
      throw new BadRequestException(Exceptions.APPOINTMENT_OVERLAP);
    }
  }

  private void checkForStaffAndServiceAvailability(User staff, Service service) {
    if (!staff.getStaffDetails().getIsAvailable()) {
      throw new BadRequestException(Exceptions.APPOINTMENT_STAFF_NOT_AVAILABLE, staff.getEmail());
    }

    if (!service.getAvailability()) {
      throw new BadRequestException(Exceptions.APPOINTMENT_SERVICE_NOT_AVAILABLE, service.getId());
    }
  }

  private void checkForStaffContainingService(User staff, Service service) {
    if (!staff.getServices().contains(service)) {
      throw new BadRequestException(
          Exceptions.APPOINTMENT_STAFF_NOT_CONTAINING_SERVICE,
          staff.getEmail(), service.getId()
      );
    }
  }

  private void checkForWorkSpaceCapacity(Service service, LocalDateTime startDate,
      LocalDateTime endDate) {
    int appointmentsCount = appointmentRepository.countAppointmentsInWorkspace(
        service.getWorkSpace().getId(), startDate, endDate);

    if (appointmentsCount >= service.getWorkSpace().getAvailableSlots()) {
      throw new BadRequestException(Exceptions.APPOINTMENT_WORK_SPACE_NOT_AVAILABLE,
          service.getWorkSpace().getId());
    }
  }

  private boolean hasOverLappingAppointmentsForGivenUser(
      User user, LocalDateTime startDate, LocalDateTime endDate) {
    return appointmentRepository.existsOverlappingAppointment(user.getEmail(), startDate, endDate);
  }
}
