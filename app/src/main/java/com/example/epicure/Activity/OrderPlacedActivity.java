package com.example.epicure.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.os.Bundle;
import android.os.Handler;

import com.airbnb.lottie.LottieAnimationView;
import com.example.epicure.R;

public class OrderPlacedActivity extends AppCompatActivity {
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed);

        LottieAnimationView animationView = findViewById(R.id.animation_placed);

        // Set up an animation listener
        animationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                // Animation started
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // Post delayed action to finish activity after 2 seconds
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1600); // 2000 milliseconds = 2 seconds
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // Animation canceled
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // Animation repeated
            }
        });

        // Start your Lottie animation
        animationView.playAnimation();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove any pending delayed actions to prevent memory leaks
        handler.removeCallbacksAndMessages(null);
    }
}