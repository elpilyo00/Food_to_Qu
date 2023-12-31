package com.example.foodtoqu;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegAdmin extends AppCompatActivity {
    private TextInputLayout nameEditText, emailEditText, usernameEditText, passwordEditText, ageEditText;
    private Button registerButton;
    private Spinner genderSpinner;
    private FirebaseAuth mAuth;
    private Button login;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_admin);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        nameEditText = findViewById(R.id.name);
        emailEditText = findViewById(R.id.Email);
        login = findViewById(R.id.loginBtn);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        ageEditText = findViewById(R.id.age);
        genderSpinner = findViewById(R.id.genderSpinner); // Update the ID

        registerButton = findViewById(R.id.registerBtn);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Admin");

        // Create an ArrayAdapter for the Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the ArrayAdapter to the Spinner
        genderSpinner.setAdapter(adapter);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Please Test new create account",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                FirebaseAuth.getInstance().signOut();
                startActivity(i);
                finish();
            }
        });



        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }
    private void registerUser() {
        // Retrieve user inputs
        final String name = nameEditText.getEditText().getText().toString().trim();
        final String email = emailEditText.getEditText().getText().toString().trim();
        final String username = usernameEditText.getEditText().getText().toString().trim();
        String password = passwordEditText.getEditText().getText().toString().trim();
        String ageStr = ageEditText.getEditText().getText().toString().trim();
        String selectedGender = genderSpinner.getSelectedItem().toString();

        // Validate the input fields
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(username) ||
                TextUtils.isEmpty(password) || TextUtils.isEmpty(ageStr) || TextUtils.isEmpty(selectedGender)) {
            Toast.makeText(getApplicationContext(), "Please fill in all the fields, including selecting a gender", Toast.LENGTH_SHORT).show();
            return;
        }

        int age = Integer.parseInt(ageStr);

        // Create user in Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            // Set display name for the user
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build();
                            user.updateProfile(profileUpdates);

                            // Send email verification
                            sendEmailVerification(user);

                            // Save user details to Realtime Database
                            saveUserDetailsToDatabase(user.getUid(), name, email, username, age, selectedGender);

                            Toast.makeText(getApplicationContext(), "Registration successful. Verification email sent.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegAdmin.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendEmailVerification(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void saveUserDetailsToDatabase(String uid, String name, String email, String username, int age, String gender) {
        DatabaseReference userRef = mDatabase.child(uid);
        userRef.child("name").setValue(name);
        userRef.child("email").setValue(email);
        userRef.child("username").setValue(username);
        userRef.child("age").setValue(age);
        userRef.child("gender").setValue(gender);
    }

    public void  onBackPressed(){
        Intent i = new Intent(getApplicationContext(),AdminActivity.class);
        startActivity(i);
        finish();
        super.onBackPressed();
    }
}
