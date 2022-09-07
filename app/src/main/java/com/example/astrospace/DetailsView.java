package com.example.astrospace;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

public class DetailsView extends AppCompatActivity {

    @SuppressLint({"ClickableViewAccessibility", "DefaultLocale"})
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
                getIntent().getSerializableExtra("telemetry");

        // Set the back and favorite/unFavorite buttons.
        final ImageView back = findViewById(R.id.back_button);
        final ImageView favorite = findViewById(R.id.favorite_button);

        // Set the back button to go back to the previous activity.
        back.setOnClickListener(v -> finish());

        // Set the favorite button to add/remove the current telemetry to/from the favorites.
        favorite.setOnClickListener(v -> {
            if (FavoritesManager.isFavorite(telemetryDetails)) {
                FavoritesManager.removeFavorite(this, telemetryDetails);
                favorite.setImageResource(R.drawable.un_favorite);
            } else {
                FavoritesManager.addFavorite(this, telemetryDetails);
                favorite.setImageResource(R.drawable.favorite);
            }
        });

        // Set the status of the favorite according to the passed in telemetry.
        if (FavoritesManager.isFavorite(telemetryDetails)) {
            favorite.setImageResource(R.drawable.favorite);
        } else {
            favorite.setImageResource(R.drawable.un_favorite);
        }

        // Set the telemetry image.
        final ImageView telemetryImage = findViewById(R.id.telemetry_image);
        telemetryImage.setImageResource(telemetryDetails.imageSource);

        // Set the image height and width to 200dp.
        telemetryImage.getLayoutParams().height = 500;
        telemetryImage.getLayoutParams().width = 500;

        // Set the name and latin name.
        final TextView name = findViewById(R.id.telemetry_name);
        final TextView latinName = findViewById(R.id.telemetry_latin_name);

        name.setText(telemetryDetails.name);
        latinName.setText(String.format("(%s)", telemetryDetails.latinName));

        // Set the mass, age, and area.
        final TextView mass = findViewById(R.id.telemetry_mass);
        final TextView age = findViewById(R.id.telemetry_age);
        final TextView dist = findViewById(R.id.telemetry_dist);

        // Shorten and abbreviate the mass, age, and area.
        double massValue = telemetryDetails.mass;
        String massUnit = "kg";

        if (massValue > 1000) {
            massValue /= 1000;
            massUnit = "t";
        }

        if (massValue > 1000) {
            massValue /= 1000;
            massUnit = "kt";
        }

        if (massValue > 1000) {
            massValue /= 1000;
            massUnit = "Mt";
        }

        if (massValue > 1000) {
            massValue /= 1000;
            massUnit = "Gt";
        }

        if (massValue > 1000) {
            massValue /= 1000;
            massUnit = "Tt";
        }

        if (massValue > 1000) {
            massValue /= 1000;
            massUnit = "Pt";
        }

        if (massValue > 1000) {
            massValue /= 1000;
            massUnit = "Et";
        }

        if (massValue > 1000) {
            massValue /= 1000;
            massUnit = "Zt";
        }

        if (massValue > 1000) {
            massValue /= 1000;
            massUnit = "Yt";
        }

        double ageValue = telemetryDetails.age;
        String ageUnit = "B yrs";

        if (ageValue > 1000) {
            ageValue /= 1000;
            ageUnit = "kyrs";
        }

        if (ageValue > 1000) {
            ageValue /= 1000;
            ageUnit = "Myrs";
        }

        if (ageValue > 1000) {
            ageValue /= 1000;
            ageUnit = "Byrs";
        }

        if (ageValue > 1000) {
            ageValue /= 1000;
            ageUnit = "Gyrs";
        }

        if (ageValue > 1000) {
            ageValue /= 1000;
            ageUnit = "Tyrs";
        }

        if (ageValue > 1000) {
            ageValue /= 1000;
            ageUnit = "Pyrs";
        }

        if (ageValue > 1000) {
            ageValue /= 1000;
            ageUnit = "Eyrs";
        }

        if (ageValue > 1000) {
            ageValue /= 1000;
            ageUnit = "Zyrs";
        }

        if (ageValue > 1000) {
            ageValue /= 1000;
            ageUnit = "Yyrs";
        }

        double distValue = telemetryDetails.distanceFromEarth;
        String distUnit = "km";

        if (distValue > 1000) {
            distValue /= 1000;
            distUnit = "Mm";
        }

        if (distValue > 1000) {
            distValue /= 1000;
            distUnit = "Gm";
        }

        if (distValue > 1000) {
            distValue /= 1000;
            distUnit = "Tm";
        }

        if (distValue > 1000) {
            distValue /= 1000;
            distUnit = "Pm";
        }

        if (distValue > 1000) {
            distValue /= 1000;
            distUnit = "Em";
        }

        if (distValue > 1000) {
            distValue /= 1000;
            distUnit = "Zm";
        }

        if (distValue > 1000) {
            distValue /= 1000;
            distUnit = "Ym";
        }

        // Set each value to have only 2 decimal places.
        mass.setText(String.format("~%.2f %s", massValue, massUnit));
        age.setText(String.format("%.2f %s", ageValue, ageUnit));
        dist.setText(String.format("%.2f %s", distValue, distUnit));

        // Set the description.
        final TextView description = findViewById(R.id.telemetry_description);
        description.setText(telemetryDetails.description);

        // Add the images to the gallery
        final GridView gallery = findViewById(R.id.telemetry_gallery);

        final ArrayList<String> castedGalleryImages = new ArrayList<>();

        for (int i = 0; i < telemetryDetails.galleryPaths.length; i++) {
            castedGalleryImages.add(String.valueOf(telemetryDetails.galleryPaths[i]));
        }

        final TelemetryGalleryAdapter galleryAdapter =
                new TelemetryGalleryAdapter(this, castedGalleryImages);

        gallery.setAdapter(galleryAdapter);
    }
}