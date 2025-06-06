package com.internship.flow_appointment_scheduling.features.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "staff_details")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "user")
@EqualsAndHashCode(exclude = "user")
public class StaffDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private BigDecimal salary;

  @Column(nullable = false)
  @Builder.Default
  private BigDecimal profit = BigDecimal.ZERO;

  @Column(name = "completed_appointments", nullable = false)
  @Builder.Default
  private Integer completedAppointments = 0;

  @Column(name = "start_date", nullable = false)
  @Builder.Default
  private LocalDate startDate = LocalDate.now();

  @Column(name = "availability", nullable = false)
  @Builder.Default
  private Boolean isAvailable = true;

  @Column(name = "begin_working_hour", nullable = false)
  private LocalTime beginWorkingHour;

  @Column(name = "end_working_hour", nullable = false)
  private LocalTime endWorkingHour;

  @OneToOne private User user;
}
