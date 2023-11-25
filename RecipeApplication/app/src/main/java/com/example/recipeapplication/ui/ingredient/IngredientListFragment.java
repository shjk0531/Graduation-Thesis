package com.example.recipeapplication.ui.ingredient;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.recipeapplication.MainActivity;
import com.example.recipeapplication.R;
import com.example.recipeapplication.backend.database.IngredientDB;
import com.example.recipeapplication.backend.items.IngredientItem;
import com.example.recipeapplication.backend.items.IngredientRecommendItem;
import com.example.recipeapplication.backend.items.IngredientServingItem;
import com.example.recipeapplication.backend.logic.Recommend;
import com.example.recipeapplication.databinding.FragmentIngredientListBinding;
import com.example.recipeapplication.ui.SharedViewModel;

import java.util.ArrayList;
import java.util.List;

public class IngredientListFragment extends Fragment {
    private FragmentIngredientListBinding binding;
    private RecyclerView ingredientList;
    private IngredientListAdapter adapter;
    private IngredientDB ingredientDB;
    private Recommend recommend;
    private List<IngredientServingItem> servingItems;
    private SharedViewModel sharedViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentIngredientListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ingredientList = binding.igdListRecycler;
        ingredientList.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new IngredientListAdapter(new ArrayList<>());
        adapter.setMainActivity((MainActivity) requireActivity());
        ingredientList.setAdapter(adapter);

        ingredientDB = new IngredientDB();
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        ingredientDB.getServings(new IngredientDB.OnServingsLoadedListener() {
            @Override
            public void onServingsLoaded(List<IngredientServingItem> ingredientServingItems) {
                servingItems = ingredientServingItems;
                loadIngredients();
            }
        });




        return root;
    }

    private void loadIngredients() {
        ingredientDB.getAllIngredients(new IngredientDB.OnIngredientsLoadedListener() {
            @Override
            public void onIngredientsLoaded(List<IngredientItem> ingredientList) {
                recommend = new Recommend(ingredientList, servingItems);
                List<IngredientRecommendItem> recommendItems = recommend.getIngredientWeights();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setIngredientListItems(recommendItems);
                    }
                });
            }
        }, getDataToViewModel());
    }

    public void setDataToViewModel(String data) {
        sharedViewModel.setSharedData(data);
    }
    public String getDataToViewModel() {
        return sharedViewModel.getSharedData();
    }
}