package com.example.recipeapplication.ui.ingredient;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeapplication.MainActivity;
import com.example.recipeapplication.R;
import com.example.recipeapplication.backend.database.IngredientDB;
import com.example.recipeapplication.backend.items.IngredientItem;
import com.example.recipeapplication.backend.items.IngredientRecommendItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

public class IngredientListAdapter extends RecyclerView.Adapter<IngredientListViewHolder> {
    private List<IngredientRecommendItem> ingredientItems;
    private MainActivity mainActivity;

    public IngredientListAdapter(List<IngredientRecommendItem> items) {
        this.ingredientItems = items;
    }

    @NonNull
    @Override
    public IngredientListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_ingredient_list, viewGroup, false);
        return new IngredientListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientListViewHolder holder, int position) {
        IngredientRecommendItem item = ingredientItems.get(position);

        int id = item.getId();
        String name = item.getName();
        int expirationDate = item.getExpirationDate();
        String buyDate = item.getBuyDate();
        int quantity = item.getQuantity();
        String type = item.getType();

        // 오늘 날짜
        long currentTimeMillis = System.currentTimeMillis();

        // buyDate를 Date 객체로 변환
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date buyDateObject;
        try {
            buyDateObject = dateFormat.parse(buyDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return; // 변환 실패 시 아무 작업도 하지 않음
        }

        // buyDate와 오늘 날짜의 차이 계산
        long timeDifferenceMillis = buyDateObject.getTime() - currentTimeMillis;
        long daysDifference = TimeUnit.MILLISECONDS.toDays(timeDifferenceMillis);

        // expirationDate에서 차이값을 뺀 값을 설정
        long resultExpirationDate = expirationDate + daysDifference;

        holder.igdName.setText(name);
        holder.igdExpirationDate.setText(String.valueOf(resultExpirationDate));
        holder.igdQuantity.setText(String.valueOf(quantity));
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentDate = getCurrentDate();
                IngredientDB.updateIngredient(
                        id,
                        holder.igdName.getText().toString(),
                        holder.igdExpirationDate.getText().toString(),
                        currentDate,
                        holder.igdQuantity.getText().toString(),
                        type
                );
                mainActivity.showToastMessage("수정 완료");
            }
        });
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IngredientDB.deleteIngredient(id);

                ingredientItems.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });
    }


    @Override
    public int getItemCount() {
        return ingredientItems.size();
    }

    public void setIngredientListItems(List<IngredientRecommendItem> ingredientItems) {
        this.ingredientItems = ingredientItems;
        notifyDataSetChanged();
    }
    private String getCurrentDate() {
        // Get the current date
        Date currentTime = Calendar.getInstance().getTime();

        // Format the date to "yyyy-MM-dd"
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(currentTime);
    }

    public void setMainActivity(MainActivity activity) {
        this.mainActivity = activity;
    }
}
