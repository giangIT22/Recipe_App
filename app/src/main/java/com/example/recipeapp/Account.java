package com.example.recipeapp;

import java.util.ArrayList;

public class Account {
    protected String username;
    protected String pass;
    private String key;

    public void setKey(String key) {
        this.key = key;
    }

    public Account(String username, String pass) {
        this.username = username;
        this.pass = pass;

    }

    public String getKey() {
        return key;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }


    public String getUsername() {
        return username;
    }

    public String getPass() {
        return pass;
    }


    public Account() {
    }
}
