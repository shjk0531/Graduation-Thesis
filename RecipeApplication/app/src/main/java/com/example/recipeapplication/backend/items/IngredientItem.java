package com.example.recipeapplication.backend.items;

public class IngredientItem {
    private final int id;                 // id
    private final String name;            // 이름
    private final int expirationDate;  // 유통기한
    private final String buyDate;         // 구매날짜
    private final int quantity;        // 수량
    private final String type;            // 종류

    public IngredientItem(int id, String name, int expirationDate, String buyDate, int quantity, String type) {
        this.id = id;
        this.name = name;
        this.expirationDate = expirationDate;
        this.buyDate = buyDate;
        this.quantity = quantity;
        this.type = type;
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
}
