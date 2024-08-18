package com.internship.flow_appointment_scheduling.infrastructure.openapi;

import com.internship.flow_appointment_scheduling.features.service.annotations.employee_or_admin.EmployeeOrAdmin;
import com.internship.flow_appointment_scheduling.features.service.dto.ServiceDTO;
import com.internship.flow_appointment_scheduling.features.service.dto.ServiceView;
import com.internship.flow_appointment_scheduling.infrastructure.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface ServiceControllerDocumentation {

  @Operation(summary = "Get all services")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found the services",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = Page.class))}),
      @ApiResponse(responseCode = "403", description = "Forbidden",
          content = {@Content(mediaType = "application/json")}),
      @ApiResponse(responseCode = "401", description = "Unauthorized",
          content = {@Content(mediaType = "application/json")}),
  })
  @GetMapping
  @SecurityRequirement(name = "bearerAuth")
  ResponseEntity<Page<ServiceView>> getAll(Pageable pageable, @RequestParam(required = false) String employeeEmail);

  @Operation(summary = "Get a service by ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found the service",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceView.class))}),
      @ApiResponse(responseCode = "404", description = "Service not found",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ProblemDetail.class))}),
      @ApiResponse(responseCode = "403", description = "Forbidden",
          content = {@Content(mediaType = "application/json")}),
      @ApiResponse(responseCode = "401", description = "Unauthorized",
          content = {@Content(mediaType = "application/json")}),
  })
  @SecurityRequirement(name = "bearerAuth")
  @GetMapping("/{id}")
  ResponseEntity<ServiceView> getById(@PathVariable Long id);

  @Operation(
      summary = "Create a service",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ServiceDTO.class),
              examples = @ExampleObject(
                  name = "CreateService",
                  summary = "Example of creating a service",
                  value = """
                {
                  "name": "Gym instructor",
                  "description": "Become the next Chris Bumstead",
                  "duration": 60,
                  "price": 99.99,
                  "workSpaceName": "Gym"
                }
                """
              )
          )
      )
  )    @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Service created",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceView.class))}),
      @ApiResponse(responseCode = "403", description = "Forbidden",
          content = {@Content(mediaType = "application/json")}),
      @ApiResponse(responseCode = "400", description = "Invalid input",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ProblemDetail.class))}),
      @ApiResponse(responseCode = "401", description = "Unauthorized",
          content = {@Content(mediaType = "application/json")}),
  })
  @SecurityRequirement(name = "bearerAuth")
  @PostMapping
  ResponseEntity<ServiceView> create(@Valid @RequestBody ServiceDTO createDto, @AuthenticationPrincipal CustomUserDetails customUserDetails);

  @Operation(summary = "Assign an employee to a service")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Employee assigned",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceView.class))}),
      @ApiResponse(responseCode = "400", description = "User already assigned to service",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ProblemDetail.class))}),
      @ApiResponse(responseCode = "403", description = "Forbidden",
          content = {@Content(mediaType = "application/json")}),
      @ApiResponse(responseCode = "404", description = "Service or employee not found",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ProblemDetail.class))}),
      @ApiResponse(responseCode = "401", description = "Unauthorized",
          content = {@Content(mediaType = "application/json")}),
  })
  @SecurityRequirement(name = "bearerAuth")
  @PostMapping("/{id}/assign")
  ResponseEntity<ServiceView> assignEmployee(@PathVariable Long id, @RequestParam @EmployeeOrAdmin String employeeEmail);

  @Operation(summary = "Unassign an employee from a service")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Employee unassigned",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceView.class))}),
      @ApiResponse(responseCode = "400", description = "User not assigned to service",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ProblemDetail.class))}),
      @ApiResponse(responseCode = "403", description = "Forbidden",
          content = {@Content(mediaType = "application/json")}),
      @ApiResponse(responseCode = "404", description = "Service or employee not found",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ProblemDetail.class))}),
      @ApiResponse(responseCode = "401", description = "Unauthorized",
          content = {@Content(mediaType = "application/json")}),
  })
  @SecurityRequirement(name = "bearerAuth")
  @PutMapping("/{id}/unassign")
  ResponseEntity<ServiceView> unassignEmployee(@PathVariable Long id, @RequestParam String employeeEmail);

  @Operation(
      summary = "Update an existing service",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ServiceDTO.class),
              examples = @ExampleObject(
                  name = "UpdateServiceExample",
                  summary = "Example of updating a service",
                  value = """
                      {
                        "name": "Gym instructor",
                        "description": "Become the next Chris Bumstead",
                        "duration": 60,
                        "price": 99.99,
                        "workSpaceName": "Gym"
                      }
                      """
              )
          )
      )
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Service updated",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceView.class))}),
      @ApiResponse(responseCode = "400", description = "Invalid input",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ProblemDetail.class))}),
      @ApiResponse(responseCode = "404", description = "Service not found",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ProblemDetail.class))}),
      @ApiResponse(responseCode = "403", description = "Forbidden",
          content = {@Content(mediaType = "application/json")}),
      @ApiResponse(responseCode = "401", description = "Unauthorized",
          content = {@Content(mediaType = "application/json")}),
  })
  @SecurityRequirement(name = "bearerAuth")
  @PutMapping("/{id}")
  ResponseEntity<ServiceView> update(@PathVariable Long id, @Valid @RequestBody ServiceDTO putDto);

  @Operation(summary = "Delete a service")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Service deleted",
          content = @Content),
      @ApiResponse(responseCode = "404", description = "Service not found",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ProblemDetail.class))}),
      @ApiResponse(responseCode = "403", description = "Forbidden",
          content = {@Content(mediaType = "application/json")}),
      @ApiResponse(responseCode = "401", description = "Unauthorized",
          content = {@Content(mediaType = "application/json")}),
  })
  @SecurityRequirement(name = "bearerAuth")
  @DeleteMapping("/{id}")
  ResponseEntity<Void> delete(@PathVariable Long id);
}