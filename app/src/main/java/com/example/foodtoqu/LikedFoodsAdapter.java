package com.example.foodtoqu;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class LikedFoodsAdapter extends RecyclerView.Adapter<LikedFoodsAdapter.FoodViewHolder> {

    private Context context;
    private List<Food3> likedFoodsList;

    public LikedFoodsAdapter(Context context, List<Food3> likedFoodsList) {
        this.context = context;
        this.likedFoodsList = likedFoodsList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item_layout, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food3 food = likedFoodsList.get(position);
        holder.foodNameTextView.setText(food.getFoodName());
        holder.calorieTextView.setText("Calorie: " + food.getCalorie());

        // Load the food image using Picasso
        String imageUrl = food.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get().load(imageUrl).into(holder.foodImageView);
        } else {
            // Set a default drawable image if the image URL is empty
            holder.foodImageView.setImageResource(R.drawable.ic_baseline_person_24);
        }
    }

    @Override
    public int getItemCount() {
        return likedFoodsList.size();
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImageView;
        TextView foodNameTextView;
        TextView calorieTextView;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImageView = itemView.findViewById(R.id.categoryImage); // Change to the correct ImageView ID
            foodNameTextView = itemView.findViewById(R.id.foodNameTextView); // Change to the correct TextView ID
            calorieTextView = itemView.findViewById(R.id.calorieTextView); // Change to the correct TextView ID
        }
    }
}
