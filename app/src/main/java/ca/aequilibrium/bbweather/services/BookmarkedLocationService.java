package ca.aequilibrium.bbweather.services;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import ca.aequilibrium.bbweather.models.BookmarkedCity;
import ca.aequilibrium.bbweather.models.Location;
import ca.aequilibrium.bbweather.utils.TaskResult;


/**
 * Service for the bookmarked locations. The service is NOT aware of where the locations are stored,
 * It can be on the cloud or a local file or a local database, which means the requests can be Sync or Async.
 * So there is a ResultCallback parameter to handle the result (especially when there is an error)
 */
public interface BookmarkedLocationService {

    void addBookmarkedCityByLocation(Location location, ResultCallback<BookmarkedCity> resultCallback);

    // remove a bookmarked city
    void remove(BookmarkedCity bookmarkedCity, ResultCallback<Boolean> resultCallback);

    void removeAll(ResultCallback<Boolean> resultCallback);

    // The observable of the bookmarkedCities, this is the majoy "output" of the this service
    LiveData<List<BookmarkedCity>> getBookmarkedCities();
}