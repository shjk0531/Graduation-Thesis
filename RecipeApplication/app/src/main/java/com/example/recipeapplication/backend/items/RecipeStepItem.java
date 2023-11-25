package com.example.recipeapplication.backend.items;

public class RecipeStepItem {
    private final int recipeId;
    private final int order;
    private final String explanation;
    private final String url;
    private final String tip;

    public RecipeStepItem (
            int recipeId,
            int order,
            String explanation,
            String url,
            String tip
    ) {
        this.recipeId = recipeId;
        this.order = order;
        this.explanation = explanation;
        this.url = url;
        this.tip = tip;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public int getOrder() {
        return order;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getUrl() {
        return url;
    }

    public String getTip() {
        return tip;
    }
}
