package com.example.felix.nrfour;

/**
 * Datastructure that hold all information regarding a user.
 */

public class User {

    private String username;
    private String salt;
    private String hashedPassword;
    private String userID;
    private String IV;

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

    /**
     * Return the users username.
     * @return username
     */
    public String getUsername() { return username; }

    /**
     * Sets the users username
     * @param username
     */
    public void setUsername(String username) {
        username = username;
    }

    /**
     * Returns the users password (Hashed)
     * @return password
     */
    public String getPassword() {
        return hashedPassword;
    }

    /**
     * Sets the users password
     * @param set
     */
    public void setPassword(String set) {
        hashedPassword = BCrypt.hashpw(set, salt);
    }

    /**
     * Returns the users alocated salt.
     * @return salt
     */
    public String getSalt() {
        return salt;
    }

    /**
     * Sets the users salt.
     * @param salt
     */
    public void setSalt(String salt) { this.salt = salt; }

    /**
     * Return the users ID.
     * @return userID
     */
    public String getUserID() { return userID; }

    /**
     * Sets the users ID.
     * @param userID
     */
    public void setUserID(String userID) { this.userID = userID; }

    /**
     * Returns the users IV.
     * @return IV
     */
    public String getIV() {return IV;}

    /**
     * Sets the users IV.
     * @param iv
     */
    public void setIV(String iv) {IV = iv;}
}
