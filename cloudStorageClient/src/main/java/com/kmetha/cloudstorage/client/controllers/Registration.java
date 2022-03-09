package com.kmetha.cloudstorage.client.controllers;

import com.kmetha.cloudstorage.client.tools.SceneSwitcher;
import com.kmetha.cloudstorage.core.action.models.UserForReg;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class Registration {

    private static final String AUTHORIZATION = "authorization.fxml";
    private final SceneSwitcher switcher = new SceneSwitcher();

    private ObjectEncoderOutputStream os;
    private ObjectDecoderInputStream is;

    public AnchorPane pane;
    public TextField nameField;
    public TextField loginField;
    public PasswordField passwordField;
    public PasswordField repeatedPasswordField;
    public Label warningIncorrectlyData;
    public Label warningLoginAlreadyUse;

    public void initialize(ObjectEncoderOutputStream os, ObjectDecoderInputStream is) {
        this.os = os;
        this.is = is;
        warningIncorrectlyData.setVisible(false);
        warningLoginAlreadyUse.setVisible(false);
    }

    private void monitoringSuccess() {
        try {
            UserForReg model = (UserForReg) is.readObject();
            if (model.isSuccess()) {
                Platform.runLater(() -> {
                    switcher.switchTo(AUTHORIZATION, pane);
                });
            } else {
                warningIncorrectlyData.setVisible(false);
                warningLoginAlreadyUse.setVisible(true);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Below this javadoc are methods that are linking to JavaFX objects (Buttons)
     */

    public void register(ActionEvent actionEvent) throws IOException {
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
            Thread monitoringCommand = new Thread(this::monitoringSuccess);
            monitoringCommand.setDaemon(true);
            monitoringCommand.start();
            os.writeObject(new UserForReg(name, login, pass));
        }
    }

    public void close(ActionEvent actionEvent) {
        switcher.switchTo(AUTHORIZATION, pane);
    }
}
