package ca.aequilibrium.bbweather.utils.asynctasks;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import ca.aequilibrium.bbweather.models.BookmarkedCity;
import ca.aequilibrium.bbweather.models.Coord;
import ca.aequilibrium.bbweather.utils.ResultCallback;
import ca.aequilibrium.bbweather.utils.TaskResult;

public class GeocodeBookmarkedCityAsyncTask extends CallbackAsyncTask<Coord, Void, BookmarkedCity> {
    private static final String UNKNOWN_CITY_NAME = "Unknown";
    private WeakReference<Context> contextRef;

    public GeocodeBookmarkedCityAsyncTask(Context context, ResultCallback<BookmarkedCity> resultCallback) {
        super(resultCallback);
        this.contextRef = new WeakReference<>(context);
    }

    @Override
    protected TaskResult<BookmarkedCity> doInBackground(Coord... coords) {
        Context context = contextRef.get();
        if (coords == null || coords.length == 0 || context == null) {
            return new TaskResult<BookmarkedCity>((BookmarkedCity) null);
        }

        Coord coord = coords[0];
        try {
            String cityName = getCityName(coord, context);
            if (cityName == null) {
                cityName = UNKNOWN_CITY_NAME;
            }
            BookmarkedCity city = new BookmarkedCity();
            city.setId(UUID.randomUUID().toString());
            city.setCoord(coord);
            city.setName(cityName);

            return new TaskResult<>(city);
        } catch (IOException e) {
            return new TaskResult<BookmarkedCity>(e);
        }
    }

    private String getCityName(final Coord location, final Context context) throws IOException {
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
}