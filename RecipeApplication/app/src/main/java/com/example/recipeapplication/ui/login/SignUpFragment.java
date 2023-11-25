package com.example.recipeapplication.ui.login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.recipeapplication.R;
import com.example.recipeapplication.backend.database.SignInDB;
import com.example.recipeapplication.databinding.FragmentSignUpBinding;

public class SignUpFragment extends Fragment {
    private FragmentSignUpBinding binding;
    private LoginActivity activity;
    private SignInDB signInDB;
    private String id;
    private String password;
    private String passwordCheck;
    private String name;
    private String email;
    private String phone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        activity = (LoginActivity) requireActivity();

        signInDB = new SignInDB();

        Button signUpBtn = binding.signUpBtn;
        Button signInBtn = binding.signUpSignInBtn;

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = binding.signUpId.getText().toString();
                password = binding.signUpPassword.getText().toString();
                passwordCheck = binding.signUpPasswordCheck.getText().toString();
                name = binding.signUpName.getText().toString();
                email = binding.signUpEmail.getText().toString();
                phone = binding.signUpPhone.getText().toString();
                if (id.equals("") ||
                        password.equals("") ||
                        passwordCheck.equals("") ||
                        name.equals("") ||
                        email.equals("") ||
                        phone.equals("")) {
                    Toast.makeText(activity, "전부 작성해 주세요", Toast.LENGTH_SHORT).show();
                } else {
                    if (checkPassword()) {
                        checkId();
                    } else {
                        Toast.makeText(activity, "비밀번호 다름", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.loadSignInFragment();
            }
        });
        return root;
    }

    private void checkId() {
        signInDB.checkUserExistence(id, new SignInDB.OnUserExistenceCheckListener() {
            @Override
            public void onUserExistenceCheck(boolean exists) {
                if (exists) {
                    signUp();
                } else {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "아이디 중복", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private boolean checkPassword() {
        if (password.equals(passwordCheck)) {
            return true;
        } else {
            return false;
        }
    }

    private void signUp() {
        signInDB.signUp(id, password, name, email, phone);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, "아이디 생성 완료", Toast.LENGTH_SHORT).show();
            }
        });
        activity.loadSignInFragment();
    }
}