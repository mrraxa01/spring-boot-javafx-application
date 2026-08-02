package com.mrrezende.springJavaFX.config;

import com.mrrezende.springJavaFX.view.event.StageReadyEventListener;
import com.mrrezende.springJavaFX.view.navigation.SceneNavigator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class AppConfig {
    @Bean
    public StageReadyEventListener stageReadyEventListener(
            @Value("${application.title}") String applicationTitle,
            SceneNavigator sceneNavigator,
            Environment environment) {
        return new StageReadyEventListener(applicationTitle, sceneNavigator, environment);
    }
}
