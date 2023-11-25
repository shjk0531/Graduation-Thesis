package com.example.recipeapplication.ui.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.recipeapplication.MainActivity;
import com.example.recipeapplication.R;
import com.example.recipeapplication.backend.database.SignInDB;
import com.example.recipeapplication.databinding.FragmentSignInBinding;

public class SignInFragment extends Fragment {
    private FragmentSignInBinding binding;
    private LoginActivity activity;
    private SignInDB signInDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        activity = (LoginActivity) requireActivity();

        signInDB = new SignInDB();

        EditText idEdit = binding.signInId;
        EditText passwordEdit = binding.signInPassword;
        Button signInBtn = binding.signInBtn;
        Button signUpBtn = binding.signInSignUpBtn;

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 입력된 ID와 비밀번호 가져오기
                String id = idEdit.getText().toString();
                String password = passwordEdit.getText().toString();

                // 서버에 로그인 요청 보내기
                signInDB.checkSignIn(id, password, new SignInDB.OnSignInResultListener() {
                    @Override
                    public void onSignInResult(boolean success) {
                        if (success) {
                            signIn(id, password);
                        } else {
                            // 로그인 실패
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, "로그인 실패", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });

            }
        });


        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SignUpFragment로 이동
                activity.loadSignUpFragment();
            }
        });

        return root;
    }

    private void signIn(String id, String password) {
        signInDB.getUserId(id, password, new SignInDB.OnUserIdListener() {
            @Override
            public void onUserIdGet(int id) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("userId", id);
                startActivity(intent);
                activity.finish();
            }
        });
    }

}