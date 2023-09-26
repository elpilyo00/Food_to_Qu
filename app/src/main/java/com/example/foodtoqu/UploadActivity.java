package com.example.foodtoqu;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class UploadActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    private ImageView foodImageView;
    private EditText foodNameEditText, calorieEditText, totalFatEditText, cholesterolEditText, sodiumEditText, carboEditText, totalSugarEditText, proteinEditText, descriptionEditText;
    private CheckBox[] healthConditions;
    private CheckBox[] moods;
    private Button uploadButton;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private Uri imageUri;

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
        setContentView(R.layout.activity_upload);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.setCancelable(false);

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("foods");
        storageReference = FirebaseStorage.getInstance().getReference("food_images");

        // Initialize UI components
        foodImageView = findViewById(R.id.uploadImage);
        foodNameEditText = findViewById(R.id.foodName);
        calorieEditText = findViewById(R.id.calorie);
        totalFatEditText = findViewById(R.id.totalFat);
        cholesterolEditText = findViewById(R.id.cholesterol);
        sodiumEditText = findViewById(R.id.sodium);
        carboEditText = findViewById(R.id.carbo);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        totalSugarEditText = findViewById(R.id.totalSugar);
        proteinEditText = findViewById(R.id.protein);
        descriptionEditText = findViewById(R.id.description);

        // Initialize other CheckBox components
        healthConditions = new CheckBox[]{
                findViewById(R.id.diabetes),
                findViewById(R.id.gastrointestinal),
                findViewById(R.id.bowel),
                findViewById(R.id.highBlood),
                findViewById(R.id.weight),
                findViewById(R.id.anemia),
                findViewById(R.id.highCholesterol),
                findViewById(R.id.heartDisease),
                findViewById(R.id.osteoporosis),
                findViewById(R.id.celiac),
                findViewById(R.id.renal),
                findViewById(R.id.hypothyroidism)
        };

        moods = new CheckBox[]{
                findViewById(R.id.happy),
                findViewById(R.id.sad),
                findViewById(R.id.excited),
                findViewById(R.id.angry),
                findViewById(R.id.stress),
                findViewById(R.id.nostalgia),
                findViewById(R.id.inLove),
                findViewById(R.id.calm)
        };

        uploadButton = findViewById(R.id.uploadBtn);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFood();
            }
        });

        // Set a click listener for the foodImageView to pick an image
        foodImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home: {
                        Toast.makeText(UploadActivity.this, "Closed", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                        overridePendingTransition(0, 0); // Disable animation
                        startActivity(intent);
                        finish();

                        break;
                    }


                    case R.id.food: {
                        Toast.makeText(UploadActivity.this, "Food Menu", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                        overridePendingTransition(0, 0); // Disable animation
                        startActivity(intent);
                        finish();
                        break;
                    }


                    case R.id.create: {
                        Toast.makeText(UploadActivity.this,"User", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), RegAdmin.class);
                        overridePendingTransition(0, 0); // Disable animation
                        startActivity(intent);
                        finish();
                        break;
                    }


                    case R.id.profile: {
                        Toast.makeText(UploadActivity.this, "User", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), prof2.class);
                        overridePendingTransition(0, 0); // Disable animation
                        startActivity(intent);
                        finish();
                        break;
                    }


                    case R.id.User: {
                        Toast.makeText(UploadActivity.this, "User", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), User_search.class);
                        overridePendingTransition(0, 0); // Disable animation
                        startActivity(intent);
                        finish();
                        break;
                    }


                    case R.id.logout: {
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(UploadActivity.this);
                        builder.setTitle("Logout");
                        builder.setMessage("Are you sure you want to logout?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(UploadActivity.this, "Logout", Toast.LENGTH_SHORT).show();
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

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            foodImageView.setImageURI(imageUri);
        }
    }

    private void uploadFood() {
        // Get the data from UI components
        String foodName = foodNameEditText.getText().toString().trim();
        String calorie = calorieEditText.getText().toString().trim();
        String totalFat = totalFatEditText.getText().toString().trim();
        String cholesterol = cholesterolEditText.getText().toString().trim();
        String sodium = sodiumEditText.getText().toString().trim();
        String carbo = carboEditText.getText().toString().trim();
        String totalSugar = totalSugarEditText.getText().toString().trim();
        String protein = proteinEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();


        // Create maps to store health conditions and moods
        Map<String, Boolean> healthConditionsMap = new HashMap<>();
        Map<String, Boolean> moodsMap = new HashMap<>();

        // Populate the health conditions map based on checkbox selections
        for (CheckBox checkbox : healthConditions) {
            healthConditionsMap.put(checkbox.getText().toString(), checkbox.isChecked());
        }

        // Populate the moods map based on checkbox selections
        for (CheckBox checkbox : moods) {
            moodsMap.put(checkbox.getText().toString(), checkbox.isChecked());
        }

        // Check if food name is empty
        if (foodName.isEmpty()) {
            Toast.makeText(this, "Please enter a food name", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show the progress dialog
        progressDialog.show();

        // Compress the image before uploading
        if (imageUri != null) {
            Uri compressedImageUri = getImageUri(imageUri); // Use the getImageUri() function you provided

            if (compressedImageUri != null) {
                // Generate a unique foodId for the food item
                String foodId = databaseReference.push().getKey();

                StorageReference imageRef = storageReference.child(foodId + ".jpg");
                imageRef.putFile(compressedImageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Image upload success, get the image download URL
                                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        // Image upload success, get the image download URL
                                        String imageUrl = uri.toString();

                                        // Create a Food object with the data, including health conditions and moods
                                        Food food = new Food(foodId, foodName, calorie, totalFat, cholesterol, sodium, carbo, totalSugar, protein, imageUrl, healthConditionsMap, moodsMap, description);

                                        // Push the food data to the Realtime Database
                                        databaseReference.child(foodId).setValue(food)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        // Hide the progress dialog
                                                        progressDialog.dismiss();

                                                        Toast.makeText(UploadActivity.this, "Food uploaded successfully", Toast.LENGTH_SHORT).show();
                                                        // Clear the input fields
                                                        foodNameEditText.setText("");
                                                        calorieEditText.setText("");
                                                        totalFatEditText.setText("");
                                                        cholesterolEditText.setText("");
                                                        sodiumEditText.setText("");
                                                        carboEditText.setText("");
                                                        totalSugarEditText.setText("");
                                                        proteinEditText.setText("");
                                                        descriptionEditText.setText("");

                                                        // Clear the checkbox selections
                                                        for (CheckBox checkbox : healthConditions) {
                                                            checkbox.setChecked(false);
                                                        }
                                                        for (CheckBox checkbox : moods) {
                                                            checkbox.setChecked(false);
                                                        }

                                                        // Clear the image
                                                        foodImageView.setImageResource(R.drawable.uploading);
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        // Hide the progress dialog
                                                        progressDialog.dismiss();

                                                        Toast.makeText(UploadActivity.this, "Failed to upload food", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Hide the progress dialog
                                progressDialog.dismiss();

                                Toast.makeText(UploadActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                // Error in compressing the image
                progressDialog.dismiss();
                Toast.makeText(this, "Failed to compress the image", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Hide the progress dialog
            progressDialog.dismiss();

            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
        }
    }


    private Uri getImageUri(Uri originalImageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(originalImageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // Define the desired image size in kilobytes
            int maxSizeKB = 500; // Adjust this value as needed

            // Compress the bitmap to the desired size
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int compressQuality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, outputStream);
            while (outputStream.toByteArray().length / 1024 > maxSizeKB && compressQuality > 10) {
                compressQuality -= 10;
                outputStream.reset();
                bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, outputStream);
            }

            // Save the compressed bitmap to a file and get the file URI
            File cachePath = new File(getCacheDir(), "temp_image.jpg");
            FileOutputStream fileOutputStream = new FileOutputStream(cachePath);
            fileOutputStream.write(outputStream.toByteArray());
            fileOutputStream.flush();
            fileOutputStream.close();

            // Get the file URI from the cache path
            return Uri.fromFile(cachePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // Return null if there was an error in compressing the image
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent i = new Intent(getApplicationContext(), AdminActivity.class);
            startActivity(i);
            finish();
            super.onBackPressed();
        }
    }
}