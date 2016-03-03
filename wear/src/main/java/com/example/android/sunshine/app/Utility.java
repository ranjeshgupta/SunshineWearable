package com.example.android.sunshine.app;

/**
 * Created by Nish on 02-03-2016.
 */
public class Utility {
    public static String getIconResourceForWeatherCondition(int weatherId) {
        // Based on weather code data found at:
        // http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes
        if (weatherId >= 200 && weatherId <= 232) {
            return "ic_storm";
        } else if (weatherId >= 300 && weatherId <= 321) {
            return "ic_light_rain";
        } else if (weatherId >= 500 && weatherId <= 504) {
            return "ic_rain";
        } else if (weatherId == 511) {
            return "ic_snow";
        } else if (weatherId >= 520 && weatherId <= 531) {
            return "ic_rain";
        } else if (weatherId >= 600 && weatherId <= 622) {
            return "ic_snow";
        } else if (weatherId >= 701 && weatherId <= 761) {
            return "ic_fog";
        } else if (weatherId == 761 || weatherId == 781) {
            return "ic_storm";
        } else if (weatherId == 800) {
            return "ic_clear";
        } else if (weatherId == 801) {
            return "ic_light_clouds";
        } else if (weatherId >= 802 && weatherId <= 804) {
            return "ic_cloudy";
        }
        return null;
    }
}
