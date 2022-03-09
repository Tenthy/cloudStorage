package com.kmetha.cloudstorage.client.controllers;

import com.kmetha.cloudstorage.client.tools.SceneSwitcher;
import com.kmetha.cloudstorage.client.animation.ShakeAnimation;
import com.kmetha.cloudstorage.core.action.models.UserForAuth;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Authorization implements Initializable {

    private final String CLIENT = "client.fxml";
    private final String REGISTRATION = "registration.fxml";
    private final int PORT = 8189;
    private final String ADDRESS = "192.168.0.101";
    private final SceneSwitcher switcher = new SceneSwitcher();

    private Socket socket;
    private ObjectEncoderOutputStream os;
    private ObjectDecoderInputStream is;

    public AnchorPane pane;
    public Button singIn;
    public Button register;
    public Label warningIncorrectlyData;
    public TextField loginField;
    public PasswordField passwordField;

    private void monitoringSuccess() {
        try {
            UserForAuth model = (UserForAuth) is.readObject();
            if (model.isSuccess()) {
                authorization(model);
            } else {
                warningIncorrectlyData.setVisible(true);
                ShakeAnimation shakeLogin = new ShakeAnimation(loginField);
                ShakeAnimation shakePass = new ShakeAnimation(passwordField);
                shakeLogin.playAnimation();
                shakePass.playAnimation();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void authorization(UserForAuth model) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(CLIENT));
                Parent root = loader.load();
                Client client = loader.getController();
                client.initialize(socket, os, is, model.getName(), model.getLogin());
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.setTitle("Cloud Storage Client");
                stage.show();
                Stage auth = (Stage) pane.getScene().getWindow();
                auth.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        warningIncorrectlyData.setVisible(false);
        try {
            socket = new Socket(ADDRESS, PORT);
            os = new ObjectEncoderOutputStream(socket.getOutputStream());
            is = new ObjectDecoderInputStream(socket.getInputStream());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Below this javadoc are methods that are linking to JavaFX objects (Buttons)
     */

    public void singIn(ActionEvent actionEvent) throws IOException {
        String login = loginField.getText();
        String pass = passwordField.getText();
        if (login.isBlank() || pass.isBlank()) {
            warningIncorrectlyData.setVisible(true);
            ShakeAnimation shakeLogin = new ShakeAnimation(loginField);
            ShakeAnimation shakePass = new ShakeAnimation(passwordField);
            shakeLogin.playAnimation();
            shakePass.playAnimation();
        } else {
            Thread monitoringCommand = new Thread(this::monitoringSuccess);
            monitoringCommand.setDaemon(true);
            monitoringCommand.start();
            os.writeObject(new UserForAuth(login, pass));
        }
    }

    public void register(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(REGISTRATION));
            Parent root = loader.load();
            Registration reg = loader.getController();
            reg.initialize(os, is);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.setTitle("Cloud Storage Registration");
            stage.show();
            Stage auth = (Stage) pane.getScene().getWindow();
            auth.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
