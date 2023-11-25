package com.example.recipeapplication.backend.api.barcode;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BarcodeNetworkTask {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final BarcodeAPICallback callback;

    public BarcodeNetworkTask(BarcodeAPICallback callback) {
        this.callback = callback;
    }

    public void requestBarcodeData(String scannedData) {
        executor.execute(() -> {
            try {
                BarcodeAPI barcodeAPI = new BarcodeAPI();
                BarcodeData responseBody = barcodeAPI.request(scannedData);

                if (responseBody != null) {
                    callback.onSuccess(responseBody);
                } else {
                    callback.onFailure("Failed to get barcode data");
                }
            } catch (Exception e) {
                e.printStackTrace();
                callback.onFailure("Error during network request");
            }
        });
    }

    public interface BarcodeAPICallback {
        void onSuccess(BarcodeData responseBody);

        void onFailure(String errorMessage);
    }
}
