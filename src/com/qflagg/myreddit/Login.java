package com.qflagg.myreddit;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

public class Login {
	private final String REDDIT_LOGIN_URL = "https://ssl.reddit.com/api/login";

	private String redditCookie = "";

	private HttpURLConnection getConnection(String url) {
		URL u = null;
		try {
			u = new URL(url);
		} catch (MalformedURLException e) {
			Log.d("Invalid URL", url);
			return null;
		}
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) u.openConnection();
		} catch (IOException e) {
			Log.d("Unable to connect", url);
			return null;
		}
		// Timeout after 30 seconds
		connection.setReadTimeout(30000);
		// Allow POST data
		connection.setDoOutput(true);
		return connection;
	}

	// This method lets you POST data to the URL.
	private boolean writeToConnection(HttpURLConnection con, String data) {
		try {
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(
					con.getOutputStream()));
			pw.write(data);
			pw.close();
			return true;
		} catch (IOException e) {
			Log.d("Unable to write", e.toString());
			return false;
		}
	}

	// This method lets you log in to Reddit.
	// It fetches the cookie which can be used in subsequent calls
	// to the Reddit API.
	boolean login(String username, String password) {
		HttpURLConnection connection = getConnection(REDDIT_LOGIN_URL);

		if (connection == null)
			return false;

		// Parameters that the API needs
		String data = "user=" + username + "&passwd=" + password;

		if (!writeToConnection(connection, data))
			return false;

		String cookie = connection.getHeaderField("set-cookie");

		if (cookie == null)
			return false;

		cookie = cookie.split(";")[0];
		if (cookie.startsWith("reddit_first")) {
			// Login failed
			Log.d("Error", "Unable to login.");
			return false;
		} else if (cookie.startsWith("reddit_session")) {
			// Login success
			Log.d("Success", cookie);
			redditCookie = cookie;
			return true;
		}
		return false;
	}
}
