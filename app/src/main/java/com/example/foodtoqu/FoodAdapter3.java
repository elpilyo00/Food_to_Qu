package com.example.foodtoqu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class FoodAdapter3 extends RecyclerView.Adapter<FoodAdapter3.FoodViewHolder> {

    private Context context;
    private List<Food3> foodList;
    private DatabaseReference databaseReference;

    public FoodAdapter3(Context context, List<Food3> foodList) {
        this.context = context;
        this.foodList = foodList;
        this.databaseReference = FirebaseDatabase.getInstance().getReference().child("foods");
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item_layout, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        final Food3 food = foodList.get(position);
        holder.rate.setText(String.valueOf(food.getRating()));
        holder.foodNameTextView.setText(food.getFoodName());
        holder.calorieTextView.setText("Calorie: " + food.getCalorie());
        // Add more TextViews for other food details as needed

        // Load the food image using Picasso
        String imageUrl = food.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get().load(imageUrl).into(holder.foodImageView);
        } else {
            // Set a default drawable image if the image URL is empty
            holder.foodImageView.setImageResource(R.drawable.ic_baseline_person_24);
        }

        // Set the rating for the MaterialRatingBar
        holder.starRatingBar.setRating(food.getRating());

        // Change the color of the rating bar based on the rating value
        int ratingColor = getRatingColor(food.getRating());
        holder.starRatingBar.setProgressTintList(ColorStateList.valueOf(ratingColor));

        setLikeStateAndCount(holder, food);
        holder.heartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the food is liked or unliked and call the appropriate method
                if (food.isLiked()) {
                    // Call the method to unlike the food item for the current user
                    unLikeFood(food, food.getFoodId());
                } else {
                    // Call the method to like the food item for the current user
                    likeFood(food, food.getFoodId());
                }
            }
        });

        // After incrementing the likes count
        int newLikesCount = food.getLikes(); // Increment likes count by 1
        food.setLikes(newLikesCount);
        holder.likesTextView.setText(String.valueOf(newLikesCount)); // Convert to string using String.valueOf()

        // Add a click listener to update the rating when stars are clicked
        holder.starRatingBar.setOnRatingChangeListener(new MaterialRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
                // Update the rating in Firebase Realtime Database
                updateRatingInFirebase(food.getFoodId(), rating);

                // Change the color of the rating bar based on the new rating value
                int newRatingColor = getRatingColor(rating);
                holder.starRatingBar.setProgressTintList(ColorStateList.valueOf(newRatingColor));
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FoodDetailActivity3.class);

                // Pass food item details as extras
                intent.putExtra("foodName", food.getFoodName());
                intent.putExtra("calorie", food.getCalorie());
                intent.putExtra("totalFat", food.getTotalFat());
                intent.putExtra("cholesterol", food.getCholesterol());
                intent.putExtra("sodium", food.getSodium());
                intent.putExtra("carbo", food.getCarbo());
                intent.putExtra("totalSugar", food.getTotalSugar());
                intent.putExtra("protein", food.getProtein());
                intent.putExtra("imageUrl", food.getImageUrl());
                intent.putExtra("rating", food.getRating());
                intent.putExtra("description", food.getDescription());
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(0, 0); // Disable animation
                ((Activity) context).finish();
            }
        });
    }

    // Method to determine the rating bar color based on the rating value
    private int getRatingColor(float rating) {
        if (rating >= 4.0) {
            // Return green color for high ratings (4.0 and above)
            return ContextCompat.getColor(context, R.color.yellow);
        } else if (rating >= 3.0) {
            // Return yellow color for medium ratings (3.0 to 3.9)
            return ContextCompat.getColor(context, R.color.yellow);
        } else {
            // Return red color for low ratings (below 3.0)
            return ContextCompat.getColor(context, R.color.yellow);
        }
    }

    private void setLikeStateAndCount(final FoodViewHolder holder, final Food3 food) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            DatabaseReference likesReference = FirebaseDatabase.getInstance().getReference().child("likes").child(food.getFoodId()).child(uid);

            likesReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean liked = dataSnapshot.exists();
                    food.setLiked(liked);

                    // Update the UI based on the like state
                    updateLikeUI(holder, food);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle onCancelled event, if needed
                }
            });

            DatabaseReference foodLikesReference = FirebaseDatabase.getInstance().getReference().child("likes").child(food.getFoodId());

            foodLikesReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long likesCount = dataSnapshot.getChildrenCount();
                    food.setLikes((int) likesCount);

                    // Update the UI with the likes count
                    holder.likesTextView.setText(String.valueOf(food.getLikes()));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle onCancelled event, if needed
                }
            });
        }
    }

    private void updateLikeUI(FoodViewHolder holder, Food3 food) {
        // Update the heart button's appearance based on the like state
        if (food.isLiked()) {
            holder.heartButton.setImageResource(R.drawable.ic_baseline_thumb_up_alt_26);
        } else {
            holder.heartButton.setImageResource(R.drawable.ic_baseline_thumb_up_alt_24);
        }
    }

    // ...

    // In your FoodAdapter2 class, modify the likeFood and unLikeFood methods as follows:
    private void likeFood(final Food3 food, final String foodId) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            final String uid = currentUser.getUid();
            final DatabaseReference likesReference = FirebaseDatabase.getInstance().getReference().child("likes").child(foodId);

            // Set the user's like for the food item
            likesReference.child(uid).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(context, LikedFoodsActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION); // Disable animation
                        context.startActivity(intent);
                        ((Activity) context).finish();
                        ((Activity) context).overridePendingTransition(0, 0);
                        // Disable animation
                        Log.d("Firebase", "Like added successfully.");

                        // Now, you can increment the likes count in the "foods" node
                        DatabaseReference foodReference = FirebaseDatabase.getInstance().getReference().child("foods").child(foodId).child("likes");
                        foodReference.runTransaction(new Transaction.Handler() {
                            @NonNull
                            @Override
                            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                                Long currentLikes = mutableData.getValue(Long.class);
                                if (currentLikes == null) {
                                    // Food has no likes, initialize it with 1
                                    mutableData.setValue(1);
                                } else {
                                    // Increment likes count by 1
                                    mutableData.setValue(currentLikes + 1);
                                }
                                return Transaction.success(mutableData);
                            }

                            @Override
                            public void onComplete(@NonNull DatabaseError databaseError, boolean committed, @Nullable DataSnapshot dataSnapshot) {
                                if (committed) {
                                    // Likes count updated successfully
                                    Log.d("Firebase", "Likes count updated successfully.");
                                } else {
                                    // Likes count update failed
                                    Log.e("Firebase", "Error updating likes count: " + databaseError.getMessage());
                                }
                            }
                        });
                    } else {
                        // Like operation failed
                        Log.e("Firebase", "Error adding like: " + task.getException());
                    }
                }
            });
        }
    }

    private void unLikeFood(final Food3 food, final String foodId) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            final String uid = currentUser.getUid();
            final DatabaseReference likesReference = FirebaseDatabase.getInstance().getReference().child("likes").child(foodId);

            // Remove the user's like for the food item
            likesReference.child(uid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(context, LikedFoodsActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION); // Disable animation
                        context.startActivity(intent);
                        ((Activity) context).finish();
                        ((Activity) context).overridePendingTransition(0, 0);
                        // Disable animation
                        Log.d("Firebase", "Unlike successful.");

                        // Now, you can decrement the likes count in the "foods" node
                        DatabaseReference foodReference = FirebaseDatabase.getInstance().getReference().child("foods").child(foodId).child("likes");
                        foodReference.runTransaction(new Transaction.Handler() {
                            @NonNull
                            @Override
                            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                                Long currentLikes = mutableData.getValue(Long.class);
                                if (currentLikes != null && currentLikes > 0) {
                                    // Decrement likes count by 1
                                    mutableData.setValue(currentLikes - 1);
                                }
                                return Transaction.success(mutableData);
                            }

                            @Override
                            public void onComplete(@NonNull DatabaseError databaseError, boolean committed, @Nullable DataSnapshot dataSnapshot) {
                                if (committed) {
                                    // Likes count updated successfully
                                    Log.d("Firebase", "Likes count updated successfully.");
                                } else {
                                    // Likes count update failed
                                    Log.e("Firebase", "Error updating likes count: " + databaseError.getMessage());
                                }
                            }
                        });
                    } else {
                        // Unlike operation failed
                        Log.e("Firebase", "Error unliking food item: " + task.getException());
                    }
                }
            });
        }
    }

    private void updateRatingInFirebase(String foodId, float rating) {
        DatabaseReference foodReference = databaseReference.child(foodId).child("rating"); // Assuming the rating field is named "rating" in your database
        foodReference.setValue(rating).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(context, LikedFoodsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION); // Disable animation
                    context.startActivity(intent);
                    ((Activity) context).finish();
                    ((Activity) context).overridePendingTransition(0, 0);
                    Log.d("Firebase", "Rating updated successfully.");
                } else {
                    // Rating update failed
                    Log.e("Firebase", "Error updating rating: " + task.getException());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView rate;
        TextView likesTextView;
        ImageButton heartButton;
        ImageView foodImageView;
        TextView foodNameTextView;
        TextView calorieTextView;
        MaterialRatingBar starRatingBar;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            rate = itemView.findViewById(R.id.rate);
            likesTextView = itemView.findViewById(R.id.likes);
            heartButton = itemView.findViewById(R.id.heartButton);
            foodImageView = itemView.findViewById(R.id.categoryImage);
            foodNameTextView = itemView.findViewById(R.id.foodNameTextView);
            calorieTextView = itemView.findViewById(R.id.calorieTextView);
            starRatingBar = itemView.findViewById(R.id.starRatingBar);
        }
    }
}
