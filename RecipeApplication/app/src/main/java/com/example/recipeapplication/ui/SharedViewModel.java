package com.example.recipeapplication.ui;

import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private String sharedData;

    public void setSharedData(String data) {
        sharedData = data;
    }

    public String getSharedData() {
        return sharedData;
    }

}
