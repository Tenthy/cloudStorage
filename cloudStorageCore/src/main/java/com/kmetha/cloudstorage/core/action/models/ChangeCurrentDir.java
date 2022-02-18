package com.kmetha.cloudstorage.core.action.models;

import com.kmetha.cloudstorage.core.action.ActionModel;
import com.kmetha.cloudstorage.core.action.CommandType;
import lombok.Data;

/**
 * Shows the contents of the selected folder on the Client
 */
@Data
public class ChangeCurrentDir implements ActionModel {

    private String path;

    public ChangeCurrentDir(String path) {
        this.path = path;
    }

    @Override
    public CommandType getType() {
        return CommandType.CHANGE_CURRENT_DIR;
    }
}
