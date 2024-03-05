package com.example.epicure.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.lifecycle.ViewModelProvider;

import com.example.epicure.R;

public class MainActivity extends ComponentActivity {

    private MainViewModel viewModel;
    View button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use SplashScreen.Companion.installSplashScreen
        SplashScreen splashScreen = SplashScreen.Companion.installSplashScreen(this);

        splashScreen.setKeepOnScreenCondition(() -> !viewModel.isReady.getValue());

        splashScreen.setOnExitAnimationListener(screen -> {
            ObjectAnimator zoomX = ObjectAnimator.ofFloat(
                    screen.getIconView(),
                    View.SCALE_X,
                    0.6f,
                    0.0f
            );
            zoomX.setInterpolator(new OvershootInterpolator());
            zoomX.setDuration(500L);
            zoomX.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    screen.remove();
                }
            });

            ObjectAnimator zoomY = ObjectAnimator.ofFloat(
                    screen.getIconView(),
                    View.SCALE_Y,
                    0.6f,
                    0.0f
            );
            zoomY.setInterpolator(new OvershootInterpolator());
            zoomY.setDuration(500L);
            zoomY.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    screen.remove();
                }
            });

            zoomX.start();
            zoomY.start();
        });

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.login_button_main);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Epicure is yours to explore!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });
    }
}