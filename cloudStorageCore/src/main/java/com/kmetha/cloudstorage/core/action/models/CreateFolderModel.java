package com.kmetha.cloudstorage.core.action.models;

import com.kmetha.cloudstorage.core.action.ActionModel;
import com.kmetha.cloudstorage.core.action.CommandType;
import lombok.Data;

@Data
public class CreateFolderModel implements ActionModel {

    private String nameFolder;

    public CreateFolderModel(String nameFolder) {
        this.nameFolder = nameFolder;
    }

    @Override
    public CommandType getType() {
        return CommandType.CREATE_FOLDER;
    }
}
