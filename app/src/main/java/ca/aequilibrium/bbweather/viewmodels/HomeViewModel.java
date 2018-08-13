package ca.aequilibrium.bbweather.viewmodels;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import ca.aequilibrium.bbweather.models.BookmarkedCity;
import ca.aequilibrium.bbweather.services.BookmarkedLocationService;
import ca.aequilibrium.bbweather.services.ServiceContext;
import ca.aequilibrium.bbweather.utils.SingleLiveEvent;
import ca.aequilibrium.bbweather.utils.TaskResult;

public class HomeViewModel extends AndroidViewModel {

    private SingleLiveEvent<Void> loadBookmarkedLocations = new SingleLiveEvent<>();
    private LiveData<List<BookmarkedCity>> bookmarkedCityObservable;
    private MutableLiveData<String> errorObservable;

    private BookmarkedLocationService bookmarkedLocationService;

    public HomeViewModel(@NonNull final Application application) {
        super(application);
        this.bookmarkedLocationService = ServiceContext.getBookmarkedCityManager();

        bookmarkedCityObservable = Transformations.switchMap(loadBookmarkedLocations, new Function<Void, LiveData<List<BookmarkedCity>>>() {
            @Override
            public LiveData<List<BookmarkedCity>> apply(Void input) {
                LiveData<TaskResult<List<BookmarkedCity>>> results = bookmarkedLocationService.getAll();
                return Transformations.map(results, new Function<TaskResult<List<BookmarkedCity>>, List<BookmarkedCity>>() {
                    @Override
                    public List<BookmarkedCity> apply(TaskResult<List<BookmarkedCity>> input) {
                        if (input.error == null) {
                            return input.result;
                        }
                        errorObservable.setValue("error");
                        return new ArrayList<>();
                    }
                });
            }
        });
    }

    public LiveData<List<BookmarkedCity>> getBookmarkedCityObservable() {
        return bookmarkedCityObservable;
    }

    public void loadBookmarkedLocations() {
        loadBookmarkedLocations.call();
    }

}
