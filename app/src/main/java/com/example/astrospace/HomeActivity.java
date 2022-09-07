package com.example.astrospace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

        for (int i = 0; i < TelemetryDetails.getTelemetries().length; i++) {
            // Check if the category is already added to the list.
            boolean isAdded = false;

            for (String category : categories) {
                if (TelemetryDetails.getTelemetries()[i].category.equals(category)) {
                    isAdded = true;
                    break;
                }
            }

            // If the category is not added, add it to the list.
            if (!isAdded) {
                categories.add(TelemetryDetails.getTelemetries()[i].category);
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

        // Load the data from this API: https://api.spaceflightnewsapi.net/v3/articles
        // Format: JSON
        // {
        //    "id": 123,
        //    "title": "Title",
        //    "url": "https://example.com",
        //    "imageUrl": "https://example.com/image.png",
        //    "newsSite": "Example",
        //    "summary": "Summary",
        //    "publishedAt": "2021-01-01T00:00:00Z",
        //    "updatedAt": "2021-01-01T00:00:00Z",
        //    "featured": false,
        //    "launches": [],
        // }
        final ProgressBar progressBar = findViewById(R.id.latest_news_loading_indicator);

        progressBar.getIndeterminateDrawable()
                .setColorFilter(ContextCompat.getColor(this, R.color.orange), PorterDuff.Mode.SRC_IN );

        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "https://api.spaceflightnewsapi.net/v3/articles";

        // Request a string response from the provided URL.
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    final ArrayList<News> latestNews = new ArrayList<>();

                    // Parse the JSON data. It will be an array of objects.
                    try {
                        final JSONArray jsonArray = new JSONArray(response);

                        // Loop through the array of objects.
                        for (int i = 0; i < jsonArray.length(); i++) {
                            // Get the object at the current index.
                            final JSONObject jsonObject = jsonArray.getJSONObject(i);

                            // Get the id of the article.
                            final int id = jsonObject.getInt("id");

                            // Get the title of the article.
                            final String title = jsonObject.getString("title");

                            // Get the URL of the article.
                            final String newsUrl = jsonObject.getString("url");

                            // Get the image URL of the article.
                            final String imageUrl = jsonObject.getString("imageUrl");

                            // Get the news site of the article.
                            final String newsSite = jsonObject.getString("newsSite");

                            // Get the summary of the article.
                            final String summary = jsonObject.getString("summary");

                            // Get the published date of the article.
                            final String publishedAt = jsonObject.getString("publishedAt");

                            // Get the updated date of the article.
                            final String updatedAt = jsonObject.getString("updatedAt");

                            // Get the featured status of the article.
                            final boolean featured = jsonObject.getBoolean("featured");

                            // Create a new article object.
                            final News parsedNews = new News(id, title, newsUrl, imageUrl, newsSite, summary, publishedAt,
                                    updatedAt, featured);

                            // Add the article to the list.
                            latestNews.add(parsedNews);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    final LinearLayout newsList = findViewById(R.id.latest_news);

                    for (int i = 0; i < latestNews.size(); i++) {
                        final News news = latestNews.get(i);

                        final View newsItem = LayoutInflater.from(this).inflate(R.layout.news_tile, newsList, false);

                        final TextView title = newsItem.findViewById(R.id.news_title);
                        final TextView date = newsItem.findViewById(R.id.news_date);
                        final ImageView image = newsItem.findViewById(R.id.news_image);

                        title.setText(news.title);

                        // Format the date into a readable format.
                        final String year = news.publishedAt.split("-")[0];
                        final String month = news.publishedAt.split("-")[1];
                        final String day = news.publishedAt.split("-")[2].substring(0, 2);

                        final String dayName = DateUtils.dayOfWeekFromInt(Integer.parseInt(day), false);
                        final String monthName = DateUtils.monthFromInt(Integer.parseInt(month), true);

                        date.setText(String.format("%s, %s %s", dayName, monthName, year));

                        final String imageUrl = news.imageUrl;

                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            Picasso.get().load(imageUrl).into(image);
                        }

                        newsItem.setOnClickListener(v -> {
                            final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(news.url));

                            startActivity(intent);
                        });

                        newsList.addView(newsItem);
                    }

                    progressBar.setVisibility(View.GONE);
                },
                error -> System.out.println("Response is: " + error));

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        handleTabButtons();
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
            Collections.addAll(filteredTelemetries, TelemetryDetails.getTelemetries());
        } else {
            // Add all the objects with the selected category to the list.
            for (TelemetryDetails telemetry : TelemetryDetails.getTelemetries()) {
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

    final boolean[] initializedNewsTab = { false };

    protected void initNewsTab() {
        if (initializedNewsTab[0]) {
            return;
        }

        initializedNewsTab[0] = true;

        final ProgressBar progressBar = findViewById(R.id.news_loading_indicator);

        progressBar.getIndeterminateDrawable()
                .setColorFilter(ContextCompat.getColor(this, R.color.orange), PorterDuff.Mode.SRC_IN );

        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "https://api.spaceflightnewsapi.net/v3/articles";

        // Request a string response from the provided URL.
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    final ArrayList<News> latestNews = new ArrayList<>();

                    // Parse the JSON data. It will be an array of objects.
                    try {
                        final JSONArray jsonArray = new JSONArray(response);

                        // Loop through the array of objects.
                        for (int i = 0; i < jsonArray.length(); i++) {
                            // Get the object at the current index.
                            final JSONObject jsonObject = jsonArray.getJSONObject(i);

                            // Get the id of the article.
                            final int id = jsonObject.getInt("id");

                            // Get the title of the article.
                            final String title = jsonObject.getString("title");

                            // Get the URL of the article.
                            final String newsUrl = jsonObject.getString("url");

                            // Get the image URL of the article.
                            final String imageUrl = jsonObject.getString("imageUrl");

                            // Get the news site of the article.
                            final String newsSite = jsonObject.getString("newsSite");

                            // Get the summary of the article.
                            final String summary = jsonObject.getString("summary");

                            // Get the published date of the article.
                            final String publishedAt = jsonObject.getString("publishedAt");

                            // Get the updated date of the article.
                            final String updatedAt = jsonObject.getString("updatedAt");

                            // Get the featured status of the article.
                            final boolean featured = jsonObject.getBoolean("featured");

                            // Create a new article object.
                            final News parsedNews = new News(id, title, newsUrl, imageUrl, newsSite, summary, publishedAt,
                                    updatedAt, featured);

                            // Add the article to the list.
                            latestNews.add(parsedNews);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    final LinearLayout newsList = findViewById(R.id.descriptive_news_list);

                    for (int i = 0; i < latestNews.size(); i++) {
                        final News news = latestNews.get(i);

                        final View newsItem = LayoutInflater.from(this).inflate(R.layout.descriptive_news_tile, newsList, false);

                        final TextView title = newsItem.findViewById(R.id.news_title);
                        final TextView description = newsItem.findViewById(R.id.news_description);
                        final TextView publisher = newsItem.findViewById(R.id.news_publisher);
                        final TextView date = newsItem.findViewById(R.id.news_date);
                        final ImageView image = newsItem.findViewById(R.id.news_image);

                        title.setText(news.title);
                        description.setText(news.summary);
                        publisher.setText(news.newsSite);

                        // Format the date into a readable format.
                        final String year = news.publishedAt.split("-")[0];
                        final String month = news.publishedAt.split("-")[1];
                        final String day = news.publishedAt.split("-")[2].substring(0, 2);

                        final String dayName = DateUtils.dayOfWeekFromInt(Integer.parseInt(day), false);
                        final String monthName = DateUtils.monthFromInt(Integer.parseInt(month), true);

                        date.setText(String.format("%s, %s %s", dayName, monthName, year));

                        final String imageUrl = news.imageUrl;

                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            Picasso.get().load(imageUrl).into(image);
                        }

                        newsItem.setOnClickListener(v -> {
                            final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(news.url));

                            startActivity(intent);
                        });

                        newsList.addView(newsItem);
                    }

                    progressBar.setVisibility(View.GONE);
                },
                error -> System.out.println("Response is: " + error));

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    final boolean[] initializedSatTab = { false };

    protected void initSat() {
        if (initializedSatTab[0]) {
            return;
        }

        initializedSatTab[0] = true;

        final LinearLayout astronautsList = findViewById(R.id.astronauts_list);
        final ProgressBar astronautsProgressBar = findViewById(R.id.astronauts_loading_indicator);

        // The format of the API response is as follows:
        // {
        //      "number": 10,
        //      "people": [
        //          {
        //              "name": "...",
        //              "craft": "ISS"
        //          },
        //          ...
        //      ],
        //      "message": "success"
        // }
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "http://api.open-notify.org/astros.json";

        // Request a string response from the provided URL.
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    final ArrayList<AstronautCraft> astronauts = new ArrayList<>();

                    // Parse the JSON data. It will be an array of objects.
                    try {
                        final JSONObject jsonObject = new JSONObject(response);

                        // Loop through the array of people
                        for (int i = 0; i < jsonObject.getJSONArray("people").length(); i++) {
                            // Get the object at the current index.
                            final JSONObject currentAstronaut =
                                    jsonObject.getJSONArray("people").getJSONObject(i);

                            // Get the name of the astronaut.
                            final String name = currentAstronaut.getString("name");

                            // Get the craft the astronaut is on.
                            final String craft = currentAstronaut.getString("craft");

                            // Create a new astronaut object.
                            final AstronautCraft astronaut = new AstronautCraft(name, craft);

                            // Add the astronaut to the list.
                            astronauts.add(astronaut);
                        }

                        // Loop through the people.
                    } catch (JSONException e) {
                        e.printStackTrace();

                        // Show a toast message.
                        Toast.makeText(this, "Failed to load astronauts.", Toast.LENGTH_SHORT).show();
                    }

                    for (int i = 0; i < astronauts.size(); i++) {
                        final AstronautCraft news = astronauts.get(i);

                        final View astronautItem = LayoutInflater.from(this).inflate(R.layout.astronaut_tile, astronautsList, false);

                        final TextView title = astronautItem.findViewById(R.id.astronaut_name);
                        final TextView craft = astronautItem.findViewById(R.id.astronaut_craft);

                        title.setText(news.name);
                        craft.setText(news.craft);

                        astronautsList.addView(astronautItem);
                    }

                    astronautsProgressBar.setVisibility(View.GONE);
                },
                error -> System.out.println("Response is: " + error));

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    final boolean[] initializedDiscoverTab = { false };

    protected void initDiscoverTab() {
        if (initializedDiscoverTab[0]) {
            return;
        }

        initializedDiscoverTab[0] = true;

        final LinearLayout galleryList = findViewById(R.id.mars_gallery);
        final ProgressBar galleryProgressBar = findViewById(R.id.mars_gallery_loading_indicator);

        final RequestQueue queue = Volley.newRequestQueue(this);
        final String galleryUrl = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?api_key=EmGBIVXtEqzeUA9qv0kjlADxkuJGN7KK3IVeb6JX&sol=1000";

        // Request a string response from the provided URL.
        @SuppressLint("SetTextI18n")
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, galleryUrl,
                response -> {
                    final ArrayList<MarsGallery> marsGallery = new ArrayList<>();

                    // Parse the JSON data. It will be an array of objects.
                    try {
                        final JSONObject jsonObject = new JSONObject(response);

                        // Loop through the array of people
                        for (int i = 0; i < 50; i++) {
                            // Get the object at the current index.
                            final JSONObject currentAstronaut =
                                    jsonObject.getJSONArray("photos").getJSONObject(i);

                            final String cameraShortName = currentAstronaut.getJSONObject("camera").getString("name");
                            final String cameraFullName = currentAstronaut.getJSONObject("camera").getString("full_name");

                            final String imageUrl = currentAstronaut.getString("img_src");

                            final String roverName = currentAstronaut.getJSONObject("rover").getString("name");
                            final String landingDate = currentAstronaut.getJSONObject("rover").getString("landing_date");
                            final String roverStatus = currentAstronaut.getJSONObject("rover").getString("status");

                            // Create a new mars gallery object.
                            final MarsGallery gallery = new MarsGallery(cameraShortName, cameraFullName, imageUrl, roverName, roverStatus, landingDate);

                            // Add the astronaut to the list.
                            marsGallery.add(gallery);
                        }

                        // Loop through the people.
                    } catch (JSONException e) {
                        e.printStackTrace();

                        // Show a toast message.
                        Toast.makeText(this, "Failed to load space gallery.", Toast.LENGTH_SHORT).show();
                    }

                    for (int i = 0; i < marsGallery.size(); i++) {
                        final MarsGallery gallery = marsGallery.get(i);

                        final View galleryItem = LayoutInflater.from(this).inflate(R.layout.mars_gallery_tile, galleryList, false);

                        final ImageView image = galleryItem.findViewById(R.id.gallery_image);
                        final TextView roverActive = galleryItem.findViewById(R.id.rover_active);
                        final TextView takenBy = galleryItem.findViewById(R.id.taken_by);
                        final TextView dateLanded = galleryItem.findViewById(R.id.landing_date);

                        if (gallery.roverStatus.equals("active")) {
                            roverActive.setText("Active");
                            // Set the color green
                            roverActive.setTextColor(Color.parseColor("#00FF00"));
                        } else {
                            roverActive.setText("Inactive");
                            // Set the color red
                            roverActive.setTextColor(Color.parseColor("#FF0000"));
                        }

                        // Load the image from the URL.
                        if (gallery.imageUrl != null && !gallery.imageUrl.isEmpty()) {
                            Picasso.get().load(gallery.imageUrl).into(image);
                        }

                        takenBy.setText(String.format("Taken by %s with the %s", gallery.roverName, gallery.cameraName));

                        dateLanded.setText(String.format("Landed on %s", gallery.landingDate));

                        galleryList.addView(galleryItem);
                    }

                    galleryProgressBar.setVisibility(View.GONE);
                },
                error -> System.out.println("Response is: " + error));

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    protected void initFavoritesTab() {
        final LinearLayout favoritesList = findViewById(R.id.user_favorites_list);

        // Clear the list.
        favoritesList.removeAllViews();

        // Get the list of favorites from the shared preferences.
        final ArrayList<TelemetryDetails> favorites = FavoritesManager.getFavorites();

        final View favoriteArt = findViewById(R.id.no_favorite_lottie);

        // If there are no favorites, show the empty view.
        if (FavoritesManager.getNumberOfFavorites() == 0) {
            favoriteArt.setVisibility(View.VISIBLE);

            // Set the height.
            final LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 500);
            favoriteArt.setLayoutParams(params);

            return;
        } else {
            favoriteArt.setVisibility(View.GONE);
        }

        // Loop through the list of favorites and then add them to the favorites list.
        for (int i = 0; i < favorites.size(); i++) {
            final TelemetryDetails telemetry = favorites.get(i);

            final View telemetryItem = LayoutInflater.from(this).inflate(R.layout.favorites_tile, favoritesList, false);

            // Set the image
            final ImageView image = telemetryItem.findViewById(R.id.telemetry_image);
            image.setImageResource(telemetry.imageSource);

            // Set the title and description.
            final TextView title = telemetryItem.findViewById(R.id.telemetry_name);
            final TextView description = telemetryItem.findViewById(R.id.telemetry_description);

            title.setText(telemetry.name);
            description.setText(telemetry.description);

            // Set the click listener. We also want to reload the favorites tab once the user
            // exits the telemetry details activity.
            telemetryItem.setOnClickListener(v -> {
                final Intent intent = new Intent(this, DetailsView.class);
                intent.putExtra("telemetry", telemetry);

                startActivity(intent);
            });

            favoritesList.addView(telemetryItem);
        }
    }

    protected void handleTabButtons() {
        // Buttons
        final ImageView homeButton = findViewById(R.id.home_button);
        final ImageView newsButton = findViewById(R.id.news_button);
        final ImageView satButton = findViewById(R.id.sat_button);
        final ImageView discoverButton = findViewById(R.id.discover_button);
        final ImageView favoritesButton = findViewById(R.id.favorites_button);

        // Views
        final View homeView = findViewById(R.id.home_view);
        final View newsView = findViewById(R.id.news_view);
        final View satView = findViewById(R.id.sat_view);
        final View discoverView = findViewById(R.id.discover_view);
        final View favoritesView = findViewById(R.id.favorites_view);

        // Set the home view to be visible.
        homeView.setVisibility(View.VISIBLE);
        newsView.setVisibility(View.GONE);
        satView.setVisibility(View.GONE);
        discoverView.setVisibility(View.GONE);
        favoritesView.setVisibility(View.GONE);

        // Set on click listener for home button.
        homeButton.setOnClickListener(v -> {
            // Set the home button to be selected.
            homeButton.setImageResource(R.drawable.home_selected);
            newsButton.setImageResource(R.drawable.news_deselected);
            satButton.setImageResource(R.drawable.sat_deselected);
            discoverButton.setImageResource(R.drawable.discover_deselected);
            favoritesButton.setImageResource(R.drawable.favorites_deselected);

            // Set the home view to be visible.
            homeView.setVisibility(View.VISIBLE);
            newsView.setVisibility(View.GONE);
            satView.setVisibility(View.GONE);
            discoverView.setVisibility(View.GONE);
            favoritesView.setVisibility(View.GONE);
        });

        // Set on click listener for news button.
        newsButton.setOnClickListener(v -> {
            // Set the news button to be selected.
            homeButton.setImageResource(R.drawable.home_deselected);
            newsButton.setImageResource(R.drawable.news_selected);
            satButton.setImageResource(R.drawable.sat_deselected);
            discoverButton.setImageResource(R.drawable.discover_deselected);
            favoritesButton.setImageResource(R.drawable.favorites_deselected);

            // Set the news view to be visible.
            homeView.setVisibility(View.GONE);
            newsView.setVisibility(View.VISIBLE);
            satView.setVisibility(View.GONE);
            discoverView.setVisibility(View.GONE);
            favoritesView.setVisibility(View.GONE);

            initNewsTab();
        });

        // Set on click listener for sat button.
        satButton.setOnClickListener(v -> {
            // Set the sat button to be selected.
            homeButton.setImageResource(R.drawable.home_deselected);
            newsButton.setImageResource(R.drawable.news_deselected);
            satButton.setImageResource(R.drawable.sat_selected);
            discoverButton.setImageResource(R.drawable.discover_deselected);
            favoritesButton.setImageResource(R.drawable.favorites_deselected);

            // Set the sat view to be visible.
            homeView.setVisibility(View.GONE);
            newsView.setVisibility(View.GONE);
            satView.setVisibility(View.VISIBLE);
            discoverView.setVisibility(View.GONE);
            favoritesView.setVisibility(View.GONE);

            initSat();
        });

        // Set on click listener for discover button.
        discoverButton.setOnClickListener(v -> {
            // Set the discover button to be selected.
            homeButton.setImageResource(R.drawable.home_deselected);
            newsButton.setImageResource(R.drawable.news_deselected);
            satButton.setImageResource(R.drawable.sat_deselected);
            discoverButton.setImageResource(R.drawable.discover_selected);
            favoritesButton.setImageResource(R.drawable.favorites_deselected);

            // Set the discover view to be visible.
            homeView.setVisibility(View.GONE);
            newsView.setVisibility(View.GONE);
            satView.setVisibility(View.GONE);
            discoverView.setVisibility(View.VISIBLE);
            favoritesView.setVisibility(View.GONE);

            initDiscoverTab();
        });

        // Set on click listener for favorites button.
        favoritesButton.setOnClickListener(v -> {
            // Set the favorites button to be selected.
            homeButton.setImageResource(R.drawable.home_deselected);
            newsButton.setImageResource(R.drawable.news_deselected);
            satButton.setImageResource(R.drawable.sat_deselected);
            discoverButton.setImageResource(R.drawable.discover_deselected);
            favoritesButton.setImageResource(R.drawable.favorites_selected);

            // Set the favorites view to be visible.
            homeView.setVisibility(View.GONE);
            newsView.setVisibility(View.GONE);
            satView.setVisibility(View.GONE);
            discoverView.setVisibility(View.GONE);
            favoritesView.setVisibility(View.VISIBLE);

            initFavoritesTab();
        });
    }
}