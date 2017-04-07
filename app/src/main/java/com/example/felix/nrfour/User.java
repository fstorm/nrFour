package com.example.felix.nrfour;

/**
 * Created by Felix on 17/02/2017.
 */

public class User {

    private String username;
    private String salt;
    private String hashedPassword;
    private String userID;
    private String IV;

   // private Account accounts[] = ;

    public User(String username, String password, String salt, String userID, String IV) {
        this.username = username;
        this.salt = salt;
        this.hashedPassword = password;
        this.userID = userID;
        this.IV = IV;
    }

    public User(String username, String password, String salt, String IV) {
        this.username = username;
        this.salt = salt;
        this.hashedPassword = password;
        this.IV = IV;
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

    public String getIV() {return IV;}

    public void setIV(String iv) {IV = iv;}
}
