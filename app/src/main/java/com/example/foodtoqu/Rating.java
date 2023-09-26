package com.example.foodtoqu;
public class Rating {
    private String userUid;
    private float rating;

    public Rating() {
        // Default constructor required for DataSnapshot.getValue(Rating.class)
    }

    public Rating(String userUid, float rating) {
        this.userUid = userUid;
        this.rating = rating;
    }

    public String getUserUid() {
        return userUid;
    }

    public float getRating() {
        return rating;
    }
}
