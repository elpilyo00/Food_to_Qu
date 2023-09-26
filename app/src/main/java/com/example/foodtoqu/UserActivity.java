package com.example.foodtoqu;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {

    ImageView  categoryImageView;
    AppCompatButton filterBtn, recommendBtn;
    RelativeLayout filterLayout;
    RecyclerView recyclerView2;
    DatabaseReference databaseReference;
    ArrayList<DataClass>list;
    UserAdapter adapter;
    int processedReferences = 0;
    CheckBox diabetesCB, gastroCB, bowelCB, highBloodCB, weightCB, anemiaCB, cholesterolCB, heartCB, osteoporosisCB, celiacCB, renalCB, hypothyroidismCB;
    CheckBox happyCB, sadCB, angryCB, stressCB, excitedCB, nostalgiaCB, inLoveCB, calmCB;
    ValueEventListener eventListener;
    TextView fullName,conditions,moods23;
    boolean filterHidden = true;
    private List<Food3> foodList;
    private FoodAdapter2 foodAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user);

        initWidgets();
        hideFilter();

        //CheckBoxes
        //Health Conditions
        diabetesCB = findViewById(R.id.diabetes);
        categoryImageView = findViewById(R.id.categoryImage);
        gastroCB = findViewById(R.id.gastrointestinal);
        bowelCB = findViewById(R.id.bowel);
        highBloodCB = findViewById(R.id.highBlood);
        weightCB = findViewById(R.id.weight);
        anemiaCB = findViewById(R.id.anemia);
        conditions = findViewById(R.id.condition);
        moods23 = findViewById(R.id.moods);
        cholesterolCB = findViewById(R.id.highCholesterol);
        heartCB = findViewById(R.id.heartDisease);
        osteoporosisCB = findViewById(R.id.osteoporosis);
        celiacCB = findViewById(R.id.celiac);
        renalCB = findViewById(R.id.renal);
        fullName = findViewById(R.id.name);
        hypothyroidismCB = findViewById(R.id.hypothyroidism);
        //Moods
        happyCB = findViewById(R.id.happy);
        sadCB = findViewById(R.id.sad);
        angryCB = findViewById(R.id.angry);
        stressCB = findViewById(R.id.stress);
        excitedCB = findViewById(R.id.excited);
        nostalgiaCB = findViewById(R.id.nostalgia);
        inLoveCB = findViewById(R.id.inLove);
        calmCB = findViewById(R.id.calm);
        recommendBtn = findViewById(R.id.recommendBtn);

        recyclerView2 = findViewById(R.id.recyclerView2);
        foodList = new ArrayList<Food3>();
        foodAdapter = new FoodAdapter2(this, foodList);
        int spanCount = 2; // You can adjust the number of columns in the grid as needed
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        recyclerView2.setLayoutManager(layoutManager);
        recyclerView2.setAdapter(foodAdapter);

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("foods");

         recommendBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 recommendButtonTapped();
             }
         });
        // Load all foods initially
        loadFoods();
        retrieveUserDetails();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();

// Check the third item (index 2)
        MenuItem menuItem = menu.getItem(0);
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
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(UserActivity.this);
                        builder.setTitle("Logout");
                        builder.setMessage("Are you sure you want to logout?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(UserActivity.this, "Logout", Toast.LENGTH_SHORT).show();
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
                    String imageUrl = dataSnapshot.child("image").getValue(String.class);
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String condition = dataSnapshot.child("health").getValue(String.class);
                    String mood = dataSnapshot.child("mood").getValue(String.class);

                    // Set the image URL to the ImageView's tag for later retrieval
                    categoryImageView.setTag(imageUrl);
                    fullName.setText(name);
                    conditions.setText(condition);
                    moods23.setText(mood);

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




    private void recommendButtonTapped() {
        // Create arrays for mood and health condition checkboxes
        CheckBox[] moodCheckboxes = {happyCB, sadCB, angryCB, stressCB, excitedCB, nostalgiaCB, inLoveCB, calmCB};
        CheckBox[] healthConditionCheckboxes = {diabetesCB, gastroCB, bowelCB, highBloodCB, weightCB, anemiaCB, cholesterolCB, heartCB, osteoporosisCB, celiacCB, renalCB, hypothyroidismCB};

        // Construct a query to order the data by the "foodName" child key
        Query query = databaseReference.orderByChild("foodName");

        // Create a list to store the filtered foods
        List<Food3> filteredFoods = new ArrayList<>();

        boolean anyCheckboxSelected = false; // Flag to check if any checkbox is selected

        for (CheckBox checkbox : moodCheckboxes) {
            if (checkbox.isChecked()) {
                anyCheckboxSelected = true;
                break;
            }
        }

        if (!anyCheckboxSelected) {
            for (CheckBox checkbox : healthConditionCheckboxes) {
                if (checkbox.isChecked()) {
                    anyCheckboxSelected = true;
                    break;
                }
            }
        }

        if (!anyCheckboxSelected) {
            // No checkboxes are selected, load all foods
            loadFoods();
            return; // Exit the method to prevent further processing
        }

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                filteredFoods.clear(); // Clear the previous filtered list

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Food3 food = snapshot.getValue(Food3.class);

                    // Check if any mood is selected
                    boolean moodSelected = false;
                    for (CheckBox moodCheckbox : moodCheckboxes) {
                        if (moodCheckbox.isChecked() && food.getMoods().get(moodCheckbox.getText().toString())) {
                            moodSelected = true;
                            break; // No need to check other moods if one is selected
                        }
                    }

                    // Check if any health condition is selected
                    boolean healthConditionSelected = false;
                    for (CheckBox healthConditionCheckbox : healthConditionCheckboxes) {
                        if (healthConditionCheckbox.isChecked() && food.getHealthConditions().get(healthConditionCheckbox.getText().toString())) {
                            healthConditionSelected = true;
                            break; // No need to check other health conditions if one is selected
                        }
                    }

                    // If any mood or health condition is selected, add the food to the filtered list
                    if (moodSelected || healthConditionSelected) {
                        filteredFoods.add(food);
                    }
                }

                // Update the RecyclerView with the filtered data
                foodList.clear();
                foodList.addAll(filteredFoods);
                foodAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UserActivity.this, "Failed to load filtered foods.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loadFoods() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                foodList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Food3 food = snapshot.getValue(Food3.class);
                    foodList.add(food);
                }
                foodAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UserActivity.this, "Failed to load foods.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initWidgets() {

        filterBtn = findViewById(R.id.filterBtn);
        filterLayout = findViewById(R.id.filterTab);
    }

    public void showFilterTapped(View view) {
        if (filterHidden == true){
            filterHidden = false;
            showFilter();
        }
        else {
            filterHidden = true;
            hideFilter();
        }

    }
    private void hideFilter() {
        filterLayout.setVisibility(View.GONE);
    }
    private void showFilter() {
        filterLayout.setVisibility(View.VISIBLE);
    }
}