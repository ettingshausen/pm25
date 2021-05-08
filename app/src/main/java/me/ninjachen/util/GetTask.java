package me.ninjachen.util;

import android.os.AsyncTask;

public class GetTask extends AsyncTask<String, String, String> {
	private String mTargetURI;

	public GetTask(String uri) {
		this.mTargetURI = uri;
	}

	@Override
	protected String doInBackground(String[] params) {
		APIClient apiClient = new APIClient();
		return apiClient.request(String.format(mTargetURI, params[0]));
	}
}