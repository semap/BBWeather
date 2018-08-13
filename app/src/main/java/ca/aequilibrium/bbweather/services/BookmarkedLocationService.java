package ca.aequilibrium.bbweather.services;

import android.arch.lifecycle.LiveData;

import java.util.List;

import ca.aequilibrium.bbweather.models.BookmarkedCity;
import ca.aequilibrium.bbweather.utils.TaskResult;

public interface BookmarkedLocationService {
    LiveData<TaskResult<BookmarkedCity>> add(BookmarkedCity bookmarkedCity);
    LiveData<TaskResult<Boolean>> remove(BookmarkedCity bookmarkedCity);
    LiveData<TaskResult<List<BookmarkedCity>>> getAll();
}
