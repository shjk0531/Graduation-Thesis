package com.example.recipeapplication.ui.recipe;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.BaseColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.recipeapplication.R;
import com.example.recipeapplication.backend.database.RecipeDB;
import com.example.recipeapplication.backend.items.RecipeTitleItem;
import com.example.recipeapplication.databinding.FragmentRandomRecipeBinding;
import com.example.recipeapplication.ui.recycler.RecipeTitleAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RandomRecipeFragment extends Fragment {
    private FragmentRandomRecipeBinding binding;
    private RecipeTitleAdapter adapter;
    private RecyclerView recyclerView;
    private Button randomBtn;
    private RecipeDB recipeDB;
    private SearchView searchView;
    List<String> suggestionsList;
    private CustomSuggestionsAdapter suggestionsAdapter;
    List<String> namesList;
    private List<String> suggestions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRandomRecipeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recipeDB = new RecipeDB();
        adapter = new RecipeTitleAdapter(new ArrayList<>());
        recyclerView = binding.randomRecipeRecycler;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        randomBtn = binding.randomBtn;
        searchView = binding.recipeSearch;
        suggestionsList = new ArrayList<>();
        suggestionsAdapter = new CustomSuggestionsAdapter(requireContext());
        suggestions = new ArrayList<>();

        recipeDB.getNames(new RecipeDB.OnNamesListener() {
            @Override
            public void onNamesListener(List<String> nameList) {
                namesList = nameList;
            }
        });

        searchView.setSuggestionsAdapter(suggestionsAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 사용자가 검색 버튼을 눌렀을 때 호출됨
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 사용자가 검색창에 텍스트를 입력할 때마다 호출됨
                getSuggestions(newText);
                return true;
            }
        });

        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                // Handle suggestion selection
                return true;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                // Handle suggestion click
                Cursor cursor = suggestionsAdapter.getCursor();
                if (cursor != null && cursor.moveToPosition(position)) {
                    int columnIndex = cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1);
                    if (columnIndex >= 0) {
                        String selectedQuery = cursor.getString(columnIndex);
                        // Use the selected query as needed
                        searchView.setQuery(selectedQuery, true);
                    }
                }
                return true;
            }
        });

        getRecipes();


        randomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRecipes();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void getRecipes() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.clearRecipes();
            }
        });

        recipeDB.getAllRecipeTitle(new RecipeDB.OnRecipeTitleListener() {
            @Override
            public void onRecipeTitleListener(List<RecipeTitleItem> recipeTitle) {
                Collections.shuffle(recipeTitle);

                List<RecipeTitleItem> selectedRecipes = recipeTitle.subList(0, Math.min(10, recipeTitle.size()));

                for (RecipeTitleItem recipeTitleItem : selectedRecipes) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.addRecipe(recipeTitleItem);
                        }
                    });
                }
            }
        });
    }
    private void performSearch(String query) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.clearRecipes();
            }
        });

        recipeDB.searchRecipes(query, new RecipeDB.OnRecipeTitleListener() {
            @Override
            public void onRecipeTitleListener(List<RecipeTitleItem> searchResults) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (RecipeTitleItem recipeTitleItem : searchResults) {
                            adapter.addRecipe(recipeTitleItem);
                        }
                    }
                });
            }
        });
    }

    private void getSuggestions(String query) {
        // Implement your logic to fetch custom suggestions based on the query
        // For this example, use namesList from recipeDB.getNames
        getSuggestions(query, namesList);
    }

    private void getSuggestions(String query, List<String> namesList) {
        // Update the suggestions adapter with the new data
        suggestionsAdapter.updateSuggestions(filterSuggestions(query, namesList));
    }

    private List<String> filterSuggestions(String query, List<String> namesList) {
        List<String> filteredSuggestions = new ArrayList<>();

        // Filter namesList based on the query
        for (String suggestion : namesList) {
            if (suggestion.toLowerCase().contains(query.toLowerCase())) {
                filteredSuggestions.add(suggestion);
            }
        }

        return filteredSuggestions;
    }

    private static class CustomSuggestionsAdapter extends CursorAdapter {
        private List<String> suggestions;

        CustomSuggestionsAdapter(Context context) {
            super(context, null, 0);
            suggestions = Arrays.asList("Recipe 1", "Recipe 2", "Recipe 3");
        }

        void updateSuggestions(List<String> newSuggestions) {
            suggestions = newSuggestions;
            MatrixCursor cursor = new MatrixCursor(new String[]{BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1});
            for (int i = 0; i < suggestions.size(); i++) {
                cursor.addRow(new Object[]{i, suggestions.get(i)});
            }
            changeCursor(cursor);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            return inflater.inflate(R.layout.item_suggestion_recipe, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            int columnIndex = cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1);
            if (columnIndex >= 0) {
                String suggestionText =  cursor.getString(columnIndex);
                TextView suggestionTextView = view.findViewById(R.id.suggestion_text);
                suggestionTextView.setText(suggestionText);
            }
        }
    }
}