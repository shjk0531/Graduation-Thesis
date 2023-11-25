package com.example.recipeapplication.ui.recycler;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeapplication.R;

public class RecipeTitleViewHolder extends RecyclerView.ViewHolder {
    public final TextView recipeName;
    public final TextView recipeSummary;
    public final TextView recipeType;
    public final TextView recipeCategory;
    public final TextView recipeCookingTime;
    public final TextView recipeCalorie;
    public final TextView recipeQuantity;
    public final TextView recipeDifficulty;
    public final TextView recipeIngredientCategory;
    public final ImageView recipeImage;
    public final RecyclerView igdRecyclerView;
    public final RecyclerView stepRecyclerView;
    public final ImageButton ingredientOpenBtn;
    public final ImageButton recipeOpenBtn;
    public final ImageButton ingredientCloseBtn;
    public final ImageButton recipeCloseBtn;
    public final LinearLayout ingredientLayout;
    public final LinearLayout recipeLayout;

    public RecipeTitleViewHolder(@NonNull View view) {
        super(view);
        recipeName = view.findViewById(R.id.rcp_ttl_name);
        recipeSummary = view.findViewById(R.id.rcp_ttl_summary);
        recipeType = view.findViewById(R.id.rcp_ttl_type);
        recipeCategory = view.findViewById(R.id.rcp_ttl_category);
        recipeCookingTime = view.findViewById(R.id.rcp_ttl_cookingTime);
        recipeCalorie = view.findViewById(R.id.rcp_ttl_calorie);
        recipeQuantity = view.findViewById(R.id.rcp_ttl_quantity);
        recipeDifficulty = view.findViewById(R.id.rcp_ttl_difficulty);
        recipeIngredientCategory = view.findViewById(R.id.rcp_ttl_ingredientCategory);
        recipeImage = view.findViewById(R.id.rcp_ttl_image);
        igdRecyclerView = view.findViewById(R.id.rcp_ttl_igd_rcy);
        stepRecyclerView = view.findViewById(R.id.rcp_ttl_step_rcy);
        ingredientOpenBtn = view.findViewById(R.id.rcp_ttl_ingredient_open_btn);
        recipeOpenBtn = view.findViewById(R.id.rcp_ttl_recipe_open_btn);
        ingredientLayout = view.findViewById(R.id.rcp_ttl_ingredient_layout);
        recipeLayout = view.findViewById(R.id.rcp_ttl_recipe_layout);
        ingredientCloseBtn = view.findViewById(R.id.rcp_ttl_ingredient_close_btn);
        recipeCloseBtn = view.findViewById(R.id.rcp_ttl_recipe_close_btn);
    }

}
