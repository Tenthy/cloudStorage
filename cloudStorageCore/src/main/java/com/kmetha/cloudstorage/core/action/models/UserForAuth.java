package com.kmetha.cloudstorage.core.action.models;

import com.kmetha.cloudstorage.core.action.ActionModel;
import com.kmetha.cloudstorage.core.action.CommandType;
import lombok.Data;

/**
 * Class for creating a user for authorization
 */
@Data
public class UserForAuth implements ActionModel {

    private String name;
    private String login;
    private String password;
    private boolean success = false;

    /**
     * For authorization
     * @param login
     * @param password
     */
    public UserForAuth(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Override
    public CommandType getType() {
        return CommandType.USER_AUTH;
    }
}
