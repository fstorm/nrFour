package com.example.felix.nrfour;

import java.io.Serializable;

/**
 * Datastructure used to record and retrieve account information
 *  Works as the model of the account.
 */
public class Account implements Serializable{

    private String name, username, password, note;


    public Account(String name, String username, String password, String note) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.note = note;
    }

    public Account(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    /**
     * Returns the account name.
     * @return account name
     */
    public String getAccountName() {
        return name;
    }

    /**
     * Return the username associated with the account.
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returnes the password associated with the account.
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returnes the note associated with the account.
     * @return note
     */
    public String getNote() {
        return note;
    }

    /**
     * Allows for chainging of the account name.
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Allows for changing of the username.
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Allows for chainging the password
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Allows for chainging the note
     * @param note
     */
    public void setNote(String note) {
        this.note = note;
    }
}
