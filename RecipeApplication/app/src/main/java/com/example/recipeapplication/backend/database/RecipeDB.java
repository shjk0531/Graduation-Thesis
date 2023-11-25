package com.example.recipeapplication.backend.database;

import android.util.Log;

import com.example.recipeapplication.backend.items.RecipeIngredientItem;
import com.example.recipeapplication.backend.items.RecipeSimilarity;
import com.example.recipeapplication.backend.items.RecipeStepItem;
import com.example.recipeapplication.backend.items.RecipeTitleItem;
import com.example.recipeapplication.backend.logic.StringUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RecipeDB extends MyDB {
    public void getAllRecipeTitle(OnRecipeTitleListener listener) {
        String fileName = "getAllRecipeTitle.php";
        String query = "SELECT * FROM recipeDB.recipe_title;";
        getData(new OnDataReceivedListener() {
            @Override
            public void onDataReceived(String data, int result) {
                List<RecipeTitleItem> recipeTitleList = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(data);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        int id = jsonObject.getInt("id");
                        String name = jsonObject.getString("name");
                        String summary = jsonObject.getString("summary");
                        String type = jsonObject.getString("type");
                        String category = jsonObject.getString("category");
                        String cookingTime = jsonObject.getString("cookingTime");
                        String calorie = jsonObject.getString("calorie");
                        String quantity = jsonObject.getString("quantity");
                        String difficulty = jsonObject.getString("difficulty");
                        String ingredientCategory = jsonObject.getString("ingredientCategory");
                        String image = jsonObject.getString("image");
                        String url = jsonObject.getString("url");

                        RecipeTitleItem recipeTitle = new RecipeTitleItem(id, name, summary, type, category, cookingTime, calorie, quantity, difficulty, ingredientCategory, image, url);
                        recipeTitleList.add(recipeTitle);
                    }

                    if (listener != null) {
                        listener.onRecipeTitleListener(recipeTitleList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, query, fileName);
    }

    public void getRecipeIdsByIngredientName(String ingredientName, OnRecipeIdsByIngredientNameListener listener) {
        String fileName = "getRecipeIdsByIngredientName.php";
        String query = "SELECT * FROM recipeDB.recipe_ingredient;";
        getData(new OnDataReceivedListener() {
            @Override
            public void onDataReceived(String data, int result) {
                List<RecipeIngredientItem> ingredientItemList = new ArrayList<>();
                Set<Integer> uniqueRecipeIds = new HashSet<>();

                try {
                    JSONArray jsonArray = new JSONArray(data);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        int recipeId = jsonObject.getInt("recipeId");
                        int order = jsonObject.getInt("order");
                        String recipeName = jsonObject.getString("name");
                        String quantity = jsonObject.getString("quantity");
                        int typeId = jsonObject.getInt("typeId");
                        String type = jsonObject.getString("type");

                        int levenshteinDistance = StringUtils.levenshteinDistance(ingredientName, recipeName);
                        double similarity = 1.0 - ((double) levenshteinDistance / Math.max(ingredientName.length(), recipeName.length()));

                        if (similarity == 0) {
                            continue;
                        }

                        int ingredientNameLength = ingredientName.length();
                        double threshold = 1.0 - ((ingredientNameLength - 1) * 0.1);
                        if (threshold < 0.5) {
                            threshold = 0.5;
                        }

                        if (similarity >= threshold) {
                            ingredientItemList.add(new RecipeIngredientItem(recipeId, order, recipeName, quantity, typeId, type));
                            uniqueRecipeIds.add(recipeId);
                        } else if (StringUtils.hasJongseongN(recipeName, ingredientName) && similarity >= threshold * 0.3) {
                            ingredientItemList.add(new RecipeIngredientItem(recipeId, order, recipeName, quantity, typeId, type));
                            uniqueRecipeIds.add(recipeId);
                        }
                    }

                    List<Integer> recipeIds = new ArrayList<>(uniqueRecipeIds);
                    Log.d("RecipeSearch", "검색어: " + ingredientName);
                    // ingredientItemList와 uniqueRecipeIds에 대한 로그도 출력 가능
                    for (RecipeIngredientItem ingredientItem : ingredientItemList) {

                        Log.d("RecipeSearch", "ingredientItemList: " + ingredientItem.getName());
                    }

                    if (listener != null) {
                        listener.onRecipeIdsByIngredientName(recipeIds);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, query, fileName);
    }


    public void getRecipeTitleById(int id, OnRecipeTitleByIdListener listener) {
        String fileName = "getRecipeTitleById.php";
        String query = "SELECT * FROM recipeDB.recipe_title WHERE id = " + id + ";";
        getData(new OnDataReceivedListener() {
            @Override
            public void onDataReceived(String data, int result) {
                RecipeTitleItem recipeTitleItem = null;
                Log.d("result", String.valueOf(result));

                try {
                    Log.d("dbData", data);
                    JSONArray jsonArray = new JSONArray(data);
                    if (jsonArray.length() > 0) {
                        JSONObject jsonObject = jsonArray.getJSONObject(0);

                        String name = jsonObject.getString("name");
                        String summary = jsonObject.getString("summary");
                        String type = jsonObject.getString("type");
                        String category = jsonObject.getString("category");
                        String cookingTime = jsonObject.getString("cookingTime");
                        String calorie = jsonObject.getString("calorie");
                        String quantity = jsonObject.getString("quantity");
                        String difficulty = jsonObject.getString("difficulty");
                        String ingredientCategory = jsonObject.getString("ingredientCategory");
                        String image = jsonObject.getString("image");
                        String url = jsonObject.getString("url");

                        recipeTitleItem = new RecipeTitleItem(id, name, summary, type, category, cookingTime, calorie, quantity, difficulty, ingredientCategory, image, url);
                    }

                    if (listener != null) {
                        listener.onRecipeTitleById(recipeTitleItem);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, query, fileName);
    }

    public void getRecipeStepsByRecipeId(int recipeId, OnRecipeStepsByRecipeIdListener listener) {
        String fileName = "getRecipeStepsByRecipeId.php";
        String query = "SELECT * FROM recipeDB.recipe_step WHERE recipeId = " + recipeId + " ORDER BY `order` ASC;";
        getData(new OnDataReceivedListener() {
            @Override
            public void onDataReceived(String data, int result) {
                List<RecipeStepItem> recipeStepList = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(data);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        int stepOrder = jsonObject.getInt("order");
                        String explanation = jsonObject.getString("explanation");
                        String url = jsonObject.getString("url");
                        String tip = jsonObject.getString("tip");

                        RecipeStepItem recipeStep = new RecipeStepItem(recipeId, stepOrder, explanation, url, tip);
                        recipeStepList.add(recipeStep);
                    }

                    // 오름차순 정렬
                    Collections.sort(recipeStepList, new Comparator<RecipeStepItem>() {
                        @Override
                        public int compare(RecipeStepItem o1, RecipeStepItem o2) {
                            return Integer.compare(o1.getOrder(), o2.getOrder());
                        }
                    });

                    if (listener != null) {
                        listener.onRecipeStepsByRecipeId(recipeStepList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, query, fileName);
    }

    public void getRecipeIngredientsByRecipeId(int recipeId, OnRecipeIngredientsByRecipeIdListener listener) {
        String fileName = "getRecipeIngredientsByRecipeId.php";
        String query = "SELECT * FROM recipeDB.recipe_ingredient WHERE recipeId = " + recipeId + " ORDER BY `order` ASC;";
        getData(new OnDataReceivedListener() {
            @Override
            public void onDataReceived(String data, int result) {
                List<RecipeIngredientItem> recipeIngredientList = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(data);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        int order = jsonObject.getInt("order");
                        String name = jsonObject.getString("name");
                        String quantity = jsonObject.getString("quantity");
                        int typeId = jsonObject.getInt("typeId");
                        String type = jsonObject.getString("type");

                        RecipeIngredientItem recipeIngredient = new RecipeIngredientItem(recipeId, order, name, quantity, typeId, type);
                        recipeIngredientList.add(recipeIngredient);
                    }

                    // 오름차순 정렬
                    Collections.sort(recipeIngredientList, new Comparator<RecipeIngredientItem>() {
                        @Override
                        public int compare(RecipeIngredientItem o1, RecipeIngredientItem o2) {
                            return Integer.compare(o1.getOrder(), o2.getOrder());
                        }
                    });

                    if (listener != null) {
                        listener.onRecipeIngredientsByRecipeId(recipeIngredientList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, query, fileName);
    }

    public void getNames(OnNamesListener listener) {
        String fileName = "getNames.php";
        String query = "SELECT DISTINCT name FROM recipeDB.recipe_title;";
        getData(new OnDataReceivedListener() {
            @Override
            public void onDataReceived(String data, int result) {
                List<String> namesList = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(data);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name = jsonObject.getString("name");
                        namesList.add(name);
                    }

                    if (listener != null) {
                        listener.onNamesListener(namesList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, query, fileName);
    }

    public void searchRecipes(String name, OnRecipeTitleListener listener) {
        String fileName = "searchRecipes.php";
        String query = "SELECT * FROM recipeDB.recipe_title WHERE name LIKE '%" + name + "%';";

        getData(new OnDataReceivedListener() {
            @Override
            public void onDataReceived(String data, int result) {
                List<RecipeTitleItem> recipeTitleList = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(data);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        int id = jsonObject.getInt("id");
                        String recipeName = jsonObject.getString("name");
                        String summary = jsonObject.getString("summary");
                        String type = jsonObject.getString("type");
                        String category = jsonObject.getString("category");
                        String cookingTime = jsonObject.getString("cookingTime");
                        String calorie = jsonObject.getString("calorie");
                        String quantity = jsonObject.getString("quantity");
                        String difficulty = jsonObject.getString("difficulty");
                        String ingredientCategory = jsonObject.getString("ingredientCategory");
                        String image = jsonObject.getString("image");
                        String url = jsonObject.getString("url");

                        RecipeTitleItem recipeTitle = new RecipeTitleItem(id, recipeName, summary, type, category, cookingTime, calorie, quantity, difficulty, ingredientCategory, image, url);

                        if (recipeName.contains(name)) {
                            recipeTitleList.add(recipeTitle);
                        }
                    }

                    if (listener != null) {
                        listener.onRecipeTitleListener(recipeTitleList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, query, fileName);
    }


    public interface OnRecipeStepsByRecipeIdListener {
        void onRecipeStepsByRecipeId(List<RecipeStepItem> recipeStepList);
    }

    public interface OnRecipeIngredientsByRecipeIdListener {
        void onRecipeIngredientsByRecipeId(List<RecipeIngredientItem> recipeIngredientList);
    }

    public interface OnRecipeTitleListener {
        void onRecipeTitleListener(List<RecipeTitleItem> recipeTitle);
    }

    public interface OnRecipeTitleByIdListener {
        void onRecipeTitleById(RecipeTitleItem recipeTitleItem);
    }

    public interface OnRecipeIngredientByIdListener {
        void onRecipeTitleById(RecipeIngredientItem recipeIngredientItem);
    }

    public interface OnRecipeStepByIdListener {
        void onRecipeTitleById(RecipeStepItem recipeStepItem);
    }

    public interface OnRecipeIdsByIngredientNameListener {
        void onRecipeIdsByIngredientName(List<Integer> recipeIds);
    }

    public interface OnNamesListener {
        void onNamesListener(List<String> namesList);
    }

}
