package ca.aequilibrium.bbweather.managers;

import android.arch.lifecycle.LiveData;

import java.util.List;

import ca.aequilibrium.bbweather.models.BookmarkedCity;
import ca.aequilibrium.bbweather.models.Coord;
import ca.aequilibrium.bbweather.utils.ResultCallback;


/**
 * Service for the bookmarked locations. The service is NOT aware of where the locations are stored,
 * It can be on the cloud or a local file or a local database, which means the requests can be Sync or Async.
 * So there is a ResultCallback parameter to handle the result (especially when there is an error)
 */
public interface BookmarkedLocationManager {

    void addBookmarkedCityByLocation(Coord coord, ResultCallback<BookmarkedCity> resultCallback);

    // remove a bookmarked city
    void remove(BookmarkedCity bookmarkedCity, ResultCallback<Boolean> resultCallback);

    void removeAll(ResultCallback<Boolean> resultCallback);

    // The observable of the bookmarkedCities, this is the majoy "output" of the this manager
    LiveData<List<BookmarkedCity>> getBookmarkedCities();
}