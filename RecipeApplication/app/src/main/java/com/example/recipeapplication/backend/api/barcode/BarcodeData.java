package com.example.recipeapplication.backend.api.barcode;
public class BarcodeData {
    private final String productName;
    private final String dayCount;
    private final String type;

    public BarcodeData( String productName, String dayCount, String type) {
        this.productName = productName;
        this.dayCount = dayCount;
        this.type = type;
    }

    public String getProductName() {
        return productName;
    }

    public String getDayCount() {
        return dayCount;
    }


    public String getType() {
        return type;
    }
}
