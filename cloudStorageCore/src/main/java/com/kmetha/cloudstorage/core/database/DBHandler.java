package com.kmetha.cloudstorage.core.database;

import java.sql.*;

public class DBHandler extends DBConfiguration {

    public static final String TABLE_USERS = "users";
    public static final String USER_ID = "id";
    public static final String USER_NAME = "name";
    public static final String USER_LOGIN = "login";
    public static final String USER_PASS = "password";

    Connection connection;

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        String connectionUrl = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection(connectionUrl, dbUser, dbPass);
        return connection;
    }

    public boolean tryToRegistration(User user) {
        if (availabilityLogin(user.getLogin())) {
            String query = "INSERT INTO " + TABLE_USERS + "(" +
                    USER_NAME + "," +
                    USER_LOGIN + "," +
                    USER_PASS + ")" +
                    "VALUES(?,?,?)";
            try {
                PreparedStatement statement = getConnection().prepareStatement(query);
                statement.setString(1, user.getName());
                statement.setString(2, user.getLogin());
                statement.setString(3, user.getPassword());
                statement.executeUpdate();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    private boolean availabilityLogin(String login) {
        String query = "SELECT * FROM " + TABLE_USERS;
        try {
            Statement statement = getConnection().createStatement();
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                String check = result.getString(USER_LOGIN);
                if (check.equals(login)) {
                    return false;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean authorization(User user) {
        String query = "SELECT * FROM " + TABLE_USERS;
        try {
            Statement statement = getConnection().createStatement();
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                String loginCheck = result.getString(USER_LOGIN);
                String passwordCheck = result.getString(USER_PASS);
                if (loginCheck.equals(user.getLogin()) && passwordCheck.equals(user.getPassword())) {
                    return true;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getNameByLoginAndPass(User user) {
        String query = "SELECT * FROM " + TABLE_USERS;
        try {
            Statement statement = getConnection().createStatement();
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                if (user.getLogin().equals(result.getString(USER_LOGIN)) &&
                        user.getPassword().equals(result.getString(USER_PASS))) {
                    return result.getString(USER_NAME);
                };
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
