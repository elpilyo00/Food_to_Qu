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

public class LikedFoodAdapter extends RecyclerView.Adapter<LikedFoodAdapter.LikedFoodViewHolder> {
    private Context context;
    private List<Food3> likedFoodList;

    public LikedFoodAdapter(Context context, List<Food3> likedFoodList) {
        this.context = context;
        this.likedFoodList = likedFoodList;
    }

    @NonNull
    @Override
    public LikedFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.liked_food_item_layout, parent, false);
        return new LikedFoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LikedFoodViewHolder holder, int position) {
        Food3 likedFood = likedFoodList.get(position);

        // Bind data to the holder's views (e.g., set text and image)
        holder.foodNameTextView.setText(likedFood.getFoodName());

        // Load the food image using Picasso (assuming you have an ImageView with the ID "likedFoodImageView")
        String imageUrl = likedFood.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get().load(imageUrl).into(holder.foodImageView);
        } else {
            // Set a default drawable image if the image URL is empty
            holder.foodImageView.setImageResource(R.drawable.ic_baseline_person_24);
        }

        // Implement other necessary logic for displaying liked foods
    }

    @Override
    public int getItemCount() {
        return likedFoodList.size();
    }

    // Method to update the liked foods data in the adapter
    public void setLikedFoods(List<Food3> likedFoods) {
        this.likedFoodList = likedFoods;
        notifyDataSetChanged();
    }

    public class LikedFoodViewHolder extends RecyclerView.ViewHolder {
        TextView foodNameTextView;
        ImageView foodImageView;

        public LikedFoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodNameTextView = itemView.findViewById(R.id.likedFoodNameTextView); // Replace with your TextView ID
            foodImageView = itemView.findViewById(R.id.likedFoodImageView); // Replace with your ImageView ID
        }
    }
}
