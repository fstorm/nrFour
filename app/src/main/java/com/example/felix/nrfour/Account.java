package com.example.felix.nrfour;

import java.io.Serializable;

/**
 * Created by Felix on 17/02/2017.
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

    public String getAccountName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNote() {
        return note;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
