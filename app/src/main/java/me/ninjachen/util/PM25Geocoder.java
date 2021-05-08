package me.ninjachen.util;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import me.ninjachen.API;

public final class PM25Geocoder {
    private static final String LogTag = "pm25";
    private Location lastKnownLocation;
    private Context mContext;
    private LocationListener mListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            Log.d(LogTag,
                    String.format("onLocationChanged : location=%s ",
                            location.toString()));
        }

        public void onProviderDisabled(String disable) {
            Log.d(PM25Geocoder.LogTag, String.format(
                    "onProviderDisabled : provider=%s",
                    disable));
        }

        public void onProviderEnabled(String enabled) {
            Log.d(PM25Geocoder.LogTag, String
                    .format("onProviderEnabled : provider=%s",
                            enabled));
        }

        public void onStatusChanged(String paramAnonymousString,
                                    int paramAnonymousInt, Bundle paramAnonymousBundle) {
            Log.d(LogTag, String.format(
                    "onStatusChanged : provider=%s , status=%s , extras=%s",
                    paramAnonymousString,
                    paramAnonymousInt,
                    paramAnonymousBundle.toString()));
        }
    };
    private final LocationManager mLocationManager;

    public PM25Geocoder(Context paramContext) {
        this.mContext = paramContext;
        this.mLocationManager = ((LocationManager) this.mContext.getSystemService(Context.LOCATION_SERVICE));
    }

    public void check() {
        if (!mLocationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            Toast.makeText(this.mContext, "请在<系统设置>中启用Google位置服务以获得准确定位",
                    Toast.LENGTH_SHORT).show();
    }

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
                    } finally {
                        mLocationManager.removeUpdates(mListener);
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
