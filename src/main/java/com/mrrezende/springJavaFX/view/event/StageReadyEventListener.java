package com.mrrezende.springJavaFX.view.event;

import com.mrrezende.springJavaFX.view.controllers.MainWindowController;
import javafx.scene.Parent;
import javafx.scene.Scene;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.context.ApplicationListener;
import fr.brouillard.oss.cssfx.CSSFX;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class StageReadyEventListener implements ApplicationListener<StageReadyEvent> {

    private final String applicationTitle;

    private final FxWeaver fxWeaver;

    public StageReadyEventListener(String applicationTitle, FxWeaver fxWeaver) {
        this.applicationTitle = applicationTitle;
        this.fxWeaver = fxWeaver;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        var stage = event.getStage();
        Parent parent = fxWeaver.loadView(MainWindowController.class);
        var scene = new Scene(parent);
        scene
                .getStylesheets()
                .add(
                        Objects.requireNonNull(
                                        StageReadyEventListener.class.getResource("/views/css/app.css"))
                                .toExternalForm());
        CSSFX.start();
        stage.setScene(scene);
        stage.setTitle(applicationTitle);
        stage.centerOnScreen();
        stage.show();
    }
}
