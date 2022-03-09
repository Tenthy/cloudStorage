package com.kmetha.cloudstorage.client.tools;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Class is used to transition between scenes JavaFX
 */

public class SceneSwitcher {

    public SceneSwitcher() {
    }

    public void switchTo(String pathFXML, AnchorPane pane) {
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource(pathFXML)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) pane.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setTitle("Cloud Storage");
        stage.show();
    }
}
