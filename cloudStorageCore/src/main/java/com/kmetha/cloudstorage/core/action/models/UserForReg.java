package com.kmetha.cloudstorage.core.action.models;

import com.kmetha.cloudstorage.core.action.ActionModel;
import com.kmetha.cloudstorage.core.action.CommandType;
import lombok.Data;

/**
 * Class for creating a user for registration
 */
@Data
public class UserForReg implements ActionModel {

    private String name;
    private String login;
    private String password;
    private boolean success = false;

    /**
     * For registration
     * @param name
     * @param login
     * @param password
     */
    public UserForReg(String name, String login, String password) {
        this.name = name;
        this.login = login;
        this.password = password;
    }

    @Override
    public CommandType getType() {
        return CommandType.USER_REG;
    }
}
