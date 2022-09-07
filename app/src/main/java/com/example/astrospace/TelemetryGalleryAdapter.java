package com.example.astrospace;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class TelemetryGalleryAdapter extends ArrayAdapter<String> {

    public TelemetryGalleryAdapter(@NonNull Context context, ArrayList<String> telemetryGalleryImages) {
        super(context, 0, telemetryGalleryImages);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.gallery_image, parent, false);
        }

        int imgId = Integer.parseInt(getItem(position));

        final ImageView imageView = listItemView.findViewById(R.id.telemetry_image);

        imageView.setImageResource(imgId);

        // Curve the corners of the image with a radius of 20.
        imageView.setClipToOutline(true);

        return listItemView;
    }
}
