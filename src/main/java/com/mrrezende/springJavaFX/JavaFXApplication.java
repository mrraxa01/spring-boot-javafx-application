package com.mrrezende.springJavaFX;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import com.mrrezende.springJavaFX.view.event.StageReadyEvent;

public class JavaFXApplication extends Application {
    private ConfigurableApplicationContext applicationContext;

    @Override
    public void start(Stage stage) throws Exception {
        applicationContext.publishEvent(new StageReadyEvent(stage));
    }

    @Override
    public void init() {
        applicationContext =
                new SpringApplicationBuilder(SpringJavaFxApplication.class)
                        .run(getParameters().getRaw().toArray(new String[0]));
    }

    @Override
    public void stop() {
        applicationContext.close();
        Platform.exit();
    }
}
