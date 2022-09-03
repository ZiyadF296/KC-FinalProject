package com.example.astrospace;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.Objects;

public class DetailsView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_view);

        // Make the background color navy blue.
        getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.bg));

        // Remove the app bar.
        Objects.requireNonNull(getSupportActionBar()).hide();

        // Make the system bars navy blue.
        getWindow().setStatusBarColor(getResources().getColor(R.color.bg));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.bg));

        // Get the passed in [TelemetryDetails] object.
        final TelemetryDetails telemetryDetails = (TelemetryDetails)
                getIntent().getSerializableExtra("telemetryDetails");
    }
}