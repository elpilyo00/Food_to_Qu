package com.example.foodtoqu;
import java.util.Map;

public class Food {
    private String foodId; // Add a unique identifier field
    private String foodName;
    private String calorie;
    private String totalFat;
    private String cholesterol;
    private String sodium;
    private String carbo;
    private String totalSugar;
    private String protein;
    private String imageUrl;
    private Map<String, Boolean> healthConditions;
    private Map<String, Boolean> moods;
    private String description;


    public Food() {

    }

    // Default constructor (required for Firebase)

    public Food(String foodId, String foodName, String calorie, String totalFat, String cholesterol, String sodium, String carbo, String totalSugar, String protein, String imageUrl, Map<String, Boolean> healthConditions, Map<String, Boolean> moods, String description) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.calorie = calorie;
        this.totalFat = totalFat;
        this.cholesterol = cholesterol;
        this.sodium = sodium;
        this.carbo = carbo;
        this.totalSugar = totalSugar;
        this.protein = protein;
        this.imageUrl = imageUrl;
        this.healthConditions = healthConditions;
        this.moods = moods;
        this.description = description;
    }

    public String getFoodId() {
        return foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public String getCalorie() {
        return calorie;
    }

    public String getTotalFat() {
        return totalFat;
    }

    public String getCholesterol() {
        return cholesterol;
    }

    public String getSodium() {
        return sodium;
    }

    public String getCarbo() {
        return carbo;
    }

    public String getTotalSugar() {
        return totalSugar;
    }

    public String getProtein() {
        return protein;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Map<String, Boolean> getHealthConditions() {
        return healthConditions;
    }

    public Map<String, Boolean> getMoods() {
        return moods;
    }
    public String getDescription() {return description;}
}
