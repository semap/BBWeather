package ca.aequilibrium.bbweather.managers;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ca.aequilibrium.bbweather.models.BookmarkedCity;
import ca.aequilibrium.bbweather.models.Coord;
import ca.aequilibrium.bbweather.utils.ResultCallback;
import ca.aequilibrium.bbweather.utils.TaskResult;
import ca.aequilibrium.bbweather.utils.asynctasks.CallbackAsyncTask;
import ca.aequilibrium.bbweather.utils.asynctasks.GeocodeBookmarkedCityAsyncTask;
import ca.aequilibrium.bbweather.utils.asynctasks.LoadBookmarkedCitiesAsyncTask;
import ca.aequilibrium.bbweather.utils.asynctasks.SaveBookmarkedCitiesAsyncTask;

/**
 * The implementation of BookmarkedLocationService. This class uses a local file to store the bookmarked cities.
 */
public class BookmarkedLocationManagerImpl implements BookmarkedLocationManager {

    private static final String BOOKMARKS_FILE_NAME = "bookmarkedcity.json";
    private static final String TAG = BookmarkedLocationManagerImpl.class.getSimpleName();
    private final MutableLiveData<List<BookmarkedCity>> bookmarkedCities;
    // the Context is for getting Geocoder, it is needed for decode the LatLng
    private Context context;


    public BookmarkedLocationManagerImpl(final Context context) {
        this.context = context;
        this.bookmarkedCities = new MutableLiveData<>();
        this.initBookmarkedCities();
    }

    @Override
    public void addBookmarkedCityByLocation(final Coord coord, final ResultCallback<BookmarkedCity> resultCallback) {

        CallbackAsyncTask<Coord, Void, BookmarkedCity> task = new GeocodeBookmarkedCityAsyncTask(context, new ResultCallback<BookmarkedCity>() {
            @Override
            public void callback(TaskResult<BookmarkedCity> geoCodeResult) {
                if (geoCodeResult.error != null) {
                    if (resultCallback != null) {
                        resultCallback.callback(geoCodeResult);
                    }
                } else {
                    BookmarkedCity city = geoCodeResult.result;
                    insertBookmarkedCity(city, resultCallback);
                }
            }
        });

        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, coord);
    }

    @Override
    public void remove(final BookmarkedCity bookmarkedCity, final ResultCallback<Boolean> resultCallback) {

        List<BookmarkedCity> list = this.bookmarkedCities.getValue();
        list.remove(bookmarkedCity);

        asyncSaveBookmarkedCities(list, new ResultCallback<List<BookmarkedCity>>() {
            @Override
            public void callback(@NonNull TaskResult<List<BookmarkedCity>> taskResult) {
                if (taskResult.error != null) {
                    if (resultCallback != null) {
                        resultCallback.callback(new TaskResult<Boolean>(taskResult.error));
                    }
                } else {
                    bookmarkedCities.postValue(taskResult.result);
                    if (resultCallback != null) {
                        resultCallback.callback(new TaskResult<>(true));
                    }
                }
            }
        });
    }

    @Override
    public void removeAll(final ResultCallback<Integer> resultCallback) {
        List<BookmarkedCity> oldList = this.bookmarkedCities.getValue();
        final Integer count = oldList == null ? 0 : oldList.size();
        List<BookmarkedCity> list = new ArrayList<>();

        asyncSaveBookmarkedCities(list, new ResultCallback<List<BookmarkedCity>>() {
            @Override
            public void callback(@NonNull TaskResult<List<BookmarkedCity>> taskResult) {
                if (taskResult.error != null) {
                    if (resultCallback != null) {
                        resultCallback.callback(new TaskResult<Integer>(taskResult.error));
                    }
                } else {
                    bookmarkedCities.postValue(taskResult.result);
                    if (resultCallback != null) {
                        resultCallback.callback(new TaskResult<>(count));
                    }
                }
            }
        });
    }

    @Override
    public LiveData<List<BookmarkedCity>> getBookmarkedCities() {
        return bookmarkedCities;
    }

    /**
     * Async insert a bookmarked city
     *
     * @param city
     * @param resultCallback
     */
    private void insertBookmarkedCity(final BookmarkedCity city, final ResultCallback<BookmarkedCity> resultCallback) {
        final List<BookmarkedCity> list = bookmarkedCities.getValue();
        list.add(city);

        asyncSaveBookmarkedCities(list, new ResultCallback<List<BookmarkedCity>>() {
            @Override
            public void callback(@NonNull TaskResult<List<BookmarkedCity>> taskResult) {
                if (taskResult.error != null) {
                    if (resultCallback != null) {
                        resultCallback.callback(new TaskResult<BookmarkedCity>(taskResult.error));
                    }
                } else {
                    bookmarkedCities.postValue(taskResult.result);
                    if (resultCallback != null) {
                        resultCallback.callback(new TaskResult<>(city));
                    }
                }
            }
        });
    }

    /**
     * Save the boookmarked cities to the local file
     *
     * @throws IOException
     */
    private void asyncSaveBookmarkedCities(final List<BookmarkedCity> cities, ResultCallback<List<BookmarkedCity>> resultCallback) {
        SaveBookmarkedCitiesAsyncTask task = new SaveBookmarkedCitiesAsyncTask(context, BOOKMARKS_FILE_NAME, resultCallback);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, cities);
    }


    /**
     * Async read the bookmarked cities from the local file.
     */
    private void initBookmarkedCities() {

        LoadBookmarkedCitiesAsyncTask asyncTask = new LoadBookmarkedCitiesAsyncTask(context, new ResultCallback<List<BookmarkedCity>>() {
            @Override
            public void callback(@NonNull TaskResult<List<BookmarkedCity>> taskResult) {
                if (taskResult.error != null) {
                    // the null value indicates that there was a exception when init, the presentation layout should handle it.
                    bookmarkedCities.postValue(null);
                } else {
                    bookmarkedCities.postValue(taskResult.result);
                }
            }
        });

        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, BOOKMARKS_FILE_NAME);
    }
}
