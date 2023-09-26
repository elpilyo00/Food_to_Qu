package com.example.foodtoqu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class prof2 extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    private DatabaseReference databaseReference;
    private TextView usernameTextView;
    private TextView ageTextView;
    private TextView genderTextView;
    private ImageView categoryImageView;
    private TextView usernam2;
    private Button btnup;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof2);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Check if there's a signed-in user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // No signed-in user, redirect to login or registration page
            startActivity(new Intent(prof2.this, LoginActivity.class));
            finish(); // Finish the current activity to prevent the user from going back
            return; // Exit the onCreate method
        }

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
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
                Intent intent = new Intent(getApplicationContext(), profileup2.class);

                // Pass the retrieved data as extras
                intent.putExtra("name", usernameTextView.getText().toString());
                intent.putExtra("username", usernam2.getText().toString());
                intent.putExtra("age", ageTextView.getText().toString());
                intent.putExtra("gender", genderTextView.getText().toString());

                // Pass the image URL as an extra
                intent.putExtra("imageUrl", (String) categoryImageView.getTag());

                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home: {
                        Toast.makeText(prof2.this, "Closed", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                        overridePendingTransition(0, 0); // Disable animation
                        startActivity(intent);
                        finish();

                        break;
                    }


                    case R.id.food: {
                        Toast.makeText(prof2.this, "Food Menu", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                        overridePendingTransition(0, 0); // Disable animation
                        startActivity(intent);
                        finish();
                        break;
                    }


                    case R.id.create: {
                        Toast.makeText(prof2.this, "User", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), RegAdmin.class);
                        overridePendingTransition(0, 0); // Disable animation
                        startActivity(intent);
                        finish();
                        break;
                    }


                    case R.id.profile: {
                        Toast.makeText(prof2.this, "User", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), prof2.class);
                        overridePendingTransition(0, 0); // Disable animation
                        startActivity(intent);
                        finish();
                        break;
                    }


                    case R.id.User: {
                        Toast.makeText(prof2.this, "User", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), User_search.class);
                        overridePendingTransition(0, 0); // Disable animation
                        startActivity(intent);
                        finish();
                        break;
                    }


                    case R.id.logout: {
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(prof2.this);
                        builder.setTitle("Logout");
                        builder.setMessage("Are you sure you want to logout?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(prof2.this, "Logout", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                FirebaseAuth.getInstance().signOut();
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

                }
                return false;

            }
        });


    }


    private void retrieveUserDetails() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference studentRef = FirebaseDatabase.getInstance().getReference().child("Admin").child(userId);
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
                        ageTextView.setText(String.valueOf(age));
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

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent i = new Intent(getApplicationContext(), AdminActivity.class);
            startActivity(i);
            finish();
            overridePendingTransition(0, 0);
            super.onBackPressed();
        }
    }
}