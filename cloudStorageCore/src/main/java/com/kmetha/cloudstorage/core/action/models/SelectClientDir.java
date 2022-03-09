package com.kmetha.cloudstorage.core.action.models;

import com.kmetha.cloudstorage.core.action.ActionModel;
import com.kmetha.cloudstorage.core.action.CommandType;
import lombok.Data;

/**
 * Class is used before working with the repository and a separate directory is created for each user.
 * Class is used only once when the client establishes a connection with the server.
 */
@Data
public class SelectClientDir implements ActionModel {

    private String clientDir;

    public SelectClientDir(String clientDir) {
        this.clientDir = clientDir;
    }

    @Override
    public CommandType getType() {
        return CommandType.SELECT_CLIENT_DIR;
    }
}
