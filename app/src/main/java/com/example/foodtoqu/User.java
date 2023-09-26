package com.example.foodtoqu;

public class User {
    private String name;
    private String age;
    private String gender;
    private String username;
    private String password;
    private String imageUrl; // New field for the image URL

    // Default constructor (required for Firebase)
    public User() {
    }

    public User(String name, String age, String gender, String username, String password, String imageUrl) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.username = username;
        this.password = password;
        this.imageUrl = imageUrl;
    }

    // Getter and setter methods for each field
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
