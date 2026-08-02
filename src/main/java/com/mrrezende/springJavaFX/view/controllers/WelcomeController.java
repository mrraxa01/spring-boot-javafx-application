package com.mrrezende.springJavaFX.view.controllers;

import com.mrrezende.springJavaFX.model.User;
import com.mrrezende.springJavaFX.service.UserService;
import com.mrrezende.springJavaFX.view.navigation.SessionContext;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/views/Welcome.fxml")
public class WelcomeController {

    private final UserService userService;
    private final SessionContext sessionContext;

    @FXML
    private Label welcomeLabel;

    public WelcomeController(UserService userService, SessionContext sessionContext) {
        this.userService = userService;
        this.sessionContext = sessionContext;
    }

    // Chamado automaticamente pelo FXMLLoader logo apos a injecao dos campos @FXML
    @FXML
    private void initialize() {
        User user = userService.findById(sessionContext.getCurrentUserId());
        welcomeLabel.setText("Bem vindo, " + user.getName());
    }
}
