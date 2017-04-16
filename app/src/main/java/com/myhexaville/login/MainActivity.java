package com.myhexaville.login;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.myhexaville.login.databinding.ActivityMainBinding;
import com.myhexaville.login.login.LoginFragment;
import com.myhexaville.login.login.SignUpFragment;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.sign_up, new SignUpFragment())
                .replace(R.id.login, new LoginFragment())
                .commit();

        binding.login.setRotation(-90);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        binding.login.setPivotX(binding.login.getWidth() / 2);
        binding.login.setPivotY(binding.login.getHeight());
    }

    public void rotate(View view) {
        binding.login.animate().rotation(0).setInterpolator(new AccelerateInterpolator());
        binding.signUp.animate().alpha(0f).setInterpolator(new AccelerateInterpolator());
        binding.button.startAnimation();
    }

    private int getBottomMargin() {
        return getResources().getDimensionPixelOffset(R.dimen.bottom_margin);
    }
}