package com.example.recipeapplication.ui.recycler;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeapplication.MainActivity;
import com.example.recipeapplication.R;
import com.example.recipeapplication.backend.items.RecipeStepItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepViewHolder> {
    private List<RecipeStepItem> recipeStepList;

    public RecipeStepAdapter(List<RecipeStepItem> recipeStepList) {
        this.recipeStepList = recipeStepList;
    }

    @NonNull
    @Override
    public RecipeStepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe_step, parent, false);
        return new RecipeStepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepViewHolder holder, int position) {
        RecipeStepItem recipeStepItem = recipeStepList.get(position);
        holder.explanation.setText(recipeStepItem.getExplanation());
        holder.order.setText(String.valueOf(recipeStepItem.getOrder()));
        String imageUrl = recipeStepItem.getUrl();

        if (!(imageUrl.equals(" ") || imageUrl == null)) {
            Picasso.get().load(imageUrl).into(holder.image);
        }


    }

    @Override
    public int getItemCount() {
        return recipeStepList.size();
    }

    public void setRecipeStepList(List<RecipeStepItem> recipeStepList) {
        final List<RecipeStepItem> finalRecipeStepList = new ArrayList<>(recipeStepList);  // Ensure a new instance of the list

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                RecipeStepAdapter.this.recipeStepList = finalRecipeStepList;
                notifyDataSetChanged();
            }
        });
    }

}
