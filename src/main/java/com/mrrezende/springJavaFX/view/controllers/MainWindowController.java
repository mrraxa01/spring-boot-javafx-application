package com.mrrezende.springJavaFX.view.controllers;

import com.mrrezende.springJavaFX.model.User;
import com.mrrezende.springJavaFX.service.UserService;
import com.mrrezende.springJavaFX.view.navigation.SceneNavigator;
import com.mrrezende.springJavaFX.view.navigation.SessionContext;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/views/Main.fxml")
public class MainWindowController {

    private final UserService userService;
    private final SceneNavigator sceneNavigator;
    private final SessionContext sessionContext;

    @FXML
    private TextField nameField;

    public MainWindowController(UserService userService, SceneNavigator sceneNavigator, SessionContext sessionContext) {
        this.userService = userService;
        this.sceneNavigator = sceneNavigator;
        this.sessionContext = sessionContext;
    }

    @FXML
    private void onOkClick() {
        String name = nameField.getText();
        if (name == null || name.isBlank()) {
            return;
        }

        User user = userService.createUser(name.trim());
        sessionContext.setCurrentUserId(user.getId());

        sceneNavigator.navigateTo(WelcomeController.class, "/views/css/welcome.css");
    }
}
