package com.example.foodtoqu;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class profileup extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    Spinner healthSpinner;
    Spinner moodsSpinner;
    TextView emailEditText;
    private ProgressDialog progressDialog;
    private EditText age2, gender2,name2,username2;
    private Button updateButton;
    private ImageView profileImageView;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profileup);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Initialize views
        age2 = findViewById(R.id.age);
        gender2 = findViewById(R.id.gender);
        username2 = findViewById(R.id.username);
        name2 = findViewById(R.id.username2);
        updateButton = findViewById(R.id.update_btn);
        profileImageView = findViewById(R.id.categoryImage);
        healthSpinner = findViewById(R.id.health);
        moodsSpinner = findViewById(R.id.moods);

        // Initialize Firebase Database and Storage references
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        storageReference = FirebaseStorage.getInstance().getReference().child("images");

        Intent intent = getIntent();
        if (intent != null) {
            String fullName = intent.getStringExtra("name");
            String username = intent.getStringExtra("username");
            String age = intent.getStringExtra("age");
            String gender = intent.getStringExtra("gender");
            String imageUrl = intent.getStringExtra("imageUrl");

            // Set the retrieved data to the corresponding views
            gender2.setText(gender);
            username2.setText(fullName);
            name2.setText(username);
            age2.setText(age);


            // Load the image into the ImageView using Picasso
            if (imageUrl != null) {
                Picasso.get().load(imageUrl).into(profileImageView);
            } else {
                // Set a default drawable image if the user has no image
                profileImageView.setImageResource(R.drawable.ic_baseline_person_24);
            }
        }

        ArrayAdapter<CharSequence> healthAdapter = ArrayAdapter.createFromResource(this, R.array.health_conditions, android.R.layout.simple_spinner_item);
        healthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        healthSpinner.setAdapter(healthAdapter);

// Create ArrayAdapter for moods
        ArrayAdapter<CharSequence> moodAdapter = ArrayAdapter.createFromResource(this, R.array.moods, android.R.layout.simple_spinner_item);
        moodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        moodsSpinner.setAdapter(moodAdapter);

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
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(profileup.this);
                        builder.setTitle("Logout");
                        builder.setMessage("Are you sure you want to logout?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(profileup.this, "Logout", Toast.LENGTH_SHORT).show();
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











        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start intent to select an image from storage
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the user input
                String fullname = name2.getText().toString().trim();
                String username = username2.getText().toString().trim();
                String gender = gender2.getText().toString().trim();
                String selectedHealth = healthSpinner.getSelectedItem().toString();
                String selectedMood = moodsSpinner.getSelectedItem().toString();

                // Check if all fields are filled
                if (TextUtils.isEmpty(fullname)) {
                    name2.setError("Please enter your fullname");
                    return;
                } else {
                    name2.setError(null); // Clear the error
                }

                if (TextUtils.isEmpty(username)) {
                    username2.setError("Please enter your username");
                    return;
                } else {
                    username2.setError(null); // Clear the error
                }

                if (TextUtils.isEmpty(gender)) {
                    gender2.setError("Please enter your gender");
                    return;
                } else {
                    gender2.setError(null); // Clear the error
                }

                if (selectedHealth.equals("Select conditions")) {
                    // Show an error message for the health spinner
                    Toast.makeText(profileup.this, "Please select a health condition", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (selectedMood.equals("Select moods")) {
                    // Show an error message for the mood spinner
                    Toast.makeText(profileup.this, "Please select a mood", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Update the profile data in the database
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference userRef = databaseReference.child(userId);
                userRef.child("name").setValue(fullname);
                userRef.child("username").setValue(username);
                userRef.child("gender").setValue(gender);
                userRef.child("health").setValue(selectedHealth);
                userRef.child("mood").setValue(selectedMood);

                // Upload the image to storage
                Uri imageUri = getImageUri();
                if (imageUri != null) {
                    progressDialog = new ProgressDialog(profileup.this);
                    progressDialog.setMessage("Updating profile...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    StorageReference imageRef = storageReference.child(userId + "/image.jpg"); // Use the user ID as the child key and a unique image name
                    imageRef.putFile(imageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Image upload successful
                                    // Get the download URL of the uploaded image
                                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String imageUrl = uri.toString();
                                            userRef.child("image").setValue(imageUrl)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            // Profile update successful
                                                            if (progressDialog != null) {
                                                                progressDialog.dismiss();
                                                            }
                                                            Toast.makeText(profileup.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            // Profile update failed
                                                            if (progressDialog != null) {
                                                                progressDialog.dismiss();
                                                            }
                                                            Toast.makeText(profileup.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Image upload failed
                                    if (progressDialog != null) {
                                        progressDialog.dismiss();
                                    }
                                    Toast.makeText(profileup.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    // No image selected
                    Toast.makeText(profileup.this, "Please select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    // ...

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            // Set the selected image to the ImageView
            profileImageView.setImageURI(imageUri);
        }
    }

    // Implement this method to get the image URI from the ImageView
    // Implement this method to get the image URI from the ImageView and compress the image
    private Uri getImageUri() {
        Drawable drawable = profileImageView.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();

            // Define the desired image size in kilobytes
            int maxSizeKB = 200; // Adjust this value as needed

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
            try {
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
        }
        return null; // Return null if the image URI couldn't be retrieved
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(profileup.this, profs.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        super.onBackPressed();
    }
}
