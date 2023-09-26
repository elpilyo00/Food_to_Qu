package com.example.foodtoqu;

import com.google.firebase.database.PropertyName;

public class Food2 {
    private String foodName;
    private String calorie;
    private String totalFat;
    private String cholesterol;
    private String sodium;
    private String carbo;
    private String totalSugar;
    private String protein;
    private String imageUrl;

    // Rename the fields using @PropertyName annotation
    @PropertyName("Diabetes")
    private boolean diabetes;
    @PropertyName("Gastrointestinal Disorder")
    private boolean gastrointestinal;
    @PropertyName("Irritable Bowel Syndrome")
    private boolean bowel;
    @PropertyName("High Blood Pressure")
    private boolean highBlood;
    @PropertyName("Weight Management")
    private boolean weight;
    @PropertyName("Anemia")
    private boolean anemia;
    @PropertyName("High Cholesterol")
    private boolean highCholesterol;
    @PropertyName("Heart Disease")
    private boolean heartDisease;
    @PropertyName("Osteoporosis")
    private boolean osteoporosis;
    @PropertyName("Celiac Disease")
    private boolean celiac;
    @PropertyName("Renal Disease")
    private boolean renal;
    @PropertyName("Hypothyroidism")
    private boolean hypothyroidism;
    @PropertyName("Happy")
    private boolean happy;
    @PropertyName("Sad")
    private boolean sad;
    @PropertyName("Angry")
    private boolean angry;
    @PropertyName("Stress")
    private boolean stress;
    @PropertyName("Excited")
    private boolean excited;
    @PropertyName("Nostalgia")
    private boolean nostalgia;
    @PropertyName("In Love")
    private boolean inLove;
    @PropertyName("Calm")
    private boolean calm;

    // Default constructor (required for Firebase)
    public Food2() {
    }

    public Food2(String foodName, String calorie, String totalFat, String cholesterol, String sodium, String carbo, String totalSugar, String protein, String imageUrl, boolean diabetes, boolean gastrointestinal, boolean bowel, boolean highBlood, boolean weight, boolean anemia, boolean highCholesterol, boolean heartDisease, boolean osteoporosis, boolean celiac, boolean renal, boolean hypothyroidism, boolean happy, boolean sad, boolean angry, boolean stress, boolean excited, boolean nostalgia, boolean inLove, boolean calm) {
        this.foodName = foodName;
        this.calorie = calorie;
        this.totalFat = totalFat;
        this.cholesterol = cholesterol;
        this.sodium = sodium;
        this.carbo = carbo;
        this.totalSugar = totalSugar;
        this.protein = protein;
        this.imageUrl = imageUrl;
        this.diabetes = diabetes;
        this.gastrointestinal = gastrointestinal;
        this.bowel = bowel;
        this.highBlood = highBlood;
        this.weight = weight;
        this.anemia = anemia;
        this.highCholesterol = highCholesterol;
        this.heartDisease = heartDisease;
        this.osteoporosis = osteoporosis;
        this.celiac = celiac;
        this.renal = renal;
        this.hypothyroidism = hypothyroidism;
        this.happy = happy;
        this.sad = sad;
        this.angry = angry;
        this.stress = stress;
        this.excited = excited;
        this.nostalgia = nostalgia;
        this.inLove = inLove;
        this.calm = calm;
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

    public boolean isDiabetes() {
        return diabetes;
    }

    public void setDiabetes(boolean diabetes) {
        this.diabetes = diabetes;
    }

    public boolean isGastrointestinal() {
        return gastrointestinal;
    }

    public void setGastrointestinal(boolean gastrointestinal) {
        this.gastrointestinal = gastrointestinal;
    }

    public boolean isBowel() {
        return bowel;
    }

    public void setBowel(boolean bowel) {
        this.bowel = bowel;
    }

    public boolean isHighBlood() {
        return highBlood;
    }

    public void setHighBlood(boolean highBlood) {
        this.highBlood = highBlood;
    }

    public boolean isWeight() {
        return weight;
    }

    public void setWeight(boolean weight) {
        this.weight = weight;
    }

    public boolean isAnemia() {
        return anemia;
    }

    public void setAnemia(boolean anemia) {
        this.anemia = anemia;
    }

    public boolean isHighCholesterol() {
        return highCholesterol;
    }

    public void setHighCholesterol(boolean highCholesterol) {
        this.highCholesterol = highCholesterol;
    }

    public boolean isHeartDisease() {
        return heartDisease;
    }

    public void setHeartDisease(boolean heartDisease) {
        this.heartDisease = heartDisease;
    }

    public boolean isOsteoporosis() {
        return osteoporosis;
    }

    public void setOsteoporosis(boolean osteoporosis) {
        this.osteoporosis = osteoporosis;
    }

    public boolean isCeliac() {
        return celiac;
    }

    public void setCeliac(boolean celiac) {
        this.celiac = celiac;
    }

    public boolean isRenal() {
        return renal;
    }

    public void setRenal(boolean renal) {
        this.renal = renal;
    }

    public boolean isHypothyroidism() {
        return hypothyroidism;
    }

    public void setHypothyroidism(boolean hypothyroidism) {
        this.hypothyroidism = hypothyroidism;
    }

    public boolean isHappy() {
        return happy;
    }

    public void setHappy(boolean happy) {
        this.happy = happy;
    }

    public boolean isSad() {
        return sad;
    }

    public void setSad(boolean sad) {
        this.sad = sad;
    }

    public boolean isAngry() {
        return angry;
    }

    public void setAngry(boolean angry) {
        this.angry = angry;
    }

    public boolean isStress() {
        return stress;
    }

    public void setStress(boolean stress) {
        this.stress = stress;
    }

    public boolean isExcited() {
        return excited;
    }

    public void setExcited(boolean excited) {
        this.excited = excited;
    }

    public boolean isNostalgia() {
        return nostalgia;
    }

    public void setNostalgia(boolean nostalgia) {
        this.nostalgia = nostalgia;
    }

    public boolean isInLove() {
        return inLove;
    }

    public void setInLove(boolean inLove) {
        this.inLove = inLove;
    }

    public boolean isCalm() {
        return calm;
    }

    public void setCalm(boolean calm) {
        this.calm = calm;
    }

    // Other getters and setters...
}
