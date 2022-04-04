package com.example.recipeapp;

public class MyStep {
    private String stepDescription;

    public MyStep(String stepDescription) {
        this.stepDescription = stepDescription;
    }

    public void setStepDescription(String stepDescription) {
        this.stepDescription = stepDescription;
    }

    public String getStepDescription() {
        return stepDescription;
    }

    public MyStep(){}
}
