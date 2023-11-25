package com.example.recipeapplication.ui.recipeTitle;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.recipeapplication.backend.database.RecipeDB;
import com.example.recipeapplication.backend.items.RecipeTitleItem;
import com.example.recipeapplication.databinding.FragmentRecipeTitleBinding;
import com.example.recipeapplication.ui.recycler.RecipeTitleAdapter;

import java.util.ArrayList;
import java.util.List;

public class RecipeTitleFragment extends Fragment {

    private FragmentRecipeTitleBinding binding;
    private RecyclerView recyclerView;
    private RecipeTitleAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRecipeTitleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.recyclerRecipeTitle;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // 레이아웃 매니저 설정

        adapter = new RecipeTitleAdapter(new ArrayList<>()); // 빈 리스트로 초기화

        recyclerView.setAdapter(adapter); // 어댑터 설정
        loadRecipeTitles(); // 데이터 가져오기



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    
    // 서버에서 받아온 데이터를 recyclerView에 배치
    private void loadRecipeTitles() {
        RecipeDB myRecipeDB = new RecipeDB();
        myRecipeDB.getAllRecipeTitle(new RecipeDB.OnRecipeTitleListener() {
            @Override
            public void onRecipeTitleListener(List<RecipeTitleItem> recipeTitleList) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setRecipeTitleList(recipeTitleList);
                    }
                });
            }
        });
    }



}