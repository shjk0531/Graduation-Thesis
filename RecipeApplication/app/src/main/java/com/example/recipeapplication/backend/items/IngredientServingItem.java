package com.example.recipeapplication.backend.items;

public class IngredientServingItem {
    private final int id;
    private final String type;
    private final int amount;
    public IngredientServingItem (
            int id,
            String type,
            int serving
    ) {
        this.id = id;
        this.type = type;
        this.amount = serving;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public int getServing() {
        return amount;
    }
}
