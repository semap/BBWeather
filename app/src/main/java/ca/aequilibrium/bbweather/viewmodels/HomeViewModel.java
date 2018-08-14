package ca.aequilibrium.bbweather.viewmodels;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ca.aequilibrium.bbweather.models.BookmarkedCity;
import ca.aequilibrium.bbweather.models.Coord;
import ca.aequilibrium.bbweather.services.BookmarkedLocationService;
import ca.aequilibrium.bbweather.services.ResultCallback;
import ca.aequilibrium.bbweather.services.ServiceContext;
import ca.aequilibrium.bbweather.utils.SingleLiveEvent;
import ca.aequilibrium.bbweather.utils.TaskResult;

public class HomeViewModel extends AndroidViewModel {

    private LiveData<List<BookmarkedCity>> bookmarkedCityObservable;
    private MutableLiveData<String> errorObservable;

    private BookmarkedLocationService bookmarkedLocationService;

    public HomeViewModel(@NonNull final Application application) {
        super(application);
        this.bookmarkedLocationService = ServiceContext.getBookmarkedLocationService();
        this.errorObservable = new SingleLiveEvent<String>();
    }

    public LiveData<List<BookmarkedCity>> getBookmarkedCityObservable() {
        return bookmarkedLocationService.getBookmarkedCities();
    }

    public void addBookmarkedLocationByCoord(Coord coord) {
        this.bookmarkedLocationService.addBookmarkedCityByLocation(coord, new ResultCallback<BookmarkedCity>() {
            @Override
            public void callback(TaskResult<BookmarkedCity> taskResult) {
                if (taskResult.error != null) {
                    errorObservable.setValue("Failed to add the bookmark");
                }
            }
        });
    }


}
