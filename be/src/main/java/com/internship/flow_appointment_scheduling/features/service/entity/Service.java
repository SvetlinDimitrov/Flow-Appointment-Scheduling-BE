package com.internship.flow_appointment_scheduling.features.service.entity;

import com.internship.flow_appointment_scheduling.features.appointments.entity.Appointment;
import com.internship.flow_appointment_scheduling.features.user.entity.User;
import com.internship.flow_appointment_scheduling.features.work_space.entity.WorkSpace;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "services")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"users", "workSpace"})
@EqualsAndHashCode(exclude = {"users", "workSpace"})
public class Service {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, columnDefinition = "varchar(255)")
  private String name;

  @Column(nullable = false, columnDefinition = "text")
  private String description;

  @Column(nullable = false)
  private Duration duration;

  @Column(nullable = false, columnDefinition = "decimal(10,2)")
  private BigDecimal price;

  @Column(nullable = false)
  private Boolean availability;

  @ManyToOne private WorkSpace workSpace;

  @ManyToMany
  @JoinTable(
      name = "users_services",
      joinColumns = @JoinColumn(name = "service_id"),
      inverseJoinColumns = @JoinColumn(name = "user_id"))
  @Builder.Default
  private List<User> users = new ArrayList<>();

  @OneToMany(mappedBy = "service", cascade = CascadeType.REMOVE)
  @Builder.Default
  private List<Appointment> appointments = new ArrayList<>();
}
