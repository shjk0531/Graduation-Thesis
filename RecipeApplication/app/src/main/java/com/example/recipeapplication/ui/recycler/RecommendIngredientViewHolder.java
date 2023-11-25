package com.example.recipeapplication.ui.recycler;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeapplication.R;
import com.example.recipeapplication.backend.items.IngredientRecommendItem;

public class RecommendIngredientViewHolder extends RecyclerView.ViewHolder {
    public CheckBox checkBox;
    public RecommendIngredientViewHolder(@NonNull View view) {
        super(view);
        checkBox = view.findViewById(R.id.rcm_igd_chb);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                IngredientRecommendItem item = (IngredientRecommendItem) checkBox.getTag();
                item.setSelected(isChecked);
            }
        });
    }
}
