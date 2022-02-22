package com.kmetha.cloudStorage.server;

import com.kmetha.cloudstorage.core.action.ActionModel;
import com.kmetha.cloudstorage.core.action.models.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CloudStorageHandler extends SimpleChannelInboundHandler<ActionModel> {

    private Path currentDir;
    private Path clientDir;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        File dir = new File("data");
        if (!dir.exists()) {
            dir.mkdir();
        }
        currentDir = Paths.get("data");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, ActionModel actionModel) throws Exception {
        switch (actionModel.getType()) {
            case REQUEST -> downloadFileToClient(context, (RequestFile) actionModel);
            case TRANSFER -> {
                uploadFileFromClient((TransferFile) actionModel);
                printServerFiles(context);
            }
            case CHANGE_CURRENT_DIR -> {
                changeCurrentDir((ChangeCurrentDir) actionModel);
                printServerFiles(context);
            }
            case SELECT_CLIENT_DIR -> {
                selectClientDir((SelectClientDir) actionModel);
                printServerFiles(context);
            }
            case CREATE_FOLDER -> {
                createFolder((CreateFolderModel) actionModel);
                printServerFiles(context);
            }
        }
    }

    private void downloadFileToClient(ChannelHandlerContext context, RequestFile model) throws IOException {
        Path path = currentDir.resolve(model.getFileName());
        context.writeAndFlush(new TransferFile(path));
    }

    private void uploadFileFromClient(TransferFile model) throws IOException {
        Files.write(currentDir.resolve(model.getFileName()), model.getBytes());
    }

    private void printServerFiles(ChannelHandlerContext context) throws IOException {
        context.writeAndFlush(new PrintServerFiles(currentDir));
    }

    private void changeCurrentDir(ChangeCurrentDir model) {
        if (model.getPath().equals("#to_folder_above#")) {
            String folder = currentDir.getParent().toString();
            if (!currentDir.equals(clientDir)) {
                currentDir = Paths.get(folder);
            }
        } else {
            currentDir = Paths.get(currentDir.resolve(model.getPath()).toString());
        }
    }

    private void selectClientDir(SelectClientDir model) throws IOException {
        String clientDirPath = currentDir.resolve(model.getClientDir()).toString();
        File client = new File(clientDirPath);
        if (!client.exists()) {
            client.mkdir();
        }
        currentDir = Paths.get(clientDirPath);
        clientDir = Paths.get(clientDirPath);
    }

    private void createFolder(CreateFolderModel model) throws IOException {
        String folderPath = String.valueOf(currentDir.resolve(model.getNameFolder()));
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}