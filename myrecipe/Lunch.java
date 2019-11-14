package com.example.user.myrecipe;

public class Lunch {

    private String UserID, Username, RecipeName, Image, Ingredient, Preparation, Duration, Serving;

    public Lunch() {

    }

    public Lunch(String userID, String username, String recipeName, String image, String ingredient, String preparation, String duration, String serving) {
        UserID = userID;
        Username = username;
        RecipeName = recipeName;
        Image = image;
        Ingredient = ingredient;
        Preparation = preparation;
        Duration = duration;
        Serving = serving;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getRecipeName() {
        return RecipeName;
    }

    public void setRecipeName(String recipeName) {
        RecipeName = recipeName;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getIngredient() {
        return Ingredient;
    }

    public void setIngredient(String ingredient) {
        Ingredient = ingredient;
    }

    public String getPreparation() {
        return Preparation;
    }

    public void setPreparation(String preparation) {
        Preparation = preparation;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public String getServing() {
        return Serving;
    }

    public void setServing(String serving) {
        Serving = serving;
    }
}