package me.ninjachen.util;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import me.ninjachen.API;

public final class PM25Geocoder {
    private static final String LogTag = "pm25";

    public void requestLocalCityName(final CityNameStatus cityNameStatus) {
        GetTask task = new GetTask(API.ipAPI) {
            @Override
            protected void onPostExecute(String result) {
                String city = null;
                Log.d(LogTag, result);
                if (result != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        city = jsonObject.getString("city");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                cityNameStatus.update(city);
            }

            @Override
            protected void onPreExecute() {
                cityNameStatus.detecting();
            }
        };

        task.execute("");
    }

    public interface CityNameStatus {
        void detecting();

        void update(String paramString);
    }
}
