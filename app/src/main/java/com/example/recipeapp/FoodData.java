package com.example.recipeapp;

import java.io.Serializable;
import java.util.ArrayList;

public class FoodData implements Serializable {
    private  String name;
    private String description;
    private String image;
    private String key;
    private String userId;
    private ArrayList<String> ingredients;
    private ArrayList<MyStep> stepList;

    public ArrayList<MyStep> getStepList() {
        return stepList;
    }

    public void setStepList(ArrayList<MyStep> stepList) {
        this.stepList = stepList;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

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

    public FoodData(String name, String description, String image, ArrayList<MyStep> stepList) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.stepList = stepList;
    }

    public FoodData(){}
}
