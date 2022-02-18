package com.kmetha.cloudstorage.client.controllers;

import com.kmetha.cloudstorage.client.constants.Constants;
import com.kmetha.cloudstorage.client.tools.SceneSwitcher;
import com.kmetha.cloudstorage.client.animation.ShakeAnimation;
import com.kmetha.cloudstorage.core.database.DBHandler;
import com.kmetha.cloudstorage.core.database.User;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;

public class Authorization implements Initializable {

    private static final String CLIENT = "client.fxml";
    private static final String REGISTRATION = "registration.fxml";
    private final SceneSwitcher switcher = new SceneSwitcher();

    public AnchorPane pane;
    public Button singIn;
    public Button register;
    public Label warningIncorrectlyData;
    public TextField loginField;
    public PasswordField passwordField;

    private void preConnect(DBHandler handler, User user) {
        File profiles = new File(Constants.PROFILES);
        if (!profiles.exists()) {
            profiles.mkdir();
        }
        File login = new File(Constants.LOGIN_INFO);
        File name = new File(Constants.NAME_INFO);
        try {
            PrintWriter writeLogin = new PrintWriter(login);
            writeLogin.print(user.getLogin());
            writeLogin.close();
            PrintWriter writeName = new PrintWriter(name);
            writeName.print(handler.getNameByLoginAndPass(user));
            writeName.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        warningIncorrectlyData.setVisible(false);
    }

    public void singIn(ActionEvent actionEvent) {
        DBHandler handler = new DBHandler();
        User user = new User(loginField.getText(), passwordField.getText());
        if (handler.authorization(user)) {
            preConnect(handler, user);
            switcher.switchTo(CLIENT, pane);
        } else {
            warningIncorrectlyData.setVisible(true);
            ShakeAnimation shakeLogin = new ShakeAnimation(loginField);
            ShakeAnimation shakePass = new ShakeAnimation(passwordField);
            shakeLogin.playAnimation();
            shakePass.playAnimation();
        }
    }

    public void register(ActionEvent actionEvent) {
        switcher.switchTo(REGISTRATION, pane);
    }
}
