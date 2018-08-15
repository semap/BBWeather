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

/**
 * Async to retrieve the Coord (Lat/Lng) from a given name.
 */
public class GeocodeSearchNameAsyncTask extends CallbackAsyncTask<String, Void, Coord> {
    private WeakReference<Context> contextRef;

    public GeocodeSearchNameAsyncTask(Context context, ResultCallback<Coord> resultCallback) {
        super(resultCallback);
        this.contextRef = new WeakReference<>(context);
    }

    @Override
    protected TaskResult<Coord> doInBackground(String... searchTexts) {
        Context context = contextRef.get();
        if (searchTexts == null || searchTexts.length == 0 || context == null) {
            return new TaskResult<Coord>((Coord) null);
        }

        String searchText = searchTexts[0];
        try {
            Coord coord = getCoord(searchText, context);
            return new TaskResult<>(coord);
        } catch (IOException e) {
            return new TaskResult<Coord>(e);
        }
    }

    private Coord getCoord(final String searchText, final Context context) throws IOException {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        List<Address> addressList = geocoder.getFromLocationName(searchText, 1);
        if (addressList == null || addressList.isEmpty()) {
            return null;
        }
        Address address = addressList.get(0);
        return new Coord(address.getLatitude(), address.getLongitude());
    }
}