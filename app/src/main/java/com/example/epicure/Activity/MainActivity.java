package com.example.epicure.Activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;
import androidx.lifecycle.ViewModelProvider;

import com.example.epicure.R;

public class MainActivity extends FireBaseActivity {

    private MainViewModel viewModel;
    private final int colorChangeInterval = 1700;
    TextView recipe_button;
    private final Handler handler = new Handler(Looper.getMainLooper());

    private final int[] colors = new int[]
            {
                    R.color.button_pressed_color, // Optional for pressed state (defined in color resource)
                    R.color.button_default_color  // Optional for default state (defined in color resource)
            };
    private int colorIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SplashScreen splashScreen = SplashScreen.Companion.installSplashScreen(this);
        splashScreen.setKeepOnScreenCondition(() -> !viewModel.isReady.getValue());
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        setContentView(R.layout.activity_main);
        startColorChangeTask();

        recipe_button = findViewById(R.id.recipe_button);
        recipe_button.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),Before_MainActivity.class)));
    }

    private void startColorChangeTask() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Update button color based on index
                int color = ContextCompat.getColor(MainActivity.this, colors[colorIndex]);
                recipe_button.setBackgroundTintList(ColorStateList.valueOf(color));

                // Update color index for next iteration
                colorIndex = (colorIndex + 1) % colors.length;

                // Schedule the next color change
                handler.postDelayed(this, colorChangeInterval);
            }
        }, colorChangeInterval);
        }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove any pending tasks when activity is destroyed
        handler.removeCallbacksAndMessages(null);
    }
}