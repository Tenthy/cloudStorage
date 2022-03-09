package com.kmetha.cloudstorage.client.controllers;

import com.kmetha.cloudstorage.core.action.ActionModel;
import com.kmetha.cloudstorage.core.action.models.*;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Client {

    private final int PORT = 8189;
    private final String NAME = "192.168.0.101";
    private final String CREATE_NEW_FOLDER = "createFolder.fxml";

    private Socket socket;
    private ObjectEncoderOutputStream os;
    private ObjectDecoderInputStream is;
    private String nameClient;
    private String loginClient;
    private List<File> selectedFiles;
    private Path downloadDir;

    public TextArea filesTextArea;
    public ListView<String> serverFilesList;

    private void monitoringCommands() {
        try {
            while (true) {
                ActionModel model = (ActionModel) is.readObject();
                switch (model.getType()) {
                    case PRINT_SERVER_FILES -> printServerFiles((PrintServerFiles) model);
                    case TRANSFER -> downloadFileFromServer((TransferFile) model);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void printServerFiles(PrintServerFiles model) {
        Platform.runLater(() -> {
            serverFilesList.getItems().clear();
            serverFilesList.getItems().addAll(model.getFiles());
        });
    }

    private void downloadFileFromServer(TransferFile model) throws IOException {
        Files.write(downloadDir.resolve(model.getFileName()), model.getBytes());
    }

    private void mouseClickAction() {
        serverFilesList.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                String selectedFolder = serverFilesList.getSelectionModel().getSelectedItem();
                try {
                    os.writeObject(new ChangeCurrentDir(selectedFolder));
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        });
    }

    private void connectingToTheClientDirectory(String nameClient, String loginClient) throws IOException {
        filesTextArea.appendText("Welcome, " + nameClient + "!");
        os.writeObject(new SelectClientDir(loginClient));
    }

    public void initialize(Socket socket, ObjectEncoderOutputStream os, ObjectDecoderInputStream is, String nameClient, String loginClient) {
        this.socket = socket;
        this.os = os;
        this.is = is;
        this.nameClient = nameClient;
        this.loginClient = loginClient;
        downloadDir = Paths.get(System.getProperty("user.home"));
        Thread monitoringCommands = new Thread(this::monitoringCommands);
        monitoringCommands.setDaemon(true);
        monitoringCommands.start();
        try {
            connectingToTheClientDirectory(nameClient, loginClient);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        mouseClickAction();
    }

    /**
     * Below this javadoc are methods that are linking to JavaFX objects (Buttons)
     */

    public void getFiles(ActionEvent actionEvent) {
        filesTextArea.clear();
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("All files", "*.*"));
        selectedFiles = fc.showOpenMultipleDialog(null);
        for (File file : selectedFiles) {
            filesTextArea.appendText(file.getAbsolutePath() + "\n");
        }
    }

    public void upload(ActionEvent actionEvent) throws IOException {
        if (!selectedFiles.isEmpty()) {
            for (File file : selectedFiles) {
                String path = file.getAbsolutePath();
                os.writeObject(new TransferFile(downloadDir.resolve(path)));
            }
            filesTextArea.clear();
            selectedFiles = new ArrayList<>();
        }
    }

    public void download(ActionEvent actionEvent) throws IOException {
        String file = serverFilesList.getSelectionModel().getSelectedItem();
        os.writeObject(new RequestFile(file));
        filesTextArea.clear();
        filesTextArea.appendText("File " + file + " has been downloaded (^_^)");
    }

    public void toFolderAbove(ActionEvent actionEvent) throws IOException {
        String command = "#to_folder_above#";
        os.writeObject(new ChangeCurrentDir(command));
    }

    public void createFolder(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(CREATE_NEW_FOLDER));
        Parent root = loader.load();
        CreateFolder cf = loader.getController();
        cf.initStream(os);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setTitle("Create a new folder");
        stage.show();
    }
}
