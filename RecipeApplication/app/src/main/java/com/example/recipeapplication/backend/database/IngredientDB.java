package com.example.recipeapplication.backend.database;

import com.example.recipeapplication.backend.items.IngredientItem;
import com.example.recipeapplication.backend.items.IngredientServingItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class IngredientDB extends MyDB {

    static String tableName = "ingredient";


    public static void insertIngredient(String name, String expirationDate, String buyDate, String quantity, String type, String userId) {

        String query = "INSERT INTO " + tableName + " (name, expirationDate, buyDate, quantity, type, userid) VALUES ('" +
                name + "', '" + expirationDate + "', '" + buyDate + "', '" + quantity + "', '" + type + "', " + userId +")";

        postData(query);
    }

    public static void updateIngredient(int id, String name, String expirationDate, String buyDate, String quantity, String type) {
        String query = "UPDATE " + tableName + " SET " +
                "name='" + name + "', " +
                "expirationDate='" + expirationDate + "', " +
                "buyDate='" + buyDate + "', " +
                "quantity='" + quantity + "', " +
                "type='" + type + "' " +
                "WHERE id=" + id;

        postData(query);
    }


    public static void deleteIngredient(int id) {
        String query = "DELETE FROM " + tableName + " WHERE id=" + id;
        postData(query);
    }

    public void getAllIngredients(OnIngredientsLoadedListener listener, String userId) {
        String fileName = "getAllIngredients.php";
        String query = "SELECT * FROM recipeDB.ingredient WHERE userId = " + userId +";";
        getData(new OnDataReceivedListener() {
            @Override
            public void onDataReceived(String data, int result) {
                List<IngredientItem> ingredientList = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(data);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        int id = jsonObject.getInt("id");
                        String name = jsonObject.getString("name");
                        int expirationDate = jsonObject.getInt("expirationDate");
                        String buyDate = jsonObject.getString("buyDate");
                        int quantity = jsonObject.getInt("quantity");
                        String type = jsonObject.getString("type");

                        IngredientItem ingredient = new IngredientItem(id, name, expirationDate, buyDate, quantity, type);
                        ingredientList.add(ingredient);
                    }

                    if (listener != null) {
                        listener.onIngredientsLoaded(ingredientList);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, query, fileName);
    }

    public void getDistinctName(OnIngredientsLoadedListener listener, String userId) {
        String fileName = "getDistinctName.php";
        String query = "SELECT DISTINCT * FROM recipeDB.ingredient WHERE userId = " + userId + ";";
        getData(new OnDataReceivedListener() {
            @Override
            public void onDataReceived(String data, int result) {
                String[] ingredientNameList = {};
                try {
                    JSONArray jsonArray = new JSONArray(data);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String name = jsonObject.getString("name");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, query, fileName);
    }

    public void getQuantity(OnQuantityLoadedListener listener) {
        String fileName = "getQuantity.php";
        String query = "SELECT SUM(quantity) AS total_quantity FROM recipeDB.ingredient;";
        getData(new OnDataReceivedListener() {
            @Override
            public void onDataReceived(String data, int result) {
                int totalQuantity = 0;
                try {
                    JSONArray jsonArray = new JSONArray(data);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    totalQuantity = jsonObject.getInt("total_quantity");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (listener != null) {
                    listener.onQuantityLoaded(totalQuantity);
                }
            }
        }, query, fileName);
    }

    // id를 입력받으면 name 출력
    public void getNameById(OnNameLoadedListener listener, int id) {
        String fileName = "getNameById.php";
        String query = "SELECT name FROM recipeDB.ingredient WHERE id = " + id + ";";
        getData(new OnDataReceivedListener() {
            @Override
            public void onDataReceived(String data, int result) {
                String name = null;
                try {
                    JSONArray jsonArray = new JSONArray(data);
                    if (jsonArray.length() > 0) {
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        name = jsonObject.getString("name");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (listener != null) {
                    listener.onNameLoaded(name);
                }
            }
        }, query, fileName);
    }

    public void getIngredientTypes(OnServingTypesLoadedListener listener) {
        String fileName = "getServingTypes.php";
        String query = "SELECT DISTINCT type FROM recipeDB.ingredient_serving;";
        getData(new OnDataReceivedListener() {
            @Override
            public void onDataReceived(String data, int result) {
                List<String> servingTypes = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(data);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String type = jsonObject.getString("type");
                        servingTypes.add(type);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (listener != null) {
                    listener.onServingTypesLoaded(servingTypes);
                }
            }
        }, query, fileName);
    }

    public void getServings(OnServingsLoadedListener listener) {
        String fileName = "getServings.php";
        String query = "SELECT * FROM recipeDB.ingredient_serving;";
        getData(new OnDataReceivedListener() {
            @Override
            public void onDataReceived(String data, int result) {
                List<IngredientServingItem> servingItems = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(data);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        int id = jsonObject.getInt("id");
                        String type = jsonObject.getString("type");
                        int amount = jsonObject.getInt("amount");

                        IngredientServingItem servingItem = new IngredientServingItem(id, type, amount);
                        servingItems.add(servingItem);
                    }

                    if (listener != null) {
                        listener.onServingsLoaded(servingItems);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, query, fileName);
    }


    public interface OnServingTypesLoadedListener {
        void onServingTypesLoaded(List<String> servingTypes);
    }


    public interface OnIngredientsLoadedListener {
        void onIngredientsLoaded(List<IngredientItem> ingredientList);
    }

    public interface OnQuantityLoadedListener {
        void onQuantityLoaded(int totalQuantity);
    }

    public interface OnNameLoadedListener {
        void onNameLoaded(String name);
    }

    public interface OnServingsLoadedListener {
        void onServingsLoaded(List<IngredientServingItem> servingItems);
    }
}
