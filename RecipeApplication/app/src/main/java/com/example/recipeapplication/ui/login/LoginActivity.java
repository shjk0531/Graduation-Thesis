package com.example.recipeapplication.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.example.recipeapplication.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        if (savedInstanceState == null) {
            loadSignInFragment();
        }
    }

    // SignInFragment로 화면 전환
    public void loadSignInFragment() {
        SignInFragment signInFragment = new SignInFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.loginFragmentContainer, signInFragment);
        fragmentTransaction.commit();
    }

    // SignUpFragment로 화면 전환
    public void loadSignUpFragment() {
        SignUpFragment signUpFragment = new SignUpFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.loginFragmentContainer, signUpFragment);
        fragmentTransaction.addToBackStack(null); // 뒤로 가기 버튼 처리를 위해 백 스택에 추가
        fragmentTransaction.commit();
    }
}