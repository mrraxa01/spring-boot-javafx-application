package com.mrrezende.springJavaFX.view.event;

import com.mrrezende.springJavaFX.view.controllers.MainWindowController;
import com.mrrezende.springJavaFX.view.navigation.SceneNavigator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import fr.brouillard.oss.cssfx.CSSFX;
import org.springframework.stereotype.Component;

@Component
public class StageReadyEventListener implements ApplicationListener<StageReadyEvent> {

    private static final Logger log = LoggerFactory.getLogger(StageReadyEventListener.class);

    private final String applicationTitle;

    private final SceneNavigator sceneNavigator;

    private final Environment environment;

    public StageReadyEventListener(String applicationTitle, SceneNavigator sceneNavigator, Environment environment) {
        this.applicationTitle = applicationTitle;
        this.sceneNavigator = sceneNavigator;
        this.environment = environment;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        var stage = event.getStage();
        try {
            sceneNavigator.setPrimaryStage(stage);
            sceneNavigator.navigateTo(MainWindowController.class, "/views/css/app.css");

            // CSSFX (hot-reload de CSS) so faz sentido em desenvolvimento
            if (environment.matchesProfiles("dev")) {
                CSSFX.start();
            }

            stage.setTitle(applicationTitle);
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            log.error("Falha ao carregar a tela inicial da aplicacao", e);
            throw e;
        }
    }
}
