package com.formpilot.aiservice.service;

import com.formpilot.aiservice.model.Recommendation;
import com.formpilot.aiservice.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RecommendationService {
    private final RecommendationRepository repository;

    public List<Recommendation> getUserRecommendation(String userId) {
        return repository.findAllByUserId(userId);
    }

    public Recommendation getActivityRecommendation(String activityId) {
        return repository.findByActivityId(activityId).orElseThrow(() -> new RuntimeException("No recommendation found for this activity: " + activityId));
    }
}
