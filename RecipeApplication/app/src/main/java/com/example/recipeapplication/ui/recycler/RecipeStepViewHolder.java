package com.example.recipeapplication.ui.recycler;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeapplication.R;
import com.example.recipeapplication.backend.items.RecipeStepItem;

public class RecipeStepViewHolder extends RecyclerView.ViewHolder {
    public TextView explanation;
    public TextView order;
    public ImageView image;
    public RecipeStepViewHolder(@NonNull View view) {
        super(view);
        explanation = view.findViewById(R.id.rcp_step_explanation);
        order = view.findViewById(R.id.rcp_step_order);
        image = view.findViewById(R.id.rcp_step_img);
    }
}
