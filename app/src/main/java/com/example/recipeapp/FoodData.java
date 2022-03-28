package com.example.recipeapp;

public class FoodData {
    private  String name;
    private String description;
    private String image;
    private String key;
    private String userId;

    public String getName() {
        return name;
    }

    public String getUserId() { return userId; }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public FoodData(String name, String description, String image) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.userId = userId;
    }

    public FoodData(){}
}
