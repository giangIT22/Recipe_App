package com.example.recipeapp;

public class Comment {
    private String content;
    private String userName;
    private String recipeId;

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public Comment(String content, String userName) {
        this.content = content;
        this.userName = userName;
    }

    public Comment(){}

    public void setContent(String content) {
        this.content = content;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public String getUserName() {
        return userName;
    }
}
