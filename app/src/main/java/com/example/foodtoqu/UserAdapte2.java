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

public class UserAdapte2 extends RecyclerView.Adapter<UserAdapte2.ViewHolder> {
    private List<User3> userList;
    private Context context;

    public UserAdapte2(List<User3> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User3 user = userList.get(position);

        // Bind user data to ViewHolder views
        holder.usernameTextView.setText(user.getUsername());

        // Load the user image using Picasso
        Picasso.get().load(user.getImage()).into(holder.userImageView);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void updateList(List<User3> newList) {
        userList.clear();
        userList.addAll(newList);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userImageView;
        TextView usernameTextView; // Corrected view ID

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userImageView = itemView.findViewById(R.id.userImageView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView); // Corrected view ID
        }
    }
}