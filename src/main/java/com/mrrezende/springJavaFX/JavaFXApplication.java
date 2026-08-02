package com.mrrezende.springJavaFX;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import com.mrrezende.springJavaFX.config.AppDataLocator;
import com.mrrezende.springJavaFX.view.event.StageReadyEvent;

import java.nio.file.Path;

public class JavaFXApplication extends Application {

    // Ajuste vendor/appName para o nome real da sua empresa/produto antes de distribuir
    private static final String VENDOR = "mrrezende";
    private static final String APP_NAME = "springJavaFX";

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void start(Stage stage) throws Exception {
        applicationContext.publishEvent(new StageReadyEvent(stage));
    }

    @Override
    public void init() {
        Path dataDir = AppDataLocator.resolveAppDataDir(VENDOR, APP_NAME);
        // Disponibiliza como property para ser referenciada no application.properties via ${app.data.dir}
        System.setProperty("app.data.dir", dataDir.toString());

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
