package com.example.astrospace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Remove the app bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        final ImageView bg = findViewById(R.id.bg);

        // Tint the background image to be black with 75% opacity
        bg.setColorFilter(0x99000000);

        // Remove the system UI (status bar and navigation bar)
        Objects.requireNonNull(getWindow()).getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        // Resize the animated arrow
        final LottieAnimationView lottieArrow = findViewById(R.id.arrow);

        final Boolean[] hasNavigated = {false};

        // If the lottie is clicked on we will navigate to the home activity.
        lottieArrow.setOnClickListener(v -> {
            hasNavigated[0] = true;
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
        });

        lottieArrow.getLayoutParams().height = 150;
        lottieArrow.getLayoutParams().width = 150;

        // In two seconds we will navigate to the home activity if the user hasn't already
        lottieArrow.postDelayed(() -> {
            if (!isFinishing() && !isDestroyed() && !hasNavigated[0]) {
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                finish();
            }
        }, 10000); // 10 Seconds
    }
}