package com.example.recipeapplication.ui.recycler;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeapplication.R;
import com.example.recipeapplication.backend.items.RecipeIngredientItem;

import java.util.List;

public class RecipeIngredientAdapter extends RecyclerView.Adapter<RecipeIngredientViewHolder> {
    private List<RecipeIngredientItem> recipeIngredientList;

    public RecipeIngredientAdapter(List<RecipeIngredientItem> recipeIngredientItems) {
        this.recipeIngredientList = recipeIngredientItems;
    }

    @NonNull
    @Override
    public RecipeIngredientViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recipe_ingredient, viewGroup, false);
        return new RecipeIngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeIngredientViewHolder holder, int position) {
        RecipeIngredientItem item = recipeIngredientList.get(position);
        holder.igdName.setText(item.getName());
        holder.igdQuantity.setText(item.getQuantity());
        holder.igdType.setText(item.getType());
    }

    @Override
    public int getItemCount() {return recipeIngredientList.size();}

    public void setRecipeIngredientList(List<RecipeIngredientItem> recipeIngredientList) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                RecipeIngredientAdapter.this.recipeIngredientList = recipeIngredientList;
                notifyDataSetChanged();
            }
        });
    }

}
