package com.example.recipeapplication.backend.items;

public class RecipeIngredientItem {
    private final int recipeId;
    private final int order;
    private final String name;
    private final String quantity;
    private final int typeId;
    private final String type;

    public RecipeIngredientItem (
            int recipeId,
            int order,
            String name,
            String quantity,
            int typeId,
            String type
    ) {
        this.recipeId = recipeId;
        this.order = order;
        this.name = name;
        this.quantity = quantity;
        this.typeId = typeId;
        this.type = type;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public int getOrder() {
        return order;
    }

    public String getName() {
        return name;
    }

    public String getQuantity() {
        return quantity;
    }

    public int getTypeId() {
        return typeId;
    }

    public String getType() {
        return type;
    }
}
