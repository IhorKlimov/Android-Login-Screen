package com.myhexaville.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.myhexaville.login.databinding.ActivityMainBinding;
import com.myhexaville.login.login.LoginFragment;
import com.myhexaville.login.login.SignUpFragment;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private boolean isLogin = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.bottom_login, new LoginFragment())
                .replace(R.id.top_login, new LoginFragment())
                .replace(R.id.bottom_sign_up, new SignUpFragment())
                .replace(R.id.top_sign_up, new SignUpFragment())
                .commit();

        binding.topLogin.setRotation(-90);
        binding.topSignUp.setRotation(90);

        binding.button.setOnButtonSwitched(isLogin ->
                binding.getRoot()
                        .setBackgroundColor(ContextCompat.getColor(
                                this,
                                isLogin ? R.color.colorPrimary : R.color.colorAccent)));

        binding.bottomLogin.setAlpha(0f);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        binding.topLogin.setPivotX(binding.topLogin.getWidth() / 2);
        binding.topLogin.setPivotY(binding.topLogin.getHeight());
        binding.topSignUp.setPivotX(binding.topLogin.getWidth() / 2);
        binding.topSignUp.setPivotY(binding.topLogin.getHeight());
    }

    public void rotate(View view) {
        if (isLogin) {
            binding.topLogin.setAlpha(1f);
            binding.topLogin.animate().rotation(0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    binding.bottomLogin.setAlpha(1f);
                    binding.topLogin.setRotation(-90);
                    binding.topLogin.setAlpha(0f);
                }
            });
            binding.bottomSignUp.animate().alpha(0f);
        } else {
            binding.topSignUp.animate().rotation(0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    binding.bottomSignUp.setAlpha(1f);
                    binding.topSignUp.setRotation(90);
                }
            });
            binding.bottomLogin.animate().alpha(0f);
        }

        isLogin = !isLogin;
        binding.button.startAnimation();
    }

    private int getBottomMargin() {
        return getResources().getDimensionPixelOffset(R.dimen.bottom_margin);
    }

}