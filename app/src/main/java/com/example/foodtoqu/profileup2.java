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
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class profileup2 extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    TextView emailEditText;
    private ProgressDialog progressDialog;
    private EditText age2, gender2,name2,username2;
    private Button updateButton;
    private ImageView profileImageView;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

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
        setContentView(R.layout.activity_profileup2);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Initialize views
        age2 = findViewById(R.id.age);
        gender2 = findViewById(R.id.gender);
        username2 = findViewById(R.id.username);
        name2 = findViewById(R.id.username2);
        updateButton = findViewById(R.id.update_btn);
        profileImageView = findViewById(R.id.categoryImage);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        // Initialize Firebase Database and Storage references
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Admin");
        storageReference = FirebaseStorage.getInstance().getReference().child("image2");


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




        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home: {
                        Toast.makeText(profileup2.this, "Closed", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                        overridePendingTransition(0, 0); // Disable animation
                        startActivity(intent);
                        finish();

                        break;
                    }

                    case R.id.food: {
                        Toast.makeText(profileup2.this, "Food Menu", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                        overridePendingTransition(0, 0); // Disable animation
                        startActivity(intent);
                        finish();
                        break;
                    }


                    case R.id.create: {
                        Toast.makeText(profileup2.this, "User", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), RegAdmin.class);
                        overridePendingTransition(0, 0); // Disable animation
                        startActivity(intent);
                        finish();
                        break;
                    }


                    case R.id.profile: {
                        Toast.makeText(profileup2.this, "User", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), prof2.class);
                        overridePendingTransition(0, 0); // Disable animation
                        startActivity(intent);
                        finish();
                        break;
                    }


                    case R.id.User: {
                        Toast.makeText(profileup2.this, "User", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), User_search.class);
                        overridePendingTransition(0, 0); // Disable animation
                        startActivity(intent);
                        finish();
                        break;
                    }


                    case R.id.logout: {
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(profileup2.this);
                        builder.setTitle("Logout");
                        builder.setMessage("Are you sure you want to logout?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(profileup2.this, "Logout", Toast.LENGTH_SHORT).show();
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
                if (TextUtils.isEmpty(username)) {
                    gender2.setError("Please enter your gender");
                    return;
                } else {
                    gender2.setError(null); // Clear the error
                }




                // Update the profile data in the database
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference userRef = databaseReference.child(userId); // Use the user ID as the child key
                userRef.child("name").setValue(fullname);
                userRef.child("username").setValue(username);
                userRef.child("gender").setValue(gender);

                // Upload the image to storage
                Uri imageUri = getImageUri();
                if (imageUri != null) {
                    progressDialog = new ProgressDialog(profileup2.this);
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
                                                            Toast.makeText(profileup2.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            // Profile update failed
                                                            if (progressDialog != null) {
                                                                progressDialog.dismiss();
                                                            }
                                                            Toast.makeText(profileup2.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(profileup2.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    // No image selected
                    Toast.makeText(profileup2.this, "Please select an image", Toast.LENGTH_SHORT).show();
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
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent i = new Intent(getApplicationContext(), prof2.class);
            startActivity(i);
            finish();
            overridePendingTransition(0, 0);
            super.onBackPressed();
        }
    }
}