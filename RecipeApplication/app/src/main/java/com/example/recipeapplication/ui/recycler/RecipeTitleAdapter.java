package com.example.recipeapplication.ui.recycler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeapplication.R;
import com.example.recipeapplication.backend.database.RecipeDB;
import com.example.recipeapplication.backend.items.RecipeIngredientItem;
import com.example.recipeapplication.backend.items.RecipeStepItem;
import com.example.recipeapplication.backend.items.RecipeTitleItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecipeTitleAdapter extends RecyclerView.Adapter<RecipeTitleViewHolder> {
    private List<RecipeTitleItem> recipeTitleList;
    private RecyclerView igdRecyclerView;
    private RecyclerView stepRecyclerView;
    private RecipeIngredientAdapter igdAdapter;
    private RecipeStepAdapter stepAdapter;
    private Context context;
    private LinearLayout recipeLayout;
    private LinearLayout ingredientLayout;


    public RecipeTitleAdapter(List<RecipeTitleItem> recipeTitleList) {
        this.recipeTitleList = recipeTitleList;
    }

    @NonNull
    @Override
    public RecipeTitleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe_title, parent, false);
        context = parent.getContext();
        return new RecipeTitleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeTitleViewHolder holder, @SuppressLint("RecyclerView") int position) {
        RecipeTitleItem recipeTitle = recipeTitleList.get(position);
        holder.recipeName.setText(recipeTitle.getName());
        holder.recipeSummary.setText(recipeTitle.getSummary());
        holder.recipeType.setText(recipeTitle.getType());
        holder.recipeCategory.setText(recipeTitle.getCategory());
        holder.recipeCookingTime.setText(recipeTitle.getCookingTime());
        holder.recipeCalorie.setText(recipeTitle.getCalorie());
        holder.recipeQuantity.setText(recipeTitle.getQuantity());
        holder.recipeDifficulty.setText(recipeTitle.getDifficulty());
        holder.recipeIngredientCategory.setText(recipeTitle.getIngredientCategory());
//        holder.recipeUrl.setText(recipeTitle.getUrl());
        String imageUrl = recipeTitle.getImage();
        if (imageUrl != null && !imageUrl.isEmpty() && !imageUrl.equals(" ")) {
                Picasso.get().load(imageUrl).into(holder.recipeImage);
        }

        igdRecyclerView = holder.igdRecyclerView;
        stepRecyclerView = holder.stepRecyclerView;
        igdRecyclerView.setLayoutManager(new LinearLayoutManager(igdRecyclerView.getContext()));
        stepRecyclerView.setLayoutManager(new LinearLayoutManager(stepRecyclerView.getContext()));

        igdAdapter = new RecipeIngredientAdapter(new ArrayList<>());
        stepAdapter = new RecipeStepAdapter(new ArrayList<>());

        igdRecyclerView.setAdapter(igdAdapter);
        stepRecyclerView.setAdapter(stepAdapter);

        recipeLayout = holder.recipeLayout;
        ingredientLayout = holder.ingredientLayout;

        int recipeId = recipeTitle.getId();

        RecipeTitleItem recipeTitleItem = recipeTitleList.get(position);
        RecipeDB recipeDB = new RecipeDB();
        recipeDB.getRecipeStepsByRecipeId(recipeId, new RecipeDB.OnRecipeStepsByRecipeIdListener() {
            @Override
            public void onRecipeStepsByRecipeId(List<RecipeStepItem> recipeStepList) {
                stepAdapter.setRecipeStepList(recipeStepList);
            }
        });

        // Get and set recipe ingredients
        recipeDB.getRecipeIngredientsByRecipeId(recipeId, new RecipeDB.OnRecipeIngredientsByRecipeIdListener() {
            @Override
            public void onRecipeIngredientsByRecipeId(List<RecipeIngredientItem> recipeIngredientList) {
                igdAdapter.setRecipeIngredientList(recipeIngredientList);
            }
        });


        holder.recipeOpenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipeTitle.setRecipeLayoutVisible(true);
                notifyItemChanged(position);
            }
        });

        holder.recipeCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipeTitle.setRecipeLayoutVisible(false);
                notifyItemChanged(position);
            }
        });

        holder.ingredientOpenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipeTitle.setIngredientLayoutVisible(true);
                notifyItemChanged(position);
            }
        });

        holder.ingredientCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipeTitle.setIngredientLayoutVisible(false);
                notifyItemChanged(position);
            }
        });

        if (recipeTitle.isRecipeLayoutVisible()) {
            recipeLayout.setVisibility(View.VISIBLE);
            holder.recipeOpenBtn.setVisibility(View.GONE);
            holder.recipeCloseBtn.setVisibility(View.VISIBLE);
        } else {
            recipeLayout.setVisibility(View.GONE);
            holder.recipeCloseBtn.setVisibility(View.GONE);
            holder.recipeOpenBtn.setVisibility(View.VISIBLE);
        }

        if (recipeTitle.isIngredientLayoutVisible()) {
            ingredientLayout.setVisibility(View.VISIBLE);
            holder.ingredientOpenBtn.setVisibility(View.GONE);
            holder.ingredientCloseBtn.setVisibility(View.VISIBLE);
        } else {
            ingredientLayout.setVisibility(View.GONE);
            holder.ingredientCloseBtn.setVisibility(View.GONE);
            holder.ingredientOpenBtn.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return recipeTitleList.size();
    }

    public void setRecipeTitleList(List<RecipeTitleItem> recipeTitleList) {
        this.recipeTitleList = recipeTitleList;
        notifyDataSetChanged();
    }

    public void addRecipe(RecipeTitleItem recipeTitleItem) {
        recipeTitleList.add(recipeTitleItem);
        notifyDataSetChanged();
    }

    public void clearRecipes() {
        recipeTitleList.clear();
        notifyDataSetChanged();
    }
}
