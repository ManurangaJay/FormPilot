package com.formpilot.activityservice.service;

import com.formpilot.activityservice.repository.ActivityRepository;
import com.formpilot.activityservice.dto.ActivityRequest;
import com.formpilot.activityservice.dto.ActivityResponse;
import com.formpilot.activityservice.model.Activity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserValidationService userValidationService;
    private final StreamBridge streamBridge;

//    @Value("${rabbitmq.exchange.name}")
//    private String exchange;
//    @Value("${rabbitmq.routing.key}")
//    private String routingKey;

    public ActivityResponse trackActivity(ActivityRequest activityRequest) {
        boolean isValidUser = userValidationService.validateUser(activityRequest.getUserId());

        if(!isValidUser){
            throw new RuntimeException("Invalid User: " + activityRequest.getUserId());
        }

        Activity activity = Activity.builder()
                .userId(activityRequest.getUserId())
                .type(activityRequest.getType())
                .duration(activityRequest.getDuration())
                .caloriesBurned(activityRequest.getCaloriesBurned())
                .startTime(activityRequest.getStartTime())
                .additionalMetrices(activityRequest.getAdditionalMetrices())
                .build();

        Activity savedActivity = activityRepository.save(activity);

        // Publish to RabbitMQ for AI processing
        try{
            log.info("Sending activity even for user: {}", savedActivity.getUserId());
            streamBridge.send("activity-out-0", savedActivity);
        }catch (Exception e){
            log.error("Failed to publish activity to RabbitMQ: " , e);
        }
        return mapToResponse(savedActivity);
    }

    private ActivityResponse mapToResponse(Activity activity){


        ActivityResponse response = new ActivityResponse();
        response.setId(activity.getId());
        response.setUserId(activity.getUserId());
        response.setType(activity.getType());
        response.setDuration(activity.getDuration());
        response.setCaloriesBurned(activity.getCaloriesBurned());
        response.setStartTime(activity.getStartTime());
        response.setAdditionalMetrices(activity.getAdditionalMetrices());
        response.setCreatedAt(activity.getCreatedAt());
        response.setUpdatedAt(activity.getUpdatedAt());
        return response;
    }

    public List<ActivityResponse> getUserActivities(String userId) {
        List<Activity> activities = activityRepository.findByUserId(userId);
        return activities.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public ActivityResponse getActivityById(String activityId) {
        return activityRepository.findById(activityId).map(this::mapToResponse).orElseThrow(() -> new RuntimeException("Activity not found"));
    }
}
