package com.example.foodtoqu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailActivity extends AppCompatActivity {

    TextView detailTitle, detailCalorie, detailFat, detailCholesterol, detailSodium, detailCarbo, detailSugar, detailProtein;
    CircleImageView detailImage;
    FloatingActionButton deleteBtn;
    String key = "";
    String imageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailImage = findViewById(R.id.detailImage);
        detailTitle = findViewById(R.id.detailTitle);
        detailCalorie = findViewById(R.id.detailCalorie);
        detailFat = findViewById(R.id.detailFat);
        detailCholesterol = findViewById(R.id.detailCholesterol);
        detailSodium = findViewById(R.id.detailSodium);
        detailCarbo = findViewById(R.id.detailCarbo);
        detailSugar = findViewById(R.id.detailSugar);
        detailProtein = findViewById(R.id.detailProtein);
        deleteBtn = findViewById(R.id.deleteBtn);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            detailTitle.setText(bundle.getString("Title"));
            detailCalorie.setText(bundle.getString("Calorie"));
            detailFat.setText(bundle.getString("Total Fat"));
            detailCholesterol.setText(bundle.getString("Cholesterol"));
            detailSodium.setText(bundle.getString("Sodium"));
            detailCarbo.setText(bundle.getString("Total Carbohydrate"));
            detailSugar.setText(bundle.getString("Total Sugar"));
            detailProtein.setText(bundle.getString("Protein"));
            key = bundle.getString("Key");
            imageUrl = bundle.getString("Food Images");
            Glide.with(this).load(bundle.getString("Food Images")).into(detailImage);
        }

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] foodPaths = {
                        "Diabetes",
                        "Gastrointestinal Disorder",
                        "Irritable Bowel Syndrome",
                        "High Blood Pressure",
                        "Weight Management",
                        "Anemia",
                        "High Cholesterol",
                        "Heart Disease",
                        "Osteoporosis",
                        "Celiac Disease",
                        "Renal Disease",
                        "Hypothyroidism"
                };

                String[] moodPaths = {
                        "Happy",
                        "Sad",
                        "Angry",
                        "Stress",
                        "Excited",
                        "Nostalgia",
                        "In Love",
                        "Calm"
                };

                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);

                // Delete data in Foods node
                for (String foodPath : foodPaths) {
                    DatabaseReference reference = databaseRef.child("Foods").child(foodPath);
                    reference.child(key).removeValue();
                }

                // Delete data in Moods node
                for (String moodPath : moodPaths) {
                    DatabaseReference reference = databaseRef.child("Moods").child(moodPath);
                    reference.child(key).removeValue();
                }

                // Delete image in Firebase Storage
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(DetailActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), AdminActivity.class));
                        finish();
                    }
                });
            }
        });


    }
}