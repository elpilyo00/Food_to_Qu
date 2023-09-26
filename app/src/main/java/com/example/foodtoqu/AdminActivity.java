package com.example.foodtoqu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    FloatingActionButton fab;
    RecyclerView recyclerView;
    private SearchView searchView;
    private DatabaseReference databaseReference;
    private List<FoodItem> foodItemList;
    private FoodAdapter foodAdapter;
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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recyclerView);
        drawerToggle.syncState();
        searchView = findViewById(R.id.search);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(AdminActivity.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        databaseReference = FirebaseDatabase.getInstance().getReference("foods");

        // Initialize RecyclerView and adapter
        foodItemList = new ArrayList<>();
        foodAdapter = new FoodAdapter(this, foodItemList);

// Set the adapter to your RecyclerView
        recyclerView.setAdapter(foodAdapter);

        // Retrieve data from Firebase and populate the RecyclerView
        retrieveDataFromFirebase();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),UploadActivity.class);
                startActivity(i);
                finish();
            }
        });
        
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform the search when the user submits the query
                filterData(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Perform the search as the user types (optional)
                filterData(newText);
                return true;
            }
        });
    
        
        
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home: {
                        Toast.makeText(AdminActivity.this, "Closed", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                        overridePendingTransition(0, 0); // Disable animation
                        startActivity(intent);
                        finish();

                        break;
                    }


                    case R.id.food: {
                        Toast.makeText(AdminActivity.this, "Food Menu", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                        overridePendingTransition(0, 0); // Disable animation
                        startActivity(intent);
                        finish();
                        break;
                    }


                    case R.id.create: {
                        Toast.makeText(AdminActivity.this, "User", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), RegAdmin.class);
                        overridePendingTransition(0, 0); // Disable animation
                        startActivity(intent);
                        finish();
                        break;
                    }


                    case R.id.profile: {
                        Toast.makeText(AdminActivity.this, "User", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), prof2.class);
                        overridePendingTransition(0, 0); // Disable animation
                        startActivity(intent);
                        finish();
                        break;
                    }


                    case R.id.User: {
                        Toast.makeText(AdminActivity.this, "User", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), User_search.class);
                        overridePendingTransition(0, 0); // Disable animation
                        startActivity(intent);
                        finish();
                        break;
                    }


                    case R.id.logout: {
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AdminActivity.this);
                        builder.setTitle("Logout");
                        builder.setMessage("Are you sure you want to logout?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(AdminActivity.this, "Logout", Toast.LENGTH_SHORT).show();
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

    private void filterData(String query) {
        List<FoodItem> filteredList = new ArrayList<>();

        for (FoodItem foodItem : foodItemList) {
            if (foodItem.getFoodName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(foodItem);
            }
        }

        // Update the RecyclerView adapter with the filtered data
        foodAdapter.setFilter(filteredList);
    }

    private void retrieveDataFromFirebase() {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                FoodItem foodItem = dataSnapshot.getValue(FoodItem.class);
                if (foodItem != null) {
                    foodItemList.add(foodItem);
                    foodAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // Handle data changes if needed
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                // Handle data removal if needed
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // Handle data movement if needed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database errors if needed
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}