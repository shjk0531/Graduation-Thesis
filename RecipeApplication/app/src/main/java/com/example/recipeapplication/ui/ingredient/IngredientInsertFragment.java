package com.example.recipeapplication.ui.ingredient;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipeapplication.backend.api.barcode.BarcodeAPI;
import com.example.recipeapplication.backend.api.barcode.BarcodeData;
import com.example.recipeapplication.databinding.FragmentIngredientInsertBinding;
import com.example.recipeapplication.backend.database.IngredientDB;
import com.example.recipeapplication.ui.SharedViewModel;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class IngredientInsertFragment extends Fragment {

    @SuppressLint("MissingInflatedId")
    private FragmentIngredientInsertBinding binding;
    BarcodeAPI barcodeAPI;
    EditText barcodeEdit;

    private SharedViewModel sharedViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentIngredientInsertBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        barcodeAPI = new BarcodeAPI();
        IngredientDB ingredientDB = new IngredientDB();

        String userId = getDataFromViewModel();

        EditText igdNameEdit = binding.igdNameEdit;
        EditText igdQuantityEdit = binding.igdQuantityEdit;
        EditText igdExpirationDateEdit = binding.igdExpirationDateEdit;
        TextView igdDateEdit = binding.igdDateEdit;
        Spinner igdTypeSpinner = binding.igdTypeSpinner;

        Button calendarBtn = binding.igdCalendarBtn;
        Button barcodeCameraBtn = binding.bcCameraBtn;
        barcodeEdit = binding.bcEditBarcode;
        Button barcodeEditBtn = binding.bcEditBtn;
        Button submitBtn = binding.igdAddBtn;

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        String formattedDate = String.format("%04d-%02d-%02d", year, month, dayOfMonth);
        igdDateEdit.setText(formattedDate);

        ingredientDB.getIngredientTypes(new IngredientDB.OnServingTypesLoadedListener() {
            @Override
            public void onServingTypesLoaded(List<String> servingTypes) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, servingTypes);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        igdTypeSpinner.setAdapter(adapter);
                    }
                });
            }
        });


        calendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                                igdDateEdit.setText(selectedDate);
                            }
                        }, year, month, day);

                // 오늘 이후의 날짜를 선택하지 못하게 함
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                datePickerDialog.show();
            }
        });

        barcodeCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(requireActivity());
                integrator.setOrientationLocked(false);
                integrator.setBeepEnabled(false);

                // ZXing을 실행하여 바코드를 스캔
                getBarcodeAPI.launch(integrator.createScanIntent());
            }
        });

        barcodeEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String scannedData = barcodeEdit.getText().toString();
                if (!scannedData.isEmpty()) {
                    // 네트워크 통신을 백그라운드 스레드에서 수행
                    Executor executor = Executors.newSingleThreadExecutor();
                    executor.execute(() -> {
                        BarcodeAPI barcodeAPI = new BarcodeAPI();
                        BarcodeData responseBody = barcodeAPI.request(scannedData);

                        // UI 업데이트는 메인 스레드에서 수행
                        getActivity().runOnUiThread(() -> {
                            if (responseBody != null) {
                                String productName = responseBody.getProductName();
                                String dayCount = responseBody.getDayCount();

                                // "제조일로부터" 이후의 숫자를 추출
                                int monthsToAdd = Integer.parseInt(dayCount.replaceAll("\\D+", ""));

                                igdNameEdit.setText(productName);
                                igdExpirationDateEdit.setText(Integer.toString(monthsToAdd));
                            } else {
                                // 처리 중 오류가 발생한 경우 사용자에게 알림 등을 추가
                                Toast.makeText(requireContext(), "바코드 정보를 정확히 입력해 주세요.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    });
                } else {
                    // 바코드 정보가 없을 경우 사용자에게 알림 등을 추가
                    Toast.makeText(requireContext(), "바코드 정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });



        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IngredientDB.insertIngredient(igdNameEdit.getText().toString(),
                        igdExpirationDateEdit.getText().toString(),
                        igdDateEdit.getText().toString(),
                        igdQuantityEdit.getText().toString(),
                        igdTypeSpinner.getSelectedItem().toString(),
                        userId);
                Toast.makeText(requireContext(), "식재료를 등록하였습니다.", Toast.LENGTH_SHORT).show();
                igdNameEdit.setText("");
                igdExpirationDateEdit.setText("");
                igdDateEdit.setText(formattedDate);
                igdQuantityEdit.setText("");
            }
        });


        // Inflate the layout for this fragment
        return root;
    }

    // 데이터를 ViewModel에 설정
    public void setDataToViewModel(String data) {
        sharedViewModel.setSharedData(data);
    }
    public String getDataFromViewModel() {
        return sharedViewModel.getSharedData();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private ActivityResultLauncher<Intent> getBarcodeAPI = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(result.getResultCode(), result.getData());
        if (intentResult != null && intentResult.getContents() != null) {
            // 바코드 정보를 읽어옴
            String scannedData = intentResult.getContents();
                // UI 업데이트는 메인 스레드에서 수행
                getActivity().runOnUiThread(() -> {
                    barcodeEdit.setText(scannedData);
                });
        }
    });
}