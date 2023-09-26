package com.example.foodtoqu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
public class User_search extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    RecyclerView userview;
    DatabaseReference databaseReference;
    List<User3> userList;
    List<User3> originalUserList; // Store the original data here
    UserAdapte2 userAdapter;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        userview = findViewById(R.id.userView);
        userList = new ArrayList<>();
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        originalUserList = new ArrayList<>(); // Initialize the original data list
        userAdapter = new UserAdapte2(userList, this);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");

        userview.setLayoutManager(new LinearLayoutManager(this));
        userview.setAdapter(userAdapter);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home: {
                        Toast.makeText(User_search.this, "Closed", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                        overridePendingTransition(0, 0); // Disable animation
                        startActivity(intent);
                        finish();

                        break;
                    }

                    case R.id.food: {
                        Toast.makeText(User_search.this, "Food Menu", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                        overridePendingTransition(0, 0); // Disable animation
                        startActivity(intent);
                        finish();
                        break;
                    }


                    case R.id.create: {
                        Toast.makeText(User_search.this, "User", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), RegAdmin.class);
                        overridePendingTransition(0, 0); // Disable animation
                        startActivity(intent);
                        finish();
                        break;
                    }


                    case R.id.profile: {
                        Toast.makeText(User_search.this, "User", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), prof2.class);
                        overridePendingTransition(0, 0); // Disable animation
                        startActivity(intent);
                        finish();
                        break;
                    }


                    case R.id.User: {
                        Toast.makeText(User_search.this, "User", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), User_search.class);
                        overridePendingTransition(0, 0); // Disable animation
                        startActivity(intent);
                        finish();
                        break;
                    }


                    case R.id.logout: {
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(User_search.this);
                        builder.setTitle("Logout");
                        builder.setMessage("Are you sure you want to logout?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(User_search.this, "Logout", Toast.LENGTH_SHORT).show();
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


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                originalUserList.clear(); // Clear the original data list

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User3 user = snapshot.getValue(User3.class);
                    userList.add(user);
                    originalUserList.add(user); // Add data to the original list
                }

                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });

        searchView = findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the list by username when the user types in the SearchView
                filterUserListByUsername(newText);
                return true;
            }
        });
    }

    private void filterUserListByUsername(String searchText) {
        if (searchText.isEmpty()) {
            // If the search text is empty, restore the original data
            userAdapter.updateList(originalUserList);
        } else {
            String searchQuery = searchText.toLowerCase();

            List<User3> filteredList = new ArrayList<>();
            for (User3 user : userList) {
                String username = user.getUsername() != null ? user.getUsername().toLowerCase() : "";

                if (username.contains(searchQuery)) {
                    filteredList.add(user);
                }
            }

            userAdapter.updateList(filteredList);
        }
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent i = new Intent(getApplicationContext(),AdminActivity.class);
            startActivity(i);
            finish();
            super.onBackPressed();
        }
    }

}
