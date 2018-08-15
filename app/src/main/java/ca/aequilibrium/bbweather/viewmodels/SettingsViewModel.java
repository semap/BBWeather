package ca.aequilibrium.bbweather.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import ca.aequilibrium.bbweather.managers.BookmarkedLocationManager;
import ca.aequilibrium.bbweather.managers.ManagerContext;
import ca.aequilibrium.bbweather.utils.Message;
import ca.aequilibrium.bbweather.utils.MessageType;
import ca.aequilibrium.bbweather.utils.ResultCallback;
import ca.aequilibrium.bbweather.utils.SharedPreferenceLiveData;
import ca.aequilibrium.bbweather.utils.SingleLiveEvent;
import ca.aequilibrium.bbweather.utils.TaskResult;

import static java.security.AccessController.getContext;

public class SettingsViewModel extends AndroidViewModel {
    private static final String TAG = SettingsViewModel.class.getSimpleName();
    private BookmarkedLocationManager bookmarkedLocationManager;
    private SingleLiveEvent<Message> messageObservable;

    public static final String USER_SETTINGS = "user_settings";
    public static final String IS_METRIC_SETTING_KEY = "is_metric";

    private SharedPreferenceLiveData<Boolean> isMetric;     // the input values are from SharedPreferenceLiveData

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        this.bookmarkedLocationManager = ManagerContext.bookmarkedLocationManager;
        this.messageObservable = new SingleLiveEvent<>();
        SharedPreferences sharedPreferences = application.getSharedPreferences(USER_SETTINGS, Context.MODE_PRIVATE);
        this.isMetric = SharedPreferenceLiveData.booleanLiveData(sharedPreferences, IS_METRIC_SETTING_KEY, true);
    }

    public LiveData<Boolean> getIsMetricObservable() {
        return isMetric;
    }

    public SingleLiveEvent<Message> getMessageObservable() {
        return messageObservable;
    }


    public void setIsMetric(@NonNull Boolean isMetric) {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(USER_SETTINGS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_METRIC_SETTING_KEY, isMetric.booleanValue());
        editor.commit();
    }

    public void removeAllBookmarkedCities() {
        bookmarkedLocationManager.removeAll(new ResultCallback<Integer>() {
            @Override
            public void callback(@NonNull TaskResult<Integer> taskResult) {
                if (taskResult.error != null) {
                    messageObservable.setValue(new Message("Unable to removed all bookmarked cities", MessageType.ERROR));
                } else {
                    messageObservable.setValue(new Message("Done. Deleted count: " + taskResult.result, MessageType.INFO));
                }
            }
        });
    }
}
