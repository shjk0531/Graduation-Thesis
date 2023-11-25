package com.example.recipeapplication.ui.ingredient;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeapplication.R;

public class IngredientListViewHolder extends RecyclerView.ViewHolder {
    public TextView igdName;
    public EditText igdExpirationDate;
    public EditText igdQuantity;
    public Button editBtn;
    public Button deleteBtn;

    public IngredientListViewHolder(@NonNull View view) {
        super(view);
        igdName = view.findViewById(R.id.igdListName);
        igdExpirationDate = view.findViewById(R.id.igdListExpirationDate);
        igdQuantity = view.findViewById(R.id.igdListQuantity);
        editBtn = view.findViewById(R.id.igdListEditBtn);
        deleteBtn = view.findViewById(R.id.igdListDeleteBtn);
    }
}
