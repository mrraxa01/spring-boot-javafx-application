package com.mrrezende.springJavaFX.config;

import com.mrrezende.springJavaFX.view.event.StageReadyEventListener;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public StageReadyEventListener stageReadyEventListener(
            @Value("${application.title}") String applicationTitle,
            FxWeaver fxWeaver) {
        return new StageReadyEventListener(applicationTitle, fxWeaver);
    }
}