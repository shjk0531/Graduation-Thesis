package com.example.recipeapplication.backend.items;

public class RecipeTitleItem {
    private final int id;
    private final String name;
    private final String summary;
    private final String type;
    private final String category;
    private final String cookingTime;
    private final String calorie;
    private final String quantity;
    private final String difficulty;
    private final String ingredientCategory;
    private final String image;
    private final String url;
    private boolean isRecipeLayoutVisible = false;
    private boolean isIngredientLayoutVisible = false;

    public RecipeTitleItem(
            int id,
            String name,
                           String summary,
                           String type,
                           String category,
                           String cookingTime,
                           String calorie,
                           String quantity,
                           String difficulty,
                           String ingredientCategory,
                           String image,
                           String url) {
        this.id = id;
        this.name = name;
        this.summary = summary;
        this.type = type;
        this.category = category;
        this.cookingTime = cookingTime;
        this.calorie = calorie;
        this.quantity = quantity;
        this.difficulty = difficulty;
        this.ingredientCategory = ingredientCategory;
        this.image = image;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSummary() {
        return summary;
    }

    public String getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public String getCookingTime() {
        return cookingTime;
    }

    public String getCalorie() {
        return calorie;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getIngredientCategory() {
        return ingredientCategory;
    }

    public String getImage() {
        return image;
    }

    public String getUrl() {
        return url;
    }

    public boolean isRecipeLayoutVisible() {
        return isRecipeLayoutVisible;
    }

    public void setRecipeLayoutVisible(boolean visible) {
        isRecipeLayoutVisible = visible;
    }

    public boolean isIngredientLayoutVisible() {
        return isIngredientLayoutVisible;
    }

    public void setIngredientLayoutVisible(boolean visible) {
        isIngredientLayoutVisible = visible;
    }
}
