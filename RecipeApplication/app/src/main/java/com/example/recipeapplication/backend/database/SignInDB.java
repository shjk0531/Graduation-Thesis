package com.example.recipeapplication.backend.database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SignInDB extends MyDB {
    private String fileName = "signIn.php";

    public void signUp(String userId, String password, String name, String email, String phone) {
        String query = "INSERT INTO user (user_id, password, name, email, phone) VALUES ('"
                + userId + "', '" + password + "', '" + name + "', '" + email + "', '" + phone + "')";

        postData(query);
    }

    // 로그인 여부
    public void checkSignIn(String userId, String password, OnSignInResultListener listener) {

        String query = "SELECT * FROM user WHERE user_id = '" + userId + "' AND password = '" + password + "'";
        getData(new OnDataReceivedListener() {
            @Override
            public void onDataReceived(String data, int result) {
                boolean success;
                if (result >0) {
                    success = true;
                } else {
                    success = false;
                }
                if (listener != null) {
                    listener.onSignInResult(success);
                }
            }
        }, query, fileName);
    }

    public void getUserId(String userId, String password, OnUserIdListener listener) {
        String query = "SELECT id FROM user WHERE user_id = '" + userId + "' AND password = '" + password + "'";
        getData(new OnDataReceivedListener() {
            @Override
            public void onDataReceived(String data, int result) {
                int userId = -1; // 초기값으로 임의의 값 설정 (id가 없는 경우를 나타냄)

                try {
                    JSONArray jsonArray = new JSONArray(data);
                    if (jsonArray.length() > 0) {
                        // 결과로 받은 첫 번째 행의 id 값을 가져옴
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        userId = jsonObject.getInt("id");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (listener != null) {
                    listener.onUserIdGet(userId);
                }
            }
        }, query, fileName);
    }


    // id 있나 여부
    public void checkUserExistence(String userId, OnUserExistenceCheckListener listener) {
        String query = "SELECT * FROM user WHERE user_id = '" + userId + "'";
        getData(new OnDataReceivedListener() {
            @Override
            public void onDataReceived(String data, int result  ) {
                boolean check;
                if (result > 0) {
                    check = false;
                } else {
                    check = true;
                }
                if (listener != null) {
                    listener.onUserExistenceCheck(check);
                }
            }
        }, query, fileName);
    }


    public interface OnSignInResultListener {
        void onSignInResult(boolean success);
    }

    public interface OnUserExistenceCheckListener {
        void onUserExistenceCheck(boolean exists);
    }

    public interface OnUserIdListener {
        void onUserIdGet(int id);
    }
}
