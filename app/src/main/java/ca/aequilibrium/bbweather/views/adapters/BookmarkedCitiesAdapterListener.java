package ca.aequilibrium.bbweather.views.adapters;

import ca.aequilibrium.bbweather.models.BookmarkedCity;

public interface BookmarkedCitiesAdapterListener {
    void onBookmarkedCityClicked(BookmarkedCity bookmarkedCity);

    void onRemoveBookMarkCityButtonClicked(BookmarkedCity bookmarkedCity);
}
