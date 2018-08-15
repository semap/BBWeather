package ca.aequilibrium.bbweather.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ca.aequilibrium.bbweather.R;

public class ForecastViewHolder extends RecyclerView.ViewHolder {
    TextView dayText;
    TextView humidityText;
    TextView tempText;
    TextView timeText;
    TextView weatherText;
    TextView windText;

    public ForecastViewHolder(View itemView) {
        super(itemView);
        dayText = itemView.findViewById(R.id.day_text);
        timeText = itemView.findViewById(R.id.time_text);
        tempText = itemView.findViewById(R.id.main_temp_text);
        weatherText = itemView.findViewById(R.id.main_text);
        humidityText = itemView.findViewById(R.id.humidity_percentage_text);
        windText = itemView.findViewById(R.id.wind_text);
    }

}
