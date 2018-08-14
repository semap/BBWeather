package ca.aequilibrium.bbweather.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import ca.aequilibrium.bbweather.models.Coord;
import ca.aequilibrium.bbweather.models.CurrentWeather;
import ca.aequilibrium.bbweather.services.ServiceContext;
import ca.aequilibrium.bbweather.services.WeatherService;
import ca.aequilibrium.bbweather.utils.Message;
import ca.aequilibrium.bbweather.utils.MessageType;
import ca.aequilibrium.bbweather.utils.ResultCallback;
import ca.aequilibrium.bbweather.utils.SingleLiveEvent;
import ca.aequilibrium.bbweather.utils.TaskResult;

public class CityViewModel extends AndroidViewModel {
    private MutableLiveData<CurrentWeather> currentWeatherObservable;
    private SingleLiveEvent<Message> messageObservable;

    private WeatherService weatherService;

    public CityViewModel(@NonNull final Application application) {
        super(application);
        this.weatherService = ServiceContext.weatherService;
        this.messageObservable = new SingleLiveEvent<>();
        this.currentWeatherObservable = new MutableLiveData<>();
    }

    public LiveData<CurrentWeather> getCurrentWeatherObservable() {
        return currentWeatherObservable;
    }

    public SingleLiveEvent<Message> getMessageObservable() {
        return messageObservable;
    }

    public void getCurentWeather(Coord coord) {
        this.weatherService.getCurrentWeather(coord, new ResultCallback<CurrentWeather>() {
            @Override
            public void callback(TaskResult<CurrentWeather> taskResult) {
                if (taskResult.error == null) {

                    messageObservable.setValue(new Message("Unable to get current weather.", MessageType.ERROR));
                } else {
                    currentWeatherObservable.setValue(taskResult.result);
                }
            }
        });
    }
}
