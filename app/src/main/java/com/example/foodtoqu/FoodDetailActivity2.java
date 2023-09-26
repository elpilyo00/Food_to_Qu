package com.example.foodtoqu;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class FoodDetailActivity2 extends AppCompatActivity {

    MaterialRatingBar ratingBar;
    TextView rate23;
    private ImageView detailImage;
    private TextView detailTitle, detailCalorie, detailFat, detailCholesterol,
            detailSodium, detailCarbo, detailSugar, detailProtein, detailDescription;
    private String foodName;
    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail2);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        pieChart = findViewById(R.id.pieChart);
        // Initialize views
        detailImage = findViewById(R.id.detailImage);
        detailTitle = findViewById(R.id.detailTitle);
        detailCalorie = findViewById(R.id.detailCalorie);
        detailFat = findViewById(R.id.detailFat);
        detailCholesterol = findViewById(R.id.detailCholesterol);
        detailSodium = findViewById(R.id.detailSodium);
        detailCarbo = findViewById(R.id.detailCarbo);
        detailSugar = findViewById(R.id.detailSugar);
        rate23 = findViewById(R.id.ratenum);
        detailProtein = findViewById(R.id.detailProtein);
        detailDescription = findViewById(R.id.detailDescription);
        ratingBar = findViewById(R.id.starRatingBar);

        // Configure the rating bar color based on the rating value
        float rating = getIntent().getFloatExtra("rating", 0.0f);
        int ratingColor = getRatingColor(rating);
        ratingBar.setProgressTintList(ColorStateList.valueOf(ratingColor));

        configurePieChart();
        populatePieChart();

        // Retrieve data from intent extras
        Intent intent = getIntent();
        foodName = intent.getStringExtra("foodName");
        String calorie = intent.getStringExtra("calorie");
        String totalFat = intent.getStringExtra("totalFat");
        String cholesterol = intent.getStringExtra("cholesterol");
        String sodium = intent.getStringExtra("sodium");
        String carbo = intent.getStringExtra("carbo");
        String totalSugar = intent.getStringExtra("totalSugar");
        String protein = intent.getStringExtra("protein");
        String imageUrl = intent.getStringExtra("imageUrl");
        rating = intent.getFloatExtra("rating", 0.0f);
        String description = intent.getStringExtra("description");

        // Set data to views
        detailTitle.setText(foodName);
        detailCalorie.setText(calorie);
        detailFat.setText(totalFat);
        detailCholesterol.setText(cholesterol);
        detailSodium.setText(sodium);
        detailCarbo.setText(carbo);
        detailSugar.setText(totalSugar);
        detailProtein.setText(protein);
        ratingBar.setRating(rating);
        rate23.setText(String.valueOf(rating));
        detailDescription.setText(description);

        // Load the image using Picasso
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.ic_baseline_local_pizza_24)
                .error(R.drawable.ic_baseline_local_pizza_24)
                .into(detailImage);

        // Delete button click listener
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),UserActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private int getRatingColor(float rating) {
        if (rating >= 4.0) {
            // Return green color for high ratings (4.0 and above)
            return ContextCompat.getColor(getApplicationContext(), R.color.yellow);
        } else if (rating >= 3.0) {
            // Return yellow color for medium ratings (3.0 to 3.9)
            return ContextCompat.getColor(getApplicationContext(), R.color.yellow);
        } else {
            // Return red color for low ratings (below 3.0)
            return ContextCompat.getColor(getApplicationContext(), R.color.yellow);
        }
    }

    private void populatePieChart() {
        // Retrieve nutritional information from intent extras
        Intent intent = getIntent();
        String calorie = intent.getStringExtra("calorie");
        String totalFat = intent.getStringExtra("totalFat");
        String cholesterol = intent.getStringExtra("cholesterol");
        String sodium = intent.getStringExtra("sodium");
        String carbo = intent.getStringExtra("carbo");
        String totalSugar = intent.getStringExtra("totalSugar");
        String protein = intent.getStringExtra("protein");

        // Convert the values to integers
        int calorieValue = Integer.parseInt(calorie);
        int totalFatValue = Integer.parseInt(totalFat);
        int cholesterolValue = Integer.parseInt(cholesterol);
        int sodiumValue = Integer.parseInt(sodium);
        int carboValue = Integer.parseInt(carbo);
        int totalSugarValue = Integer.parseInt(totalSugar);
        int proteinValue = Integer.parseInt(protein);

        // Create PieEntries for the nutritional information
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(calorieValue, "Calories"));
        entries.add(new PieEntry(totalFatValue, "Total Fat"));
        entries.add(new PieEntry(cholesterolValue, "Cholesterol"));
        entries.add(new PieEntry(sodiumValue, "Sodium"));
        entries.add(new PieEntry(carboValue, "Carbohydrates"));
        entries.add(new PieEntry(totalSugarValue, "Total Sugar"));
        entries.add(new PieEntry(proteinValue, "Protein"));

        PieDataSet dataSet = new PieDataSet(entries, "Nutritional Info");
        dataSet.setColors(Color.rgb(255, 0, 0), Color.rgb(0, 255, 0), Color.rgb(255, 165, 0),
                Color.rgb(0, 0, 255), Color.rgb(255, 192, 203), Color.rgb(255, 255, 0),
                Color.rgb(128, 0, 128));
        dataSet.setValueTextSize(12f);
        dataSet.setDrawValues(true);

        // Set the value formatter to format values as integers
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);
            }
        });

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate();
    }



    private void configurePieChart() {
        pieChart.setUsePercentValues(false); // Set to false to display actual values
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);

        pieChart.setEntryLabelTypeface(Typeface.DEFAULT_BOLD);
        pieChart.setEntryLabelTextSize(12f);
        pieChart.setEntryLabelColor(Color.BLACK);
    }



    // Method to delete the food item from Firebase Realtime Database
    private void deleteFoodItem() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("foods");

        // Create a query to find the item with the specified foodName
        Query query = databaseReference.orderByChild("foodName").equalTo(foodName);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Delete the item with the specified foodName
                    snapshot.getRef().removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Item deleted successfully
                                        Toast.makeText(getApplicationContext(), "Food item deleted", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(getApplicationContext(),AdminActivity.class);
                                        startActivity(i);
                                        finish();
                                    } else {
                                        // Handle the error
                                        Toast.makeText(getApplicationContext(), "Failed to delete food item", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), UserActivity.class);
        startActivity(i);
        finish();
        super.onBackPressed();
    }
}
