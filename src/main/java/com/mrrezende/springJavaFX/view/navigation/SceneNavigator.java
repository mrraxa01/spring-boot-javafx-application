package com.mrrezende.springJavaFX.view.navigation;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Centraliza a troca de telas da aplicacao. Controllers que precisam navegar
 * recebem este bean por injecao de dependencia em vez de montar Scene/Stage
 * na mao.
 */
@Component
public class SceneNavigator {

    private final FxWeaver fxWeaver;

    private Stage primaryStage;

    public SceneNavigator(FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
    }

    /**
     * Deve ser chamado uma vez, quando o Stage principal fica pronto
     * (ver StageReadyEventListener).
     */
    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    /**
     * Troca a cena atual do Stage principal pela view associada ao controller informado.
     *
     * @param controllerClass controller anotado com @FxmlView
     * @param stylesheets     caminhos absolutos de classpath para CSS (ex: "/views/css/app.css").
     *                        Pode ser omitido se a tela nao tiver estilo proprio.
     */
    public void navigateTo(Class<?> controllerClass, String... stylesheets) {
        if (primaryStage == null) {
            throw new IllegalStateException("SceneNavigator.setPrimaryStage() precisa ser chamado antes de navegar");
        }
        Parent parent = fxWeaver.loadView(controllerClass);
        Scene scene = new Scene(parent);
        for (String css : stylesheets) {
            scene.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource(css), "CSS nao encontrado: " + css)
                            .toExternalForm());
        }
        primaryStage.setScene(scene);
    }
}
