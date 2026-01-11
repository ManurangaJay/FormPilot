package com.formpilot.activityservice.dto;

import com.formpilot.activityservice.model.ActivityType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ActivityResponse {
    private String id;
    private String userId;
    private ActivityType type;
    private Integer duration;
    private Integer calariesBurned;
    private LocalDateTime startTime;
    private Map<String, Object> additionalMetrices;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
