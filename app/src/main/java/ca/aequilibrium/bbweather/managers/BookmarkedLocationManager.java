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

    /**
     * Add a BookmarkedCity by coordinator. The function will lookup the city name from the coordinator.
     * @param coord the lat / lng of the bookmark.
     * @param resultCallback The callback of the result. If successful, the callback will contain the
     *                       newly created BookmarkedCity. If failed, the callback will contain the exception.
     */
    void addBookmarkedCityByLocation(Coord coord, ResultCallback<BookmarkedCity> resultCallback);

    /**
     * Remove a bookmarkedCities.
     * @param resultCallback The result of this method, if successful, the result in the resultCallback
     *                       is true. If failed, the result will have the exception.
     */
    void remove(BookmarkedCity bookmarkedCity, ResultCallback<Boolean> resultCallback);

    /**
     * Remove all the bookmarkedCities.
     * @param resultCallback The result of this method, if successful, the result has the number
     *                       of BookmarkedCity that has been removed. If failed, the result will
     *                       have the exception.
     */
    void removeAll(ResultCallback<Integer> resultCallback);

    /**
     * Given a name, return the Coord (Lat/Lng) of the name.
     * @param name
     * @param resultCallback
     */
    void getCoordFromName(String name, ResultCallback<Coord> resultCallback);

    // The observable of the bookmarkedCities, this is the majoy "output" of the this manager
    /**
     * The LiveData which keeps the list of the BookmarkedCity. Whenever a list is modified,
     *  the liveDate will emit a new element to notify the observers.
     * @return
     */
    LiveData<List<BookmarkedCity>> getBookmarkedCities();
}