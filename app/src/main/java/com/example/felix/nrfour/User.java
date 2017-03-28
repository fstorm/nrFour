package com.example.felix.nrfour;

/**
 * Created by Felix on 17/02/2017.
 */

public class User {

    private String username;
    private String salt;
    private String hashedPassword;
    private String userID;

   // private Account accounts[] = ;

    public User(String username, String password, String salt, String userID) {
        this.username = username;
        this.salt = salt;
        this.hashedPassword = password;
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String set) {
        username = set;
    }

    public String getPassword() {
        return hashedPassword;
    }

    public void setPassword(String set) {
        hashedPassword = BCrypt.hashpw(set, salt);
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) { this.salt = salt; }

    public String getUserID() { return userID; }

    public void setUserID(String userID) { this.userID = userID; }
}
