package com.internship.flow_appointment_scheduling.features.service.entity;

import com.internship.flow_appointment_scheduling.features.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "services")
@Getter
@Setter
@ToString(exclude = {"users", "workSpace"})
@EqualsAndHashCode(exclude = {"users", "workSpace"})
public class Service {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, columnDefinition = "varchar(255)")
  private String name;

  @Column(nullable = false , columnDefinition = "text")
  private String description;

  @Column(nullable = false , name = "duration_minutes")
  private Integer duration;

  @Column(nullable = false , columnDefinition = "decimal(10,2)")
  private BigDecimal price;

  @ManyToOne
  private WorkSpace workSpace;

  @ManyToMany
  @JoinTable(
      name = "users_services",
      joinColumns = @JoinColumn(name = "service_id"),
      inverseJoinColumns = @JoinColumn(name = "user_id")
  )
  private List<User> users;
}
