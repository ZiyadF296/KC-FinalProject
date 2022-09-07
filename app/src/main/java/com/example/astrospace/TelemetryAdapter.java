package com.example.astrospace;

import android.content.Intent;
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
public class TelemetryAdapter extends RecyclerView.Adapter<TelemetryAdapter.TelemetryView> {
    // List with [TelemetryDetails] type
    private final List<TelemetryDetails> list;

    /**
     * View Holder class which extends RecyclerView.ViewHolder
     */
    public static class TelemetryView extends RecyclerView.ViewHolder {
        final View layout;
        final ImageView image;
        final TextView title;
        final TextView description;

        /**
         * Parameterised constructor for View Holder class which takes the view as a parameter.
         */
        public TelemetryView(View view) {
            super(view);

            layout = view.findViewById(R.id.layout_view);
            image = view.findViewById(R.id.image);
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
        }
    }

    /**
     * Constructor for adapter class which takes a list of String type
     */
    public TelemetryAdapter(List<TelemetryDetails> horizontalList) {
        this.list = horizontalList;
    }

    /**
     * Override onCreateViewHolder which deals with the inflation of the card layout
     * as an item for the RecyclerView.
     */
    @NonNull
    @Override
    public TelemetryView onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.telemetry_tile, parent, false);

        return new TelemetryView(itemView);
    }

    /**
     * Override onBindViewHolder which deals with the setting of different data and
     * methods related to clicks on particular items of the RecyclerView.
     */
    @Override
    public void onBindViewHolder(@NonNull final TelemetryView holder, final int position) {
        final TelemetryDetails info = list.get(position);

        // Add a margin to the first item with 50dp to the left.
        if (info == this.list.get(0)) {
            final ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
            params.setMarginStart(75);
            holder.itemView.setLayoutParams(params);
        }

        // If the item is the last item, add a margin to the right.
        if (info == this.list.get(this.list.size() - 1)) {
            final ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
            params.setMarginEnd(75);
            holder.itemView.setLayoutParams(params);
        }

        holder.image.setImageResource(info.imageSource);
        holder.title.setText(info.name);
        holder.description.setText(info.description);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the details view and also pass in the item that was clicked.
                final Intent intent = new Intent(v.getContext(), DetailsView.class);
                intent.putExtra("telemetry", info);

                v.getContext().startActivity(intent);
            }
        });
    }

    /**
     * Override getItemCount which Returns the length of the RecyclerView.
     */
    @Override
    public int getItemCount() {
        return list.size();
    }
}
