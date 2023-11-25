package com.example.recipeapplication.backend.api.barcode;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import okhttp3.*;

import java.io.IOException;
import java.io.StringReader;

public class BarcodeAPI {

    public BarcodeData request(String barCode) {
        try {
            String keyId = "479397caf3374e0fbdde";
            String serviceId = "C005";
            String dataType = "json";
            int startIdx = 1;
            int endIdx = 5;

            String url = String.format("http://openapi.foodsafetykorea.go.kr/api/%s/%s/%s/%d/%d/BAR_CD=%s",
                    keyId, serviceId, dataType, startIdx, endIdx, barCode);

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            String jsonString = response.body().string();
            Log.d("logMsg", jsonString);

            return parseJson(jsonString);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private BarcodeData parseJson(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject c005Object = jsonObject.getJSONObject("C005");
            int total_count = Integer.parseInt(c005Object.getString("total_count"));

            if (total_count == 0) {
                return null;
            }

            JSONArray rowArray = c005Object.getJSONArray("row");
            JSONObject rowObject = rowArray.getJSONObject(0);

            String productName = rowObject.getString("PRDLST_NM");
            String dayCount = rowObject.getString("POG_DAYCNT");
            String productDescription = rowObject.getString("PRDLST_DCNM");

            return new BarcodeData(productName, dayCount, productDescription);

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
