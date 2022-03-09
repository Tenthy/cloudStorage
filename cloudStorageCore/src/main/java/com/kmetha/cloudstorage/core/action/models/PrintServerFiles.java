package com.kmetha.cloudstorage.core.action.models;

import com.kmetha.cloudstorage.core.action.ActionModel;
import com.kmetha.cloudstorage.core.action.CommandType;
import lombok.Data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple query class to display the currently existing files on the Server
 */
@Data
public class PrintServerFiles implements ActionModel {

    private final List<String> files;

    public PrintServerFiles(Path path) throws IOException {
        files = Files.list(path)
                .map(p -> p.getFileName().toString())
                .collect(Collectors.toList());
    }

    @Override
    public CommandType getType() {
        return CommandType.PRINT_SERVER_FILES;
    }
}
