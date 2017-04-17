package com.myhexaville.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

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
                .replace(R.id.bottom, new SignUpFragment())
                .replace(R.id.top, new LoginFragment())
                .commit();

        binding.top.setRotation(-90);

        binding.button.setOnButtonSwitched(isLogin ->
                binding.getRoot()
                        .setBackgroundColor(ContextCompat.getColor(
                                this,
                                isLogin ? R.color.colorPrimary : R.color.colorAccent)));

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        binding.top.setPivotX(binding.top.getWidth() / 2);
        binding.top.setPivotY(binding.top.getHeight());
    }

    // todo fix ugly replacement
    public void rotate(View view) {
        binding.top.animate().rotation(0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (isLogin) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.bottom, new LoginFragment())
                            .replace(R.id.top, new SignUpFragment())
                            .commit();
                    binding.top.setRotation(90);
                } else {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.bottom, new SignUpFragment())
                            .replace(R.id.top, new LoginFragment())
                            .commit();
                    binding.top.setRotation(-90);
                }

                isLogin = !isLogin;
            }
        });

        binding.bottom.animate().alpha(0f).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                binding.bottom.setAlpha(1f);
            }
        });
        binding.button.startAnimation();
    }

    private int getBottomMargin() {
        return getResources().getDimensionPixelOffset(R.dimen.bottom_margin);
    }

}