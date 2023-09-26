package com.example.foodtoqu;
public class NutritionalValues {
    public float calorie;
    public float totalFat;
    public float cholesterol;
    public float sodium;
    public float carbo;
    public float totalSugar;
    public float protein;

    public NutritionalValues() {
        // Default constructor required for Firebase
    }

    public NutritionalValues(float calorie, float totalFat, float cholesterol, float sodium, float carbo, float totalSugar, float protein) {
        this.calorie = calorie;
        this.totalFat = totalFat;
        this.cholesterol = cholesterol;
        this.sodium = sodium;
        this.carbo = carbo;
        this.totalSugar = totalSugar;
        this.protein = protein;
    }
}
