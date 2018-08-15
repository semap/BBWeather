package ca.aequilibrium.bbweather.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import ca.aequilibrium.bbweather.managers.ImageManager;
import ca.aequilibrium.bbweather.managers.ManagerContext;
import ca.aequilibrium.bbweather.managers.WeatherManager;
import ca.aequilibrium.bbweather.models.Coord;
import ca.aequilibrium.bbweather.models.CurrentWeather;
import ca.aequilibrium.bbweather.models.ForecastInfo;
import ca.aequilibrium.bbweather.models.Weather;
import ca.aequilibrium.bbweather.utils.DistinctUntilChangedObserver;
import ca.aequilibrium.bbweather.utils.Message;
import ca.aequilibrium.bbweather.utils.MessageType;
import ca.aequilibrium.bbweather.utils.ResultCallback;
import ca.aequilibrium.bbweather.utils.SharedPreferenceLiveData;
import ca.aequilibrium.bbweather.utils.SingleLiveEvent;
import ca.aequilibrium.bbweather.utils.TaskResult;

public class CityViewModel extends AndroidViewModel {
    private static final String TAG = CityViewModel.class.getSimpleName();
    private static final String USER_SETTINGS = "user_settings";
    private static final String IS_METRIC_SETTING_KEY = "is_metric";

    // The input properties, those properties will drive the output of this viewModel
    private LiveData<Boolean> isMetric;     // the input values are from SharedPreferenceLiveData
    private MutableLiveData<Coord> coord;          // the input values are from users

    // The output properties
    private MediatorLiveData<CurrentWeather> currentWeather;
    private MutableLiveData<ForecastInfo> forecastInfo;
    // Do NOT direct ues the Async in the Fragment, it is easy to cause memory leaks,
    // or you should use the WeakReference for the ImageView
    private MutableLiveData<Bitmap> weatherIcon;
    private SingleLiveEvent<Message> messageObservable;

    // Internal properties
    private WeatherManager weatherManager;
    private ImageManager imageManager;

    public CityViewModel(@NonNull final Application application) {
        super(application);
        this.weatherManager = ManagerContext.weatherManager;
        this.imageManager = ManagerContext.imageManager;
        this.messageObservable = new SingleLiveEvent<>();
        this.coord = new MutableLiveData<>();
        this.forecastInfo = new MutableLiveData<>();
        SharedPreferences sharedPreferences = application.getSharedPreferences(USER_SETTINGS, Context.MODE_PRIVATE);
        this.isMetric = SharedPreferenceLiveData.booleanLiveData(sharedPreferences, IS_METRIC_SETTING_KEY, true);
        this.currentWeather = combineLatestCoordAndIsMetric();
        this.weatherIcon = new MutableLiveData<>();
    }

    /**
     * Combine the latest from coord and isMetric. And invoke the getCurentWeather whenever their is a new element.
     *
     * @return
     */
    private MediatorLiveData<CurrentWeather> combineLatestCoordAndIsMetric() {
        currentWeather = new MediatorLiveData<>();
        currentWeather.addSource(isMetric, new DistinctUntilChangedObserver<Boolean>() {
            @Override
            public void onDistnctChanged(@Nullable Boolean aBoolean) {
                Log.d(TAG, "isMetric changed");
                CityViewModel.this.getCurentWeather();
            }
        });

        currentWeather.addSource(coord, new Observer<Coord>() {
            @Override
            public void onChanged(@Nullable Coord coord) {
                Log.d(TAG, "coord changed");
                CityViewModel.this.getCurentWeather();
            }
        });
        return currentWeather;
    }

    public LiveData<CurrentWeather> getCurrentWeatherObservable() {
        return currentWeather;
    }

    public LiveData<Bitmap> getWeatherIconObservable() {
        return this.weatherIcon;
    }

    public SingleLiveEvent<Message> getMessageObservable() {
        return messageObservable;
    }

    public void setCoord(final Coord coord) {
        this.coord.setValue(coord);
    }

    private void getCurentWeather() {
        Coord coordParameter = coord.getValue();
        Boolean isMetricParameter = isMetric.getValue();

        if (coordParameter != null && isMetricParameter != null) {
            Log.d(TAG, "update current weather");

            // get the current weather
            this.weatherManager.getCurrentWeather(coordParameter, isMetricParameter, new ResultCallback<CurrentWeather>() {
                @Override
                public void callback(TaskResult<CurrentWeather> taskResult) {
                    if (taskResult.error != null) {
                        Log.i(TAG, "Failed to call the weather API");
                        messageObservable.setValue(new Message("Unable to get current weather.", MessageType.ERROR));
                    } else {
                        currentWeather.setValue(taskResult.result);
                        getWeatherIcon(taskResult.result.getWeather());
                    }
                }
            });

            // get the forecasts
            this.weatherManager.getForecasts(coordParameter, isMetricParameter, new ResultCallback<ForecastInfo>() {
                @Override
                public void callback(TaskResult<ForecastInfo> taskResult) {
                    if (taskResult.error != null) {
                        Log.i(TAG, "Failed to call the forecast API");
                        messageObservable.setValue(new Message("Unable to get the forecasts.", MessageType.ERROR));
                    } else {
                        forecastInfo.setValue(taskResult.result);
                    }
                }
            });
        }
    }

    private void getWeatherIcon(List<Weather> weatherList) {
        if (weatherList == null || weatherList.size() < 1) {
            messageObservable.setValue(new Message("No weather icon information.", MessageType.INFO));
            return;
        }
        String name = weatherList.get(0).getIcon();

        this.imageManager.getWeatherImage(name, new ResultCallback<Bitmap>() {
            @Override
            public void callback(@NonNull TaskResult<Bitmap> taskResult) {
                if (taskResult.error != null) {
                    Log.e(TAG, "Unable to get the weather icon.", taskResult.error);
                    messageObservable.setValue(new Message("Unable to get the weather icon.", MessageType.ERROR));
                } else {
                    weatherIcon.setValue(taskResult.result);
                }
            }
        });
    }
}
