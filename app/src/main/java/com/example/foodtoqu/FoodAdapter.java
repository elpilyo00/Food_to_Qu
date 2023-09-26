package com.example.foodtoqu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {
    private List<FoodItem> foodItemList;
    private Context context;

    public FoodAdapter(Context context, List<FoodItem> foodItemList) {
        this.context = context;
        this.foodItemList = foodItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodItem foodItem = foodItemList.get(position);

        // Bind data to your ViewHolder
        holder.foodNameTextView.setText(foodItem.getFoodName());
        holder.calorieTextView.setText("Calorie: " + foodItem.getCalorie());
        holder.fatTextView.setText("Total Fat: " + foodItem.getTotalFat());
        holder.cholesterolTextView.setText("Cholesterol: " + foodItem.getCholesterol());
        holder.sodiumTextView.setText("Sodium: " + foodItem.getSodium());
        holder.carboTextView.setText("Total Carbohydrate: " + foodItem.getCarbo());
        holder.sugarTextView.setText("Total Sugar: " + foodItem.getTotalSugar());
        holder.proteinTextView.setText("Protein: " + foodItem.getProtein());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FoodDetailActivity.class);

                // Pass food item details as extras
                intent.putExtra("foodName", foodItem.getFoodName());
                intent.putExtra("calorie", foodItem.getCalorie());
                intent.putExtra("totalFat", foodItem.getTotalFat());
                intent.putExtra("cholesterol", foodItem.getCholesterol());
                intent.putExtra("sodium", foodItem.getSodium());
                intent.putExtra("carbo", foodItem.getCarbo());
                intent.putExtra("totalSugar", foodItem.getTotalSugar());
                intent.putExtra("protein", foodItem.getProtein());
                intent.putExtra("imageUrl", foodItem.getImageUrl());
                intent.putExtra("rating", foodItem.getRating());
                intent.putExtra("description", foodItem.getDescription());
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(0, 0); // Disable animation
                ((Activity) context).finish();
            }
        });


        // Load the image using Picasso
        Picasso.get()
                .load(foodItem.getImageUrl())
                .placeholder(R.drawable.ic_baseline_local_pizza_24) // You can set a placeholder image
                .error(R.drawable.ic_baseline_local_pizza_24) // You can set an error image
                .into(holder.foodImageView);
    }

    @Override
    public int getItemCount() {
        return foodItemList.size();
    }

    public void setFilter(List<FoodItem> filteredList) {
        foodItemList = new ArrayList<>(filteredList);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImageView;
        TextView foodNameTextView;
        TextView calorieTextView;
        TextView fatTextView;
        TextView cholesterolTextView;
        TextView sodiumTextView;
        TextView carboTextView;
        TextView sugarTextView;
        TextView proteinTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImageView = itemView.findViewById(R.id.foodImageView);
            foodNameTextView = itemView.findViewById(R.id.foodNameTextView);
            calorieTextView = itemView.findViewById(R.id.calorieTextView);
            fatTextView = itemView.findViewById(R.id.fatTextView);
            cholesterolTextView = itemView.findViewById(R.id.cholesterolTextView);
            sodiumTextView = itemView.findViewById(R.id.sodiumTextView);
            carboTextView = itemView.findViewById(R.id.carboTextView);
            sugarTextView = itemView.findViewById(R.id.sugarTextView);
            proteinTextView = itemView.findViewById(R.id.proteinTextView);
        }
    }
}
