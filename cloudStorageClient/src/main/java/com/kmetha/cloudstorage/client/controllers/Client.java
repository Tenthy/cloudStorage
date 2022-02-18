package com.kmetha.cloudstorage.client.controllers;

import com.kmetha.cloudstorage.client.constants.Constants;
import com.kmetha.cloudstorage.client.tools.CloseConnection;
import com.kmetha.cloudstorage.core.action.ActionModel;
import com.kmetha.cloudstorage.core.action.models.*;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Client implements Initializable {

    private static final int PORT = 8189;
    private static final String NAME = "localhost";

    private Socket socket;
    private ObjectDecoderInputStream is;
    private ObjectEncoderOutputStream os;
    private List<File> selectedFiles;
    private Path clientDir;

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
        Files.write(clientDir.resolve(model.getFileName()), model.getBytes());
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

    private void preConnect() throws IOException {
        filesTextArea.appendText("Welcome, " + Files.readString(Path.of(Constants.NAME_INFO)) + "!");
        String login = Files.readString(Path.of(Constants.LOGIN_INFO));
        os.writeObject(new SelectClientDir(login));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            socket = new Socket(NAME, PORT);
            os = new ObjectEncoderOutputStream(socket.getOutputStream()); //First
            is = new ObjectDecoderInputStream(socket.getInputStream()); //Second
            clientDir = Paths.get(System.getProperty("user.home"));
            Thread monitoringCommands = new Thread(this::monitoringCommands);
            monitoringCommands.setDaemon(true);
            monitoringCommands.start();
            preConnect();
            mouseClickAction();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            CloseConnection c = new CloseConnection(is, os, socket);
            c.closeConnection();
        }
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
                os.writeObject(new TransferFile(clientDir.resolve(path)));
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
}
