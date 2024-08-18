package com.internship.flow_appointment_scheduling.features.service.dto;

public record ServiceView(
    Long id,
    String name,
    String description,
    Double price,
    WorkSpaceView workSpace) {
}