package com.example.recipeapplication.backend.items;

public class IngredientRecommendItem {
    private final int id;
    private final String name;
    private final int expirationDate;
    private final String buyDate;
    private final int quantity;
    private final String type;
    private final double weight;
    private boolean isSelected;

    public IngredientRecommendItem(int id, String name, int expirationDate, String buyDate, int quantity, String type , double weight) {
        this.id = id;
        this.name = name;
        this.expirationDate = expirationDate;
        this.buyDate = buyDate;
        this.quantity = quantity;
        this.type = type;
        this.weight = weight;
        this.isSelected = false;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public int getExpirationDate() {
        return expirationDate;
    }

    public String getBuyDate() {
        return buyDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getType() {
        return type;
    }

    public double getWeight() {
        return weight;
    }

    public boolean isSelected() {
        return isSelected;
    }


    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }
}
