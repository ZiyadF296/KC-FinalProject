package com.example.astrospace;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import java.util.ArrayList;

public class FavoritesManager {
    // A list of the favorite telemetries.
    private static final ArrayList<TelemetryDetails> FAVORITE_TELEMETRIES = new ArrayList<>();

    public static void initFromDisk(Context context) {
        // Clear the list.
        FAVORITE_TELEMETRIES.clear();

        // Get the shared preferences.
        final SharedPreferences sharedPreferences =
                context.getSharedPreferences("favorites", Context.MODE_PRIVATE);

        final String[] allKeys = sharedPreferences.getAll().keySet().toArray(new String[0]);

        // Add the favorite telemetries to the list.
        for (String favoriteTelemetryIndex : allKeys) {
            // Get the telemetry latinName from the index.
            final String telemetryLatinName = sharedPreferences.getString(favoriteTelemetryIndex, "");

            // Get the telemetry from the latinName.
            for (TelemetryDetails telemetry : TelemetryDetails.getTelemetries()) {
                if (telemetry.latinName.equals(telemetryLatinName)) {
                    FAVORITE_TELEMETRIES.add(telemetry);
                    break;
                }
            }
        }
    }

    private static void saveToDisk(Context context) {
        // Save the [FAVORITE_TELEMETRIES] list to disk using [SharedPreferences] so that it
        // can be loaded when the app is opened again.
        final SharedPreferences sharedPreferences = context.
                getSharedPreferences("favorites", Context.MODE_PRIVATE);

        final SharedPreferences.Editor editor = sharedPreferences.edit();

        // Clear the previous list.
        editor.clear();

        // Add the new list.
        for (int i = 0; i < FAVORITE_TELEMETRIES.size(); i++) {
            editor.putString(String.valueOf(i), FAVORITE_TELEMETRIES.get(i).latinName);
        }

        editor.apply();
    }

    // Add a telemetry to the favorites list.
    public static void addFavorite(Context context, TelemetryDetails telemetryDetails) {
        FAVORITE_TELEMETRIES.add(telemetryDetails);
        saveToDisk(context);
    }

    // Remove a telemetry from the favorites list.
    public static void removeFavorite(Context context, TelemetryDetails telemetryDetails) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FAVORITE_TELEMETRIES.removeIf(telemetry -> telemetry.latinName.equals(telemetryDetails.latinName));
        }

        saveToDisk(context);
    }

    // Get the favorites list.
    public static ArrayList<TelemetryDetails> getFavorites() {
        return FAVORITE_TELEMETRIES;
    }

    // Check if a telemetry is in the favorites list.
    public static boolean isFavorite(TelemetryDetails telemetryDetails) {
        // Iterate through the favorites list and check if the passed in telemetry name is
        // matching any of the favorites.
        for (TelemetryDetails telemetry : FAVORITE_TELEMETRIES) {
            if (telemetry.latinName.equals(telemetryDetails.latinName)) {
                return true;
            }
        }

        return false;
    }

    // Get the number of favorites.
    public static int getNumberOfFavorites() {
        return FAVORITE_TELEMETRIES.size();
    }
}
