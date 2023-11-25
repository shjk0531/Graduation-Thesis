package com.example.recipeapplication.ui.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeapplication.R;
import com.example.recipeapplication.backend.items.IngredientRecommendItem;

import java.util.ArrayList;
import java.util.List;

public class RecommendIngredientAdapter extends RecyclerView.Adapter<RecommendIngredientViewHolder> {
    private List<IngredientRecommendItem> ingredientList;

    public RecommendIngredientAdapter(List<IngredientRecommendItem> ingredientList) {
        this.ingredientList = ingredientList;
    }

    @NonNull
    @Override
    public RecommendIngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend_ingredient, parent, false);
        return new RecommendIngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendIngredientViewHolder holder, int position) {
        IngredientRecommendItem ingredientRecommend = ingredientList.get(position);
        holder.checkBox.setText(ingredientRecommend.getName());
        holder.checkBox.setChecked(ingredientRecommend.isSelected());
        holder.checkBox.setTag(ingredientRecommend); // Tag the checkbox with the corresponding item
    }


    @Override
    public int getItemCount() {return ingredientList.size();}

    public void setIngredientList(List<IngredientRecommendItem> ingredientList) {
        this.ingredientList = ingredientList;
        notifyDataSetChanged();
    }

    public List<IngredientRecommendItem> getSelectedItems() {
        List<IngredientRecommendItem> selectedItems = new ArrayList<>();
        for (IngredientRecommendItem item : ingredientList) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }
}
