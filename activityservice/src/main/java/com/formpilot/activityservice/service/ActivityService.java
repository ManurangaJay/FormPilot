package com.formpilot.activityservice.service;

import com.formpilot.activityservice.repository.ActivityRepository;
import com.formpilot.activityservice.dto.ActivityRequest;
import com.formpilot.activityservice.dto.ActivityResponse;
import com.formpilot.activityservice.model.Activity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;

    public ActivityResponse trackActivity(ActivityRequest activityRequest) {
        Activity activity = Activity.builder()
                .userId(activityRequest.getUserId())
                .type(activityRequest.getType())
                .duration(activityRequest.getDuration())
                .calariesBurned(activityRequest.getCalariesBurned())
                .startTime(activityRequest.getStartTime())
                .additionalMetrices(activityRequest.getAdditionalMetrices())
                .build();

        Activity savedActivity = activityRepository.save(activity);

        return mapToResponse(savedActivity);
    }

    private ActivityResponse mapToResponse(Activity activity){
        ActivityResponse response = new ActivityResponse();
        response.setId(activity.getId());
        response.setUserId(activity.getUserId());
        response.setType(activity.getType());
        response.setDuration(activity.getDuration());
        response.setCalariesBurned(activity.getCalariesBurned());
        response.setStartTime(activity.getStartTime());
        response.setAdditionalMetrices(activity.getAdditionalMetrices());
        response.setCreatedAt(activity.getCreatedAt());
        response.setUpdatedAt(activity.getUpdatedAt());
        return response;
    }
}
