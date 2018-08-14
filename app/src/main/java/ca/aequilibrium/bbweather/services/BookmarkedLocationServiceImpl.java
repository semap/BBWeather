package ca.aequilibrium.bbweather.services;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import ca.aequilibrium.bbweather.models.BookmarkedCity;
import ca.aequilibrium.bbweather.models.Coord;
import ca.aequilibrium.bbweather.utils.TaskResult;

/**
 * The implementation of BookmarkedLocationService. This class uses a local file to store the bookmarked cities.
 */
public class BookmarkedLocationServiceImpl implements BookmarkedLocationService {

    private static final String BOOKMARKS_FILE_NAME = "bookmarkedcity.json";
    private static final String TAG = BookmarkedLocationServiceImpl.class.getSimpleName();
    private static final String UNKWOWN_CITY_NAME = "Unknown";

    // the Context is for getting Geocoder, it is needed for decode the LatLng
    private Context context;

    private MutableLiveData<List<BookmarkedCity>> bookmarkedCity;


    public BookmarkedLocationServiceImpl(Context context) {
        this.context = context;
        this.bookmarkedCity = new MutableLiveData<>();
        this.bookmarkedCity.setValue(new ArrayList<BookmarkedCity>());

        try {
            load();
        } catch (IOException e) {

            bookmarkedCity.setValue(null);
        }
    }

    @Override
    public void addBookmarkedCityByLocation(Coord coord, ResultCallback<BookmarkedCity> resultCallback) {
        try {
            String cityName = mapToCityName(coord);
            if (cityName == null) {
                cityName = UNKWOWN_CITY_NAME;
            }
            BookmarkedCity city = new BookmarkedCity();
            city.setId(UUID.randomUUID().toString());
            city.setCoord(coord);
            city.setName(cityName);
            List<BookmarkedCity> list = bookmarkedCity.getValue();
            list.add(city);

            bookmarkedCity.setValue(list);
            save();

            if (resultCallback != null) {
                resultCallback.callback(new TaskResult<>(city));
            }

        } catch (IOException ioe) {
            // failed
            if (resultCallback != null) {
                resultCallback.callback(new TaskResult<BookmarkedCity>(ioe));
            }
        }
    }

    @Override
    public void remove(BookmarkedCity bookmarkedCity, ResultCallback<Boolean> resultCallback) {

        List<BookmarkedCity> list = this.bookmarkedCity.getValue();
        Boolean result = list.remove(bookmarkedCity);

        this.bookmarkedCity.setValue(list);
        try {
            save();
        } catch (IOException e) {
            if (resultCallback != null) {
                resultCallback.callback(new TaskResult<Boolean>(e));
            }
        }

        if (resultCallback != null) {
            resultCallback.callback(new TaskResult<>(result));
        }
    }

    @Override
    public void removeAll(ResultCallback<Boolean> resultCallback) {

        this.bookmarkedCity.setValue(new ArrayList<BookmarkedCity>());
        try {
            save();
        } catch (IOException e) {
            if (resultCallback != null) {
                resultCallback.callback(new TaskResult<Boolean>(e));
            }
        }

        if (resultCallback != null) {
            resultCallback.callback(new TaskResult<>(true));
        }
    }

    @Override
    public LiveData<List<BookmarkedCity>> getBookmarkedCities() {
        return bookmarkedCity;
    }

    private String mapToCityName(final Coord location) throws IOException {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        String cityName = null;
        List<Address> addresses = geocoder.getFromLocation(location.getLat(), location.getLon(), 1);
        if (addresses.isEmpty()) {
            cityName = null;
        } else {
            cityName = addresses.get(0).getLocality();
        }

        return cityName;
    }


    /**
     *  Save the boookmarked cities to the local file
     * @throws IOException
     */
    private void save() throws IOException {
        FileOutputStream outputStream = null;

        try {
            outputStream = context.openFileOutput(BOOKMARKS_FILE_NAME, Context.MODE_PRIVATE);
            List<BookmarkedCity> list = this.bookmarkedCity.getValue();
            String json = new Gson().toJson(list);
            outputStream.write(json.getBytes());
        } catch (IOException e) {
            Log.e(TAG, "failed to save the bookmarked city into the local file.", e);
            throw e;
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }


    /**
     * Read the bookmarked cities from the local file
     * @throws IOException
     */
    private void load() throws IOException {
        FileInputStream fileInputStream = null;
        try {
            StringBuilder text = new StringBuilder();
            fileInputStream = context.openFileInput(BOOKMARKS_FILE_NAME);
            BufferedReader br = new BufferedReader(new InputStreamReader(new BufferedInputStream(fileInputStream)));

            Gson gson = new Gson();
            Type listType = new TypeToken<List<BookmarkedCity>>() {}.getType();

            List<BookmarkedCity> list = gson.fromJson(br, listType);
            this.bookmarkedCity.setValue(list);
        } catch (FileNotFoundException fnfe) {
            Log.d(TAG, "Bookmark file does not exist.");
            this.bookmarkedCity.setValue(new ArrayList<BookmarkedCity>());
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        }
    }
}
