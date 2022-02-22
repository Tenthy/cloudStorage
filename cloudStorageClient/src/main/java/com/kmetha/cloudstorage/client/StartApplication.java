package com.kmetha.cloudstorage.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartApplication extends Application {

    private static final String AUTH = "authorization.fxml";

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(AUTH));
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setTitle("Cloud Storage");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
