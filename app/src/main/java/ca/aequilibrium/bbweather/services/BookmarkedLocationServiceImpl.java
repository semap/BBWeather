package ca.aequilibrium.bbweather.services;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import ca.aequilibrium.bbweather.models.BookmarkedCity;
import ca.aequilibrium.bbweather.utils.TaskResult;

public class BookmarkedLocationServiceImpl implements BookmarkedLocationService {


    @Override
    public LiveData<TaskResult<BookmarkedCity>> add(BookmarkedCity bookmarkedCity) {
        return null;
    }

    @Override
    public LiveData<TaskResult<Boolean>> remove(BookmarkedCity bookmarkedCity) {
        return null;
    }

    @Override
    public LiveData<TaskResult<List<BookmarkedCity>>> getAll() {

        List<BookmarkedCity> mockBookmarkedCities = new ArrayList<>();
        BookmarkedCity vancouver = new BookmarkedCity();
        vancouver.setName("Vancouver");
        mockBookmarkedCities.add(vancouver);

        MutableLiveData<TaskResult<List<BookmarkedCity>>> liveData = new MutableLiveData<>();


        liveData.postValue(new TaskResult<>(mockBookmarkedCities));
        return liveData;
    }
}
