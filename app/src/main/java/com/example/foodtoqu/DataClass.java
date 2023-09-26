package com.example.foodtoqu;

public class DataClass {
    private String dataName, dataCalorie, dataFat, dataCholesterol, dataSodium, dataCarbo, dataSugar, dataProtein, dataImage, key;

    public DataClass(String dataName, String dataCalorie, String dataFat, String dataCholesterol, String dataSodium, String dataCarbo, String dataSugar, String dataProtein, String dataImage) {
        this.dataName = dataName;
        this.dataCalorie = dataCalorie;
        this.dataFat = dataFat;
        this.dataCholesterol = dataCholesterol;
        this.dataSodium = dataSodium;
        this.dataCarbo = dataCarbo;
        this.dataSugar = dataSugar;
        this.dataProtein = dataProtein;
        this.dataImage = dataImage;
    }

    public DataClass(){

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDataName() {
        return dataName;
    }

    public String getDataCalorie() {
        return dataCalorie;
    }

    public String getDataFat() {
        return dataFat;
    }

    public String getDataCholesterol() {
        return dataCholesterol;
    }

    public String getDataSodium() {
        return dataSodium;
    }

    public String getDataCarbo() {
        return dataCarbo;
    }

    public String getDataSugar() {
        return dataSugar;
    }

    public String getDataProtein() {
        return dataProtein;
    }

    public String getDataImage() {
        return dataImage;
    }
}
