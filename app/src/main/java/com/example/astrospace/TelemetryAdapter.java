package com.example.astrospace;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * The adapter class which extends RecyclerView Adapter
 */
public class Adapter extends RecyclerView.Adapter<Adapter.MyView> {

    // List with String type
    private final List<TelemetryDetails> list;

    /**
     * View Holder class which extends RecyclerView.ViewHolder
     */
    public static class MyView extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title;
        TextView description;

        /**
         * Parameterised constructor for View Holder class which takes the view as a parameter.
         */
        public MyView(View view) {
            super(view);

            image = view.findViewById(R.id.image);
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
        }
    }

    /**
     *Constructor for adapter class which takes a list of String type
     */
    public Adapter(List<TelemetryDetails> horizontalList) {
        this.list = horizontalList;
    }

    /**
     * Override onCreateViewHolder which deals with the inflation of the card layout
     * as an item for the RecyclerView.
     */
    @NonNull
    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate UI component.
        final View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.telemetry_tile, parent, false);

        return new MyView(itemView);
    }

    /**
     * Override onBindViewHolder which deals with the setting of different data and
     * methods related to clicks on particular items of the RecyclerView.
     */
    @Override
    public void onBindViewHolder(final MyView holder, final int position) {
        final TelemetryDetails info = list.get(position);

        holder.image.setImageResource(info.imageSource);
        holder.title.setText(info.name);
        holder.description.setText(info.description);
    }

    /**
     * Override getItemCount which Returns the length of the RecyclerView.
     */
    @Override
    public int getItemCount() {
        return list.size();
    }
}
