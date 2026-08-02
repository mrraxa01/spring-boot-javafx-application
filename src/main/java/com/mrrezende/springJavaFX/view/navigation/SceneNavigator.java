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

    private static final String GLOBAL_STYLESHEET = "/views/css/app.css";

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
     * O CSS global (/views/css/app.css) e sempre aplicado primeiro, como tema base;
     * qualquer stylesheet especifico da tela e adicionado depois, podendo sobrescrever
     * regras do tema base (ultima regra declarada vence, em caso de mesma especificidade).
     *
     * @param controllerClass controller anotado com @FxmlView
     * @param stylesheets     caminhos absolutos de classpath para CSS adicionais, especificos
     *                        da tela (ex: "/views/css/welcome.css"). Pode ser omitido.
     */
    public void navigateTo(Class<?> controllerClass, String... stylesheets) {
        if (primaryStage == null) {
            throw new IllegalStateException("SceneNavigator.setPrimaryStage() precisa ser chamado antes de navegar");
        }
        Parent parent = fxWeaver.loadView(controllerClass);
        Scene scene = new Scene(parent);

        addStylesheet(scene, GLOBAL_STYLESHEET);
        for (String css : stylesheets) {
            addStylesheet(scene, css);
        }

        primaryStage.setScene(scene);
    }

    private void addStylesheet(Scene scene, String css) {
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource(css), "CSS nao encontrado: " + css)
                        .toExternalForm());
    }
}
