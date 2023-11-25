package com.example.recipeapplication.backend.logic;

import android.util.Log;

import com.example.recipeapplication.backend.items.IngredientItem;
import com.example.recipeapplication.backend.items.IngredientRecommendItem;
import com.example.recipeapplication.backend.items.IngredientServingItem;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Recommend {
    private List<IngredientRecommendItem> ingredientWeights = new ArrayList<>();
    private final List<IngredientServingItem> servingItems;

    public Recommend(List<IngredientItem> items, List<IngredientServingItem> servingItems) {
        this.servingItems = servingItems;
        for (IngredientItem item : items) {
            double weight = calculateWeight(item);
            IngredientRecommendItem recommendItem = new IngredientRecommendItem(item.getId(), item.getName(), item.getExpirationDate(), item.getBuyDate(), item.getQuantity(), item.getType(), weight);
            this.ingredientWeights.add(recommendItem);
        }
    }

    private double calculateExpirationWeight(int expirationDate) {
        return 1.0 / Math.pow((1 + expirationDate), 2);
    }


    private double calculateWeight(IngredientItem item) {
        int expiration = item.getExpirationDate();
        int quantity = item.getQuantity();
        String buyDate = item.getBuyDate();
        String type = item.getType();
        int amount = setAmount(type);
        int daysDifference = calculateDaysDifference(buyDate);

        double expirationWeight = calculateExpirationWeight(Math.max(0, expiration + daysDifference));
        return expirationWeight * (quantity / amount);
    }


    public List<IngredientRecommendItem> getIngredientWeights() {
        List<IngredientRecommendItem> sortedWeights = this.ingredientWeights.stream()
                .sorted(Comparator.comparingDouble(IngredientRecommendItem::getWeight).reversed())
                .collect(Collectors.toList());
        for (IngredientRecommendItem item : sortedWeights) {
        }

        return sortedWeights;
    }

    private int setAmount(String type) {
        int amount = 1;
        for (IngredientServingItem servingItem : servingItems) {
            if (servingItem.getType().equals(type)) {
                amount = servingItem.getServing();
                break;
            }
        }
        return amount;
    }

    private int calculateDaysDifference(String buyDate) {
        LocalDate today = LocalDate.now();
        LocalDate buyLocalDate = LocalDate.parse(buyDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        int daysDifference =  today.until(buyLocalDate).getDays();

        return daysDifference;
    }

}
