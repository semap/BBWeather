package ca.aequilibrium.bbweather.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ca.aequilibrium.bbweather.R;
import ca.aequilibrium.bbweather.models.Forecast;

public class ForecastsAdapter extends RecyclerView.Adapter<ForecastViewHolder> {

    private List<Forecast> forecasts = new ArrayList<>();
    private boolean isMetric = true;

    @Override
    public ForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ForecastViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.view_forecast, parent, false));
    }

    @Override
    public void onBindViewHolder(ForecastViewHolder holder, int position) {
        Forecast forecast = forecasts.get(position);
        Context context = holder.itemView.getContext();
        long timestampMillis = forecast.getDt() * 1000;
        String day = DateUtils.formatDateTime(context, timestampMillis, DateUtils.FORMAT_SHOW_WEEKDAY);
        String time = DateUtils.formatDateTime(context, timestampMillis, DateUtils.FORMAT_SHOW_TIME);
        holder.dayText.setText(day);
        holder.timeText.setText(time);
        if (isMetric) {
            holder.tempText
                    .setText(context.getString(R.string.metric_temp, String.valueOf(forecast.getMain().getTemp())));
        } else {
            holder.tempText
                    .setText(context.getString(R.string.imperial_temp, String.valueOf(forecast.getMain().getTemp())));
        }
        if (!forecast.getWeather().isEmpty()) {
            holder.weatherText.setText(forecast.getWeather().get(0).getMain());
        } else {
            holder.weatherText.setText(null);
        }

        if (isMetric) {
            holder.windText.setText(context.getString(R.string.metric_speed, String.valueOf(forecast.getWind().getSpeed())));
        } else {
            holder.windText.setText(context.getString(R.string.imperial_speed, String.valueOf(forecast.getWind().getSpeed())));
        }

        holder.humidityText.setText(context.getString(R.string.percentage, String.valueOf(forecast.getMain().getHumidity())));
    }

    @Override
    public int getItemCount() {
        return forecasts.size();
    }

    public void setForecasts(final List<Forecast> list) {
        forecasts.clear();
        forecasts.addAll(list);
        notifyDataSetChanged();
    }

    public void setMetric(boolean metric) {
        isMetric = metric;
    }
}
