package com.internship.flow_appointment_scheduling.features.service.dto;

import java.time.Duration;

public record ServiceView(
    Long id,
    String name,
    Duration duration,
    String description,
    Boolean availability,
    Double price,
    WorkSpaceView workSpace) {
}
