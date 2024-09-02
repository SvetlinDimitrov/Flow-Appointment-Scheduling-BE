package com.internship.flow_appointment_scheduling.features.user.service;

import com.internship.flow_appointment_scheduling.features.appointments.entity.Appointment;
import com.internship.flow_appointment_scheduling.features.user.dto.staff_details.StaffHireDto;
import com.internship.flow_appointment_scheduling.features.user.dto.staff_details.StaffModifyDto;
import com.internship.flow_appointment_scheduling.features.user.dto.users.UserPostRequest;
import com.internship.flow_appointment_scheduling.features.user.dto.users.UserPutRequest;
import com.internship.flow_appointment_scheduling.features.user.dto.users.UserView;
import com.internship.flow_appointment_scheduling.features.user.entity.User;
import com.internship.flow_appointment_scheduling.features.user.entity.enums.UserRoles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

  Page<UserView> getAll(Pageable pageable , UserRoles userRole);

  UserView getById(Long id);

  UserView create(UserPostRequest userView);

  UserView update(Long id, UserPutRequest userView);

  void delete(Long id);

  User findByEmail(String email);

  UserView hireStaff(StaffHireDto userPostRequest);

  UserView modifyStaff(Long id, StaffModifyDto userPutRequest);

  void handleCompletingTheAppointment(Appointment appointment);
}
