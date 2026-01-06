package com.formpilot.aiservice.service;

import com.formpilot.aiservice.model.Activity;
import com.formpilot.aiservice.model.Recommendation;
import com.formpilot.aiservice.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListener {

    private final ActivityAIService aiService;
    private final RecommendationRepository repository;

    @RabbitListener(queues = "activity.queue")
    public void processActivity(Activity activity){
        log.info("Received activity for processing: {}" , activity.getId());
//        log.info("Generated Recommendation: {}" , aiService.generateRecommendation(activity));
        Recommendation recommendation = aiService.generateRecommendation(activity);
        repository.save(recommendation);
    }
}
