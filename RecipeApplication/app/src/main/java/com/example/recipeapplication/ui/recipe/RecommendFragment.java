package com.example.recipeapplication.ui.recipe;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.recipeapplication.R;
import com.example.recipeapplication.backend.database.IngredientDB;
import com.example.recipeapplication.backend.database.RecipeDB;
import com.example.recipeapplication.backend.items.IngredientItem;
import com.example.recipeapplication.backend.items.IngredientRecommendItem;
import com.example.recipeapplication.backend.items.IngredientServingItem;
import com.example.recipeapplication.backend.items.RecipeTitleItem;
import com.example.recipeapplication.backend.logic.Recommend;
import com.example.recipeapplication.databinding.FragmentRecommendBinding;
import com.example.recipeapplication.ui.SharedViewModel;
import com.example.recipeapplication.ui.recycler.RecipeTitleAdapter;
import com.example.recipeapplication.ui.recycler.RecommendIngredientAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class RecommendFragment extends Fragment {
    private FragmentRecommendBinding binding;
    private SharedViewModel sharedViewModel;
    private RecyclerView IngredientRecyclerView;
    private RecommendIngredientAdapter igdAdapter;
    private RecipeDB recipeDB;
    private RecyclerView recipeRecycler;
    private RecipeTitleAdapter recipeAdapter;
    private Button recommendBtn;
    private List<IngredientServingItem> servingItems;
    private IngredientDB ingredientDB;
    private ImageButton ingredientCloseBtn;
    private ImageButton ingredientOpenBtn;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRecommendBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        recipeDB = new RecipeDB();
        ingredientDB = new IngredientDB();

        IngredientRecyclerView = binding.ingredientRecommendRecycler;
        IngredientRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        igdAdapter = new RecommendIngredientAdapter(new ArrayList<>());
        IngredientRecyclerView.setAdapter(igdAdapter);

        recipeRecycler = binding.rcmRecipeRecycler;
        recipeRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recipeAdapter = new RecipeTitleAdapter(new ArrayList<>());
        recipeRecycler.setAdapter(recipeAdapter);

        ingredientOpenBtn = binding.rcmIngredientOpenBtn;
        ingredientCloseBtn = binding.rcmIngredientCloseBtn;

        ingredientOpenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IngredientRecyclerView.setVisibility(View.VISIBLE);
                ingredientOpenBtn.setVisibility(View.GONE);
                ingredientCloseBtn.setVisibility(View.VISIBLE);
            }
        });

        ingredientCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IngredientRecyclerView.setVisibility(View.GONE);
                ingredientCloseBtn.setVisibility(View.GONE);
                ingredientOpenBtn.setVisibility(View.VISIBLE);
            }
        });

        ingredientDB.getServings(new IngredientDB.OnServingsLoadedListener() {
            @Override
            public void onServingsLoaded(List<IngredientServingItem> ingredientServingItems) {
                servingItems = ingredientServingItems;
                loadIngredients();
            }
        });

        recommendBtn = binding.rcmRcpBtn;

        // recommendBtn 클릭 이벤트 처리 부분
        recommendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipeAdapter.clearRecipes();
                recommendBtn.setEnabled(false);

                // ProgressBar 표시
                ProgressBar progressBar = root.findViewById(R.id.rcm_loading_prg);
                progressBar.setVisibility(View.VISIBLE);

                List<IngredientRecommendItem> selectedItems = igdAdapter.getSelectedItems();

                // 서버에서 레시피 정보 불러오기
                searchRecipeIdsAndCalculateFrequency(selectedItems, root);
            }
        });

        // searchRecipeIdsAndCalculateFrequency 메서드 내에서 서버 응답 후에 아래 코드를 추가
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 버튼 활성화
                recommendBtn.setEnabled(true);

                // ProgressBar 숨김
                ProgressBar progressBar = root.findViewById(R.id.rcm_loading_prg);
                progressBar.setVisibility(View.GONE);

                // 화면 터치 가능하도록 설정
                root.setClickable(true);
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // 데이터를 ViewModel에 설정
    public void setDataToViewModel(String data) {
        sharedViewModel.setSharedData(data);
    }
    public String getDataFromViewModel() {
        return sharedViewModel.getSharedData();
    }

    private void loadIngredients() {
        ingredientDB.getAllIngredients(new IngredientDB.OnIngredientsLoadedListener() {
            @Override
            public void onIngredientsLoaded(List<IngredientItem> ingredientList) {
                Recommend recommend = new Recommend(ingredientList, servingItems);

                List<IngredientRecommendItem> sortedList = recommend.getIngredientWeights();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        igdAdapter.setIngredientList(sortedList);
                    }
                });
            }
        }, getDataFromViewModel());
    }

    public void searchRecipeIdsAndCalculateFrequency(List<IngredientRecommendItem> selectedItems, View root) {
        // 동시성 문제 해결
        final AtomicInteger totalTasks = new AtomicInteger(selectedItems.size());

        // 결과를 저장할 맵
        Map<Integer, Integer> recipeIdFrequency = new HashMap<>();

        for (IngredientRecommendItem item : selectedItems) {
            String ingredientName = item.getName();
            recipeDB.getRecipeIdsByIngredientName(ingredientName, new RecipeDB.OnRecipeIdsByIngredientNameListener() {
                @Override
                public void onRecipeIdsByIngredientName(List<Integer> recipeIds) {
                    for (int recipeId : recipeIds) {
                        if (recipeIdFrequency.containsKey(recipeId)) {
                            recipeIdFrequency.put(recipeId, recipeIdFrequency.get(recipeId) + 1);
                        } else {
                            recipeIdFrequency.put(recipeId, 1);
                        }
                    }

                    // 한 작업이 완료되면 totalTasks를 감소시키고
                    // 모든 작업이 완료되면 getTopRecipeIds를 호출
                    if (totalTasks.decrementAndGet() == 0) {
                        if (!recipeIdFrequency.isEmpty()) {
                            recipeIds = getTopRecipeIds(recipeIdFrequency);
                            for (int id : recipeIds) {
                                Log.d("recipeId", String.valueOf(id));
                                getRecipeTitleById(id);
                            }
                        } else {
                            // 처리할 레시피 ID가 없는 경우
                            Toast.makeText(requireContext(), "레시피 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }

        // searchRecipeIdsAndCalculateFrequency 메서드 내에서 서버 응답 후에 아래 코드를 추가
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 버튼 활성화
                recommendBtn.setEnabled(true);

                // ProgressBar 숨김
                ProgressBar progressBar = root.findViewById(R.id.rcm_loading_prg);
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    public List<Integer> getTopRecipeIds(Map<Integer, Integer> recipeIdFrequency) {
        List<List<Integer>> frequencyGroups = new ArrayList<>();
        List<Integer> topIds = new ArrayList<>();

        // 새로운 맵 생성
        Map<Integer, Integer> modifiedFrequency = new HashMap<>(recipeIdFrequency);

        // 빈도수로 그룹 나누기
        for (int frequency = Collections.max(modifiedFrequency.values()); frequency >= 1; frequency--) {
            List<Integer> group = new ArrayList<>();
            for (Map.Entry<Integer, Integer> entry : modifiedFrequency.entrySet()) {
                if (entry.getValue() == frequency) {
                    group.add(entry.getKey());
                }
            }
            frequencyGroups.add(group);
        }

        // 각 그룹의 멤버들을 랜덤으로 섞기
        Random random = new Random();
        for (List<Integer> group : frequencyGroups) {
            Collections.shuffle(group, random);
        }

        // Logcat에 그룹 내용 출력
        for (int i = 0; i < frequencyGroups.size(); i++) {
            Log.d("FrequencyGroup", "Group " + i + ": " + frequencyGroups.get(i).toString());
        }

        // 빈도수가 가장 큰 그룹부터 추가
        for (List<Integer> group : frequencyGroups) {
            topIds.addAll(group);
            if (topIds.size() >= 10) {
                break;
            }
        }

        // 10개가 넘는 경우 잘라냄
        return topIds.subList(0, Math.min(10, topIds.size()));
    }


    public void getRecipeTitleById(int id) {
        recipeDB.getRecipeTitleById(id, new RecipeDB.OnRecipeTitleByIdListener() {
            @Override
            public void onRecipeTitleById(RecipeTitleItem recipeTitleItem) {
                // 가져온 RecipeTitleItem을 이용하여 화면 업데이트 등을 수행
                // 예시로 RecyclerView에 추가하는 코드를 작성해보겠습니다.
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recipeAdapter.addRecipe(recipeTitleItem);
                    }
                });
            }
        });
    }

}