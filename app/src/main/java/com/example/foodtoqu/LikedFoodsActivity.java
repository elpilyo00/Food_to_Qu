package com.example.foodtoqu;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LikedFoodsActivity extends AppCompatActivity {

    FloatingActionButton fab23;
    private RecyclerView recyclerView;
    private List<Food3> foodList;
    private FoodAdapter3 foodAdapter;
    private BarChart barChart;
    private float totalCalorie = 0;
    private float totalFat = 0;
    private float totalCholesterol = 0;
    private float totalSodium = 0;
    private float totalCarbo = 0;
    private float totalSugar = 0;
    private float totalProtein = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_foods);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        recyclerView = findViewById(R.id.recyclerView2);
        foodList = new ArrayList<Food3>();
        fab23 = findViewById(R.id.fabs23);
        foodAdapter = new FoodAdapter3(this, foodList);
        barChart = findViewById(R.id.bar_chart);
        int spanCount = 2;
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(foodAdapter);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();

        populateBarChart();
        fab23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),LikedFoodsActivity.class);
                startActivity(i);
                overridePendingTransition(0,0);
                finish();
            }
        });

// Check the third item (index 1)
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@androidx.annotation.NonNull MenuItem item) {
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
                        Toast.makeText(getApplicationContext(), "Like foods", Toast.LENGTH_LONG).show();
                        Intent homeIntent2 = new Intent(getApplicationContext(), LikedFoodsActivity.class);
                        startActivity(homeIntent2);
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
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(LikedFoodsActivity.this);
                        builder.setTitle("Logout");
                        builder.setMessage("Are you sure you want to logout?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_SHORT).show();
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

        // Call a method to load the liked foods
        loadLikedFoods();
    }

    // Call a method to load the liked foods or any other data for this activity
    private void loadLikedFoods() {
        DatabaseReference likesReference = FirebaseDatabase.getInstance().getReference().child("likes");

        likesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear the existing food list
                foodList.clear();

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    String uid = currentUser.getUid();

                    // Reset the total nutritional values
                    totalCalorie = 0;
                    totalFat = 0;
                    totalCholesterol = 0;
                    totalSodium = 0;
                    totalCarbo = 0;
                    totalSugar = 0;
                    totalProtein = 0;

                    for (DataSnapshot foodSnapshot : dataSnapshot.getChildren()) {
                        if (foodSnapshot.child(uid).exists() && foodSnapshot.child(uid).getValue(Boolean.class)) {
                            // This food is liked by the user, retrieve its data
                            String foodId = foodSnapshot.getKey();
                            DatabaseReference foodReference = FirebaseDatabase.getInstance().getReference().child("foods").child(foodId);

                            foodReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Food3 food = dataSnapshot.getValue(Food3.class);
                                    if (food != null) {
                                        // Add the liked food item to the list
                                        foodList.add(food);

                                        // Update the total nutritional values
                                        totalCalorie += Float.parseFloat(food.getCalorie());
                                        totalFat += Float.parseFloat(food.getTotalFat());
                                        totalCholesterol += Float.parseFloat(food.getCholesterol());
                                        totalSodium += Float.parseFloat(food.getSodium());
                                        totalCarbo += Float.parseFloat(food.getCarbo());
                                        totalSugar += Float.parseFloat(food.getTotalSugar());
                                        totalProtein += Float.parseFloat(food.getProtein());

                                        // Notify the adapter that the data has changed
                                        foodAdapter.notifyDataSetChanged();

                                        // Update the BarChart with the new data
                                        populateBarChart();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    // Handle onCancelled event, if needed
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled event, if needed
            }
        });
    }

    private void populateBarChart() {
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        final String[] customLabels = new String[]{"Calorie", "Fat", "Cholesterol", "Sodium", "Carbohydrates", "Sugar", "Protein"};

        // Loop through the foodList
        for (int i = 0; i < foodList.size(); i++) {
            Food3 food = foodList.get(i);

            // Parse the nutritional values to float (you can change the data type if needed)
            float calorie = Float.parseFloat(food.getCalorie());
            float totalFat = Float.parseFloat(food.getTotalFat());
            float cholesterol = Float.parseFloat(food.getCholesterol());
            float sodium = Float.parseFloat(food.getSodium());
            float carbo = Float.parseFloat(food.getCarbo());
            float totalSugar = Float.parseFloat(food.getTotalSugar());
            float protein = Float.parseFloat(food.getProtein());

            // Create a BarEntry for each nutritional value and add them to separate data sets
            ArrayList<BarEntry> entries = new ArrayList<>();
            entries.add(new BarEntry(0, calorie));
            entries.add(new BarEntry(1, totalFat));
            entries.add(new BarEntry(2, cholesterol));
            entries.add(new BarEntry(3, sodium));
            entries.add(new BarEntry(4, carbo));
            entries.add(new BarEntry(5, totalSugar));
            entries.add(new BarEntry(6, protein));

            // Create a BarDataSet for this food item with a single color
            BarDataSet dataSet = new BarDataSet(entries, "");
            dataSet.setColors(Color.rgb(255, 0, 0), Color.rgb(0, 255, 0), Color.rgb(255, 165, 0),
                    Color.rgb(0, 0, 255), Color.rgb(255, 192, 203), Color.rgb(255, 255, 0),
                    Color.rgb(128, 0, 128)); // Set the color for all bars
            dataSet.setValueTextSize(0f); // Set the text size for values on the bars to 0f to hide them
            dataSet.setValueTextColor(Color.BLACK); // Set the text color for values

            dataSets.add(dataSet);
        }

        // Define the custom colors
        int[] customColors = {
                Color.rgb(255, 0, 0),        // Red
                Color.rgb(0, 255, 0),        // Green
                Color.rgb(255, 165, 0),      // Orange
                Color.rgb(0, 0, 255),        // Blue
                Color.rgb(255, 192, 203),    // Pink
                Color.rgb(255, 255, 0),      // Yellow
                Color.rgb(128, 0, 128)       // Purple
        };
        // Create a BarData object with the collected data sets
        BarData data = new BarData(dataSets);

        // Set custom labels on the x-axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        barChart.setData(data);

        // Customize the legend to match the custom labels
        Legend legend = barChart.getLegend();
        legend.setForm(Legend.LegendForm.SQUARE); // Set the legend form to SQUARE
        legend.setTextSize(8f); // Set the text size for legend labels
        legend.setTextColor(Color.BLACK); // Set the text color for legend labels
        legend.setXEntrySpace(3f); // Set horizontal spacing between legend entries
        legend.setYEntrySpace(5f); // Set vertical spacing between legend entries

        // Create custom legend labels
        LegendEntry[] legendEntries = new LegendEntry[customColors.length];
        for (int i = 0; i < customColors.length; i++) {
            LegendEntry entry = new LegendEntry();
            entry.formColor = customColors[i]; // Set the color for legend entry
            entry.label = customLabels[i];     // Set the label for legend entry
            legendEntries[i] = entry;

//            LegendEntry[] legendEntries = new LegendEntry[customLabels.length];
//            for (int i = 0; i < customLabels.length; i++) {
//                LegendEntry entry = new LegendEntry();
//                entry.formColor = Color.parseColor("#8ECDDD"); // Set the color for legend entry
//                entry.label = customLabels[i]; // Set the label for legend entry
//                legendEntries[i] = entry;
//            }
        }

        // Set the custom legend labels
        legend.setCustom(legendEntries);

        // Refresh the chart
        barChart.invalidate();
    }


    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), UserActivity.class);
        startActivity(i);
        overridePendingTransition(0,0);
        finish();
    }
}