package com.example.foodtoqu;

public class UserHelperClass2 {
    private String fullName;
    private String username;
    private String age;
    private String gender;

    public UserHelperClass2() {
        // Default constructor required for Firebase
    }

    public UserHelperClass2(String fullName, String username, String age, String gender) {
        this.fullName = fullName;
        this.username = username;
        this.age = age;
        this.gender = gender;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
