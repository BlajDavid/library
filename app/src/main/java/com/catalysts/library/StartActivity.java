package com.catalysts.library;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;

public class StartActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    private Button btnLogin;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        linearLayout = findViewById(R.id.linear_layout);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);

        linearLayout.animate().alpha(0f).setDuration(1);
        TranslateAnimation animation = createAnimation();
        linearLayout.setAnimation(animation);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this, LoginActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this, RegisterActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
    }


    private TranslateAnimation createAnimation() {
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, linearLayout.getPivotY());
        animation.setDuration(500);
        animation.setFillAfter(false);
        animation.setAnimationListener(new MyAnimationListener());
        return animation;
    }

    private class MyAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            linearLayout.animate().alpha(1f).setDuration(500);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}