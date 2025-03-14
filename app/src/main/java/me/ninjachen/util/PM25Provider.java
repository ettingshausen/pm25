package me.ninjachen.util;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static me.ninjachen.API.PM25API;

public class PM25Provider {
	private static final String LogTag = "pm25";

	public void request(final PM25Info pm25Listener, String param) {
		GetTask task = new GetTask(PM25API) {
			protected void onPostExecute(String result) {
				ArrayList<PM25> pm25List = null;
				if (result != null && !result.contains("error")) {
					Log.d(PM25Provider.LogTag, result);
					try {
						JSONArray localJSONArray = new JSONArray(result);
						pm25List = new ArrayList<>();
						for (int i = 0; i < localJSONArray.length(); i++) {
							JSONObject localJSONObject = localJSONArray
									.getJSONObject(i);
							PM25Provider.PM25 pm25 = new PM25Provider.PM25();
							pm25.aqi = localJSONObject.optString("aqi");
							pm25.area = localJSONObject.optString("area");
							pm25.pm2_5 = localJSONObject.optString("pm2_5");
							pm25.pm10 = localJSONObject.optString("pm10");
							pm25.position_name = localJSONObject
									.optString("position_name");
							pm25.primary_pollutant = localJSONObject
									.optString("primary_pollutant");
							pm25.quality = localJSONObject.optString("quality");
							pm25.time_point = localJSONObject
									.optString("time_point");
							pm25List.add(pm25);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					pm25Listener.onInfo(pm25List);
				}
			}
		};
		task.execute(param.toLowerCase().replace("'", ""));
	}

	public static class PM25 {
		public String aqi;
		public String area;
		public String pm10;
		public String pm2_5;
		public String position_name;
		public String primary_pollutant;
		public String quality;
		public String time_point;

	}

	public interface PM25Info {
		void onInfo(List<PM25Provider.PM25> pm25List);
	}
}
