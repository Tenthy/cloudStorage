package com.kmetha.cloudstorage.core.action.models;

import com.kmetha.cloudstorage.core.action.ActionModel;
import com.kmetha.cloudstorage.core.action.CommandType;
import lombok.Data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Class was created for copying and transfer files between the Server and the Client
 */
@Data
public class TransferFile implements ActionModel {

    private final String fileName;
    private final byte[] bytes;

    public TransferFile(Path path) throws IOException {
        fileName = path.getFileName().toString();
        bytes = Files.readAllBytes(path);
    }

    @Override
    public CommandType getType() {
        return CommandType.TRANSFER;
    }
}
