package com.example.foodtoqu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class profs extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private TextView usernameTextView;
    private TextView ageTextView;
    private TextView genderTextView;
    private ImageView categoryImageView;
    private TextView usernam2;
    private Button btnup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profs);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Check if there's a signed-in user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // No signed-in user, redirect to login or registration page
            startActivity(new Intent(profs.this, LoginActivity.class));
            finish(); // Finish the current activity to prevent the user from going back
            return; // Exit the onCreate method
        }

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("User");

        // Initialize UI elements
        usernam2 = findViewById(R.id.username2);
        usernameTextView = findViewById(R.id.username);
        ageTextView = findViewById(R.id.age);
        genderTextView = findViewById(R.id.gender);
        btnup = findViewById(R.id.update_btn);
        categoryImageView = findViewById(R.id.categoryImage);

        // Call the new function to retrieve user details
        retrieveUserDetails();


        btnup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), profileup.class);

                // Pass the retrieved data as extras
                intent.putExtra("name", usernameTextView.getText().toString());
                intent.putExtra("username", usernam2.getText().toString());
                intent.putExtra("age", ageTextView.getText().toString());
                intent.putExtra("gender", genderTextView.getText().toString());

                // Pass the image URL as an extra
                intent.putExtra("imageUrl", (String) categoryImageView.getTag());
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        });




        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();

        // Check the third item (index 2)
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        // Handle Home item click
                        Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_LONG).show();
                        Intent homeIntent = new Intent(getApplicationContext(), UserActivity.class);
                        startActivity(homeIntent);
                        overridePendingTransition(0, 0);
                        finish();
                        break;

                    case R.id.heart:
                        Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_LONG).show();
                        Intent homeIntent2 = new Intent(getApplicationContext(), LikedFoodsActivity.class);
                        startActivity(homeIntent2);
                        finish();
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.profile:
                        // Handle Quiz item click
                        Toast.makeText(getApplicationContext(), "profile", Toast.LENGTH_LONG).show();
                        Intent quizIntents = new Intent(getApplicationContext(), profs.class);
                        startActivity(quizIntents);
                        overridePendingTransition(0, 0);
                        finish();
                        break;

                    case R.id.logout:
                        // Handle Profile item click
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(profs.this);
                        builder.setTitle("Logout");
                        builder.setMessage("Are you sure you want to logout?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(profs.this, "Logout", Toast.LENGTH_SHORT).show();
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                                overridePendingTransition(0, 0);
                                startActivity(intent);
                                finish();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing
                            }
                        });
                        builder.show();
                        break;




                }
                return true;
            }
        });



    }





    private void retrieveUserDetails() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference studentRef = FirebaseDatabase.getInstance().getReference().child("User").child(userId);
        studentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String fullName = dataSnapshot.child("name").getValue(String.class);
                    String username = dataSnapshot.child("username").getValue(String.class);
                    Long ageLong = dataSnapshot.child("age").getValue(Long.class); // Retrieve age as Long
                    String gender = dataSnapshot.child("gender").getValue(String.class);
                    String imageUrl = dataSnapshot.child("image").getValue(String.class);
                    genderTextView.setText(gender);
                    usernam2.setText(username);
                    usernameTextView.setText(fullName);

                    // Convert Long age to integer and set it to the ageTextView
                    if (ageLong != null) {
                        int age = ageLong.intValue();
                        ageTextView.setText( String.valueOf(age));
                    } else {
                        // Handle the case if age data is missing
                    }

                    // Set the image URL to the ImageView's tag for later retrieval
                    categoryImageView.setTag(imageUrl);

                    // Load the image into the ImageView using a library like Picasso or Glide
                    // For example, using Picasso:
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Picasso.get().load(imageUrl).into(categoryImageView);
                    } else {
                        // Set a default drawable image if the image URL is empty
                        categoryImageView.setImageResource(R.drawable.ic_baseline_person_24);
                    }
                } else {
                    // Handle the case if student data does not exist
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error if necessary
            }
        });
    }
    public void onBackPressed(){
        Intent i = new Intent(getApplicationContext(),UserActivity.class);
        startActivity(i);
        finish();
        overridePendingTransition(0,0);
        super.onBackPressed();
    }
}