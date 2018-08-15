package ca.aequilibrium.bbweather.views.adapters;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ca.aequilibrium.bbweather.R;
import ca.aequilibrium.bbweather.models.BookmarkedCity;

public class BookmarkedCitiesAdapter extends RecyclerView.Adapter<BookmarkedCityViewHolder> {

    private List<BookmarkedCity> bookmarkedCities = new ArrayList<>();
    private BookmarkedCitiesAdapterListener listener;

    @Override
    public BookmarkedCityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BookmarkedCityViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.view_bookmarked_city, parent, false));
    }

    @Override
    public void onBindViewHolder(BookmarkedCityViewHolder holder, int position) {
        final BookmarkedCity city = bookmarkedCities.get(position);
        holder.cityNameTextView.setText(city.getName());
        holder.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onRemoveBookMarkCityButtonClicked(city);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onBookmarkedCityClicked(city);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookmarkedCities.size();
    }

    public void setBookmarkedCities(List<BookmarkedCity> bookmarkedCities) {
        BookmarkedCityDiffCallback locationDiffCallback = new BookmarkedCityDiffCallback(this.bookmarkedCities, bookmarkedCities);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(locationDiffCallback);
        this.bookmarkedCities.clear();
        this.bookmarkedCities.addAll(bookmarkedCities);
        diffResult.dispatchUpdatesTo(this);
    }

    public void setListener(BookmarkedCitiesAdapterListener listener) {
        this.listener = listener;
    }

}
