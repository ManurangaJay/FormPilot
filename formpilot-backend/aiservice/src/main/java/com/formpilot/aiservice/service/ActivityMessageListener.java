package com.formpilot.aiservice.service;

import com.formpilot.aiservice.model.Activity;
import com.formpilot.aiservice.model.Recommendation;
import com.formpilot.aiservice.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListener {

    private final ActivityAIService aiService;
    private final RecommendationRepository repository;

    @Bean
    public Consumer<Activity> activityProcessor(){
        return activity -> {
            log.info("Received activity for processing via Stream: {}" , activity.getId());
            try {
                Recommendation recommendation = aiService.generateRecommendation(activity);
                repository.save(recommendation);
                log.info("Saved Recommendation for activity: {}", activity.getId() );
            } catch (Exception e){
                log.error("Failed to process activity: {}", activity.getId(), e);
            }
        };
    }

//    @RabbitListener(queues = "activity.queue")
//    public void processActivity(Activity activity){
//        log.info("Received activity for processing: {}" , activity.getId());
////        log.info("Generated Recommendation: {}" , aiService.generateRecommendation(activity));
//        Recommendation recommendation = aiService.generateRecommendation(activity);
//        repository.save(recommendation);
//    }
}
