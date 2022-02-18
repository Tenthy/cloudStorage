package com.kmetha.cloudstorage.core.action.models;

import com.kmetha.cloudstorage.core.action.ActionModel;
import com.kmetha.cloudstorage.core.action.CommandType;
import lombok.Data;

/**
 * The class was created to request a file. It is mainly used when the Client needs to download a file from the Server.
 */
@Data
public class RequestFile implements ActionModel {

    private final String fileName;

    @Override
    public CommandType getType() {
        return CommandType.REQUEST;
    }
}
