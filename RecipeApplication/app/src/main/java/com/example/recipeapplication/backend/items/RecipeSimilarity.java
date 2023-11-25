package com.example.recipeapplication.backend.items;

public class RecipeSimilarity {
    private final String name;
    private final double similarity;

    public RecipeSimilarity(String name, double similarity) {
        this.name = name;
        this.similarity = similarity;
    }


    public String getName() {
        return name;
    }

    public double getSimilarity() {
        return similarity;
    }
}