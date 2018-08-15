package ca.aequilibrium.bbweather.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import ca.aequilibrium.bbweather.models.BookmarkedCity;
import ca.aequilibrium.bbweather.models.Coord;
import ca.aequilibrium.bbweather.managers.BookmarkedLocationManager;
import ca.aequilibrium.bbweather.utils.Message;
import ca.aequilibrium.bbweather.utils.MessageType;
import ca.aequilibrium.bbweather.utils.ResultCallback;
import ca.aequilibrium.bbweather.managers.ManagerContext;
import ca.aequilibrium.bbweather.utils.SingleLiveEvent;
import ca.aequilibrium.bbweather.utils.TaskResult;

public class HomeViewModel extends AndroidViewModel {

    private LiveData<List<BookmarkedCity>> bookmarkedCityObservable;
    private SingleLiveEvent<Message> messageObservable;

    private BookmarkedLocationManager bookmarkedLocationManager;

    public HomeViewModel(@NonNull final Application application) {
        super(application);
        this.bookmarkedLocationManager = ManagerContext.bookmarkedLocationManager;
        this.messageObservable = new SingleLiveEvent<>();
    }

    public LiveData<List<BookmarkedCity>> getBookmarkedCityObservable() {
        return bookmarkedLocationManager.getBookmarkedCities();
    }

    public void addBookmarkedLocationByCoord(Coord coord) {
        this.bookmarkedLocationManager.addBookmarkedCityByLocation(coord, new ResultCallback<BookmarkedCity>() {
            @Override
            public void callback(TaskResult<BookmarkedCity> taskResult) {
                if (taskResult.error != null) {
                    messageObservable.setValue(new Message("Failed to add the bookmark", MessageType.ERROR));
                } else {
                    messageObservable.setValue(new Message("Bookmark created", MessageType.INFO));
                }
            }
        });
    }

    public SingleLiveEvent<Message> getMessageObservable() {
        return messageObservable;
    }
}
