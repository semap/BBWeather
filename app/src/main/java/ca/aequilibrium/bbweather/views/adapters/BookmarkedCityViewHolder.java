package ca.aequilibrium.bbweather.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ca.aequilibrium.bbweather.R;

public class BookmarkedCityViewHolder extends RecyclerView.ViewHolder {

    TextView cityNameTextView;
    ImageView closeButton;

    public BookmarkedCityViewHolder(View itemView) {
        super(itemView);
        cityNameTextView = itemView.findViewById(R.id.cityName);
        closeButton = itemView.findViewById(R.id.removeBookmark);
    }
}
