package com.example.foodtoqu;

public class User2 {
    private String fullName;
    private String email;
    private String age;
    private String gender;

    // Default constructor required for Firebase Realtime Database
    public User2() {
    }

    public User2(String fullName, String email, String age, String gender) {
        this.fullName = fullName;
        this.email = email;
        this.age = age;
        this.gender = gender;
    }

    // Getter and Setter methods for class fields
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
