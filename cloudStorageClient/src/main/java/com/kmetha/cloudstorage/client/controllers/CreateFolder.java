package com.kmetha.cloudstorage.client.controllers;

import com.kmetha.cloudstorage.core.action.models.CreateFolderModel;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CreateFolder implements Serializable {

    private ObjectEncoderOutputStream os;

    public AnchorPane pane;
    public TextField nameFolderField;

    public void initStream(ObjectEncoderOutputStream os) {
        this.os = os;
    }

    public void create(ActionEvent actionEvent) throws IOException {
        String nameFolder = nameFolderField.getText();
        if (!nameFolder.trim().isBlank()) {
            os.writeObject(new CreateFolderModel(nameFolder));
            Stage stage = (Stage) pane.getScene().getWindow();
            stage.close();
        }
    }
}
