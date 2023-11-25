package com.example.recipeapplication.backend.database;

import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public abstract class MyDB {
    protected static final String BASE_URL = "http://52.78.191.63/";

    protected static void postData(String query) {
            new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String fileName = "postData.php";
                    String getUrl = BASE_URL + URLEncoder.encode(fileName, "UTF-8");
                    URL url = new URL(getUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);

                    String postData = "query=" + URLEncoder.encode(query, "UTF-8");

                    OutputStream os = connection.getOutputStream();
                    os.write(postData.getBytes("UTF-8"));
                    os.flush();

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                    } else {
                        InputStream errorStream = connection.getErrorStream();
                        BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
                        StringBuilder errorMsg = new StringBuilder();
                        String line;
                        while ((line = errorReader.readLine()) != null) {
                            errorMsg.append(line);
                        }
                        errorReader.close();
                        errorStream.close();

                        final String finalErrorMsg = errorMsg.toString();
                        System.out.println(finalErrorMsg);
                    }

                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    protected void getData(OnDataReceivedListener listener, String query, String fileName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String getUrl = BASE_URL + URLEncoder.encode(fileName, "UTF-8");
                    URL url = new URL(getUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setDoOutput(true);

                    String postData = "query=" + URLEncoder.encode(query, "UTF-8");

                    OutputStream os = connection.getOutputStream();
                    os.write(postData.getBytes("UTF-8"));
                    os.flush();

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream is = connection.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            response.append(line);
                        }
                        br.close();
                        is.close();

                        String responseData = response.toString();

                        // Check if the response contains an "error" field
                        JSONObject jsonResponse = new JSONObject(responseData);
                        Log.d("serverResponse", String.valueOf(jsonResponse));
                        if (jsonResponse.has("error")) {
                            String errorMsg = jsonResponse.getString("error");
                            System.out.println("Server Error: " + errorMsg);
                        } else {
                            if (listener != null) {
                                // Pass only the "data" field to the listener
                                String dataField = "";
                                int resultField = jsonResponse.optInt("result", 0);
                                if (resultField > 0) {
                                    dataField = jsonResponse.optString("data", "");
                                }
                                listener.onDataReceived(dataField, resultField);
                            }
                        }
                    } else {
                        InputStream errorStream = connection.getErrorStream();
                        BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
                        StringBuilder errorMsg = new StringBuilder();
                        String line;
                        while ((line = errorReader.readLine()) != null) {
                            errorMsg.append(line);
                        }
                        errorReader.close();
                        errorStream.close();

                        final String finalErrorMsg = errorMsg.toString();
                        System.out.println("HTTP Error: " + responseCode + ", " + finalErrorMsg);
                    }

                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    protected interface OnDataReceivedListener {
        void onDataReceived(String data, int result);
    }
}
