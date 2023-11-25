package com.example.recipeapplication.ui.recycler;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeapplication.R;

public class RecipeIngredientViewHolder extends RecyclerView.ViewHolder {
    public TextView igdName;
    public TextView igdQuantity;
    public TextView igdType;

    public RecipeIngredientViewHolder(@NonNull View view) {
        super(view);
        igdName = view.findViewById(R.id.rcp_igd_name);
        igdQuantity = view.findViewById(R.id.rcp_igd_quantity);
        igdType = view.findViewById(R.id.rcp_igd_type);
    }
}
