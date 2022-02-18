package com.kmetha.cloudstorage.client.controllers;

import com.kmetha.cloudstorage.client.tools.SceneSwitcher;
import com.kmetha.cloudstorage.core.database.DBHandler;
import com.kmetha.cloudstorage.core.database.User;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class Registration implements Initializable {

    private static final String AUTHORIZATION = "authorization.fxml";
    private final SceneSwitcher switcher = new SceneSwitcher();

    public AnchorPane pane;
    public TextField nameField;
    public TextField loginField;
    public PasswordField passwordField;
    public PasswordField repeatedPasswordField;
    public Label warningIncorrectlyData;
    public Label warningLoginAlreadyUse;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        warningIncorrectlyData.setVisible(false);
        warningLoginAlreadyUse.setVisible(false);
    }

    public void registration(ActionEvent actionEvent) {
        String name = nameField.getText().trim();
        String login = loginField.getText().trim();
        String pass = passwordField.getText().trim();
        String repeatedPass = repeatedPasswordField.getText().trim();
        if (name.isEmpty() || login.isEmpty() || pass.isEmpty() || repeatedPass.isEmpty()) {
            warningLoginAlreadyUse.setVisible(false);
            warningIncorrectlyData.setVisible(true);
        } else if (!pass.equals(repeatedPass)) {
            passwordField.clear();
            repeatedPasswordField.clear();
            warningLoginAlreadyUse.setVisible(false);
            warningIncorrectlyData.setVisible(true);
        } else {
            DBHandler handler = new DBHandler();
            User user = new User(name, login, pass);
            if (handler.tryToRegistration(user)) {
                nameField.getScene().getWindow().hide();
                switcher.switchTo(AUTHORIZATION, pane);
            } else {
                warningIncorrectlyData.setVisible(false);
                warningLoginAlreadyUse.setVisible(true);
            }
        }
    }

    public void close(ActionEvent actionEvent) {
        nameField.getScene().getWindow().hide();
        switcher.switchTo(AUTHORIZATION, pane);
    }
}
