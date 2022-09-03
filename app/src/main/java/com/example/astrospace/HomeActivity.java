package com.example.astrospace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {
    // Get all the possible categories from [TelemetryDetails.spaceObjects].
    // This is used to create the category buttons in the home screen.
    final ArrayList<String> categories = new ArrayList<>();

    // The currently selected category.
    final String[] selectedCategory = { "All" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Remove the app bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        // Set the background color to black
        getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.bg));

        // Set the system background color to black
        getWindow().setStatusBarColor(getResources().getColor(R.color.bg));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.bg));

        // Add the "All" category first.
        categories.add("All");

        for (int i = 0; i < TelemetryDetails.spaceObjects.length; i++) {
            // Check if the category is already added to the list.
            boolean isAdded = false;

            for (String category : categories) {
                if (TelemetryDetails.spaceObjects[i].category.equals(category)) {
                    isAdded = true;
                    break;
                }
            }

            // If the category is not added, add it to the list.
            if (!isAdded) {
                categories.add(TelemetryDetails.spaceObjects[i].category);
            }
        }

        // Add all the categories to the home screen LinearLayout.
        final LinearLayout categoriesList = findViewById(R.id.categories_list);

        // Create a view with 50 dp of width
        final View widthView1 = new View(this);
        widthView1.setLayoutParams(new LinearLayout.LayoutParams(100, 0));

        // Add the first view to the LinearLayout
        categoriesList.addView(widthView1);

        for (String category : categories) {
            // Create a new button for the category.
            final View categoryButton = LayoutInflater.from(this).inflate(R.layout.category_tile, categoriesList,
                    false);

            // Set the category name.
            final TextView text = categoryButton.findViewById(R.id.category_name);

            text.setText(category);

            // Change the color to white if the category is not selected.
            if (!category.equals(selectedCategory[0])) {
                text.setTextColor(getResources().getColor(R.color.white));

                final View indicator = categoryButton.findViewById(R.id.underline);

                indicator.setVisibility(View.GONE);
            }

            categoryButton.setOnClickListener(v -> {
                if (category.equals(selectedCategory[0])) {
                    return;
                }

                // Change the color of the selected category to white.
                text.setTextColor(getResources().getColor(R.color.orange));

                final View indicator = categoryButton.findViewById(R.id.underline);

                indicator.setVisibility(View.VISIBLE);

                // Set the width of the indicator half the size of the text.
                final int width = (int) text.getPaint().measureText(text.getText().toString());

                indicator.getLayoutParams().width = width / 2;

                // Change the color of the previously selected category to white.
                for (int i = 0; i < categories.size(); i++) {
                    final String text1 = categories.get(i);

                    // If the text is equal to the currently selected category, change
                    // the color to white.
                    if (text1.equals(selectedCategory[0])) {
                        final View categoryButton1 = categoriesList.getChildAt(i + 1);

                        final TextView categoryButtonText = categoryButton1.findViewById(R.id.category_name);

                        categoryButtonText.setTextColor(getResources().getColor(R.color.white));

                        final View childIndicator = categoryButton1.findViewById(R.id.underline);

                        childIndicator.setVisibility(View.GONE);

                        break;
                    }
                }

                // Set the selected category.
                selectedCategory[0] = text.getText().toString();

                // Update the list of objects.
                updateTelemetries(selectedCategory[0]);
            });

            // Add the button to the home screen.
            categoriesList.addView(categoryButton);

            updateTelemetries(selectedCategory[0]);
        }

        final View widthView2 = new View(this);
        widthView2.setLayoutParams(new LinearLayout.LayoutParams(75, 0));
    }

    /**
     * This method is called when the user clicks on any category button.
     *
     * Will set the view to display the objects in the selected category.
     *
     * This is idempotent and will ignore if same category is selected.
     *
     * @param category The category to display.
     *
     * If the category is "All", all the objects will be displayed.
     */
    protected void updateTelemetries(@NonNull String category) {
        final RecyclerView telemetryRecycleView = findViewById(R.id.telemetry_list);
        final RecyclerView.LayoutManager recyclerViewLayoutManager =
                new LinearLayoutManager(getApplicationContext());

        // Set LayoutManager on Recycler View
        telemetryRecycleView.setLayoutManager(recyclerViewLayoutManager);

        // Create a new list to hold the filtered objects.
        final ArrayList<TelemetryDetails> filteredTelemetries = new ArrayList<>();

        // If the category is "All", add all the objects to the list.
        if (category.equals("All")) {
            Collections.addAll(filteredTelemetries, TelemetryDetails.spaceObjects);
        } else {
            // Add all the objects with the selected category to the list.
            for (TelemetryDetails telemetry : TelemetryDetails.spaceObjects) {
                if (telemetry.category.equals(category)) {
                    filteredTelemetries.add(telemetry);
                }
            }
        }

        // Calling constructor of adapter with [filteredTelemetries] list as a parameter
        final TelemetryAdapter adapter = new TelemetryAdapter(filteredTelemetries);

        // Set Horizontal Layout Manager for Recycler view
        telemetryRecycleView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));

        // Set adapter on recycler view.
        telemetryRecycleView.setAdapter(adapter);
    }
}