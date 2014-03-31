package com.qflagg.myreddit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.util.Log;

//serves as a utility class that handles network connections
public class RemoteData {

	// Returns a connection to the specified URL
	static HttpURLConnection getConnection(String url) {
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

	// Reads the contents of a URL and returns them as a String
	public static String readContents(String url) {
		HttpURLConnection connection = getConnection(url);
		if (connection == null)
			return null;
		try {
			StringBuffer buffer = new StringBuffer(8192);
			String temp = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			while ((temp = reader.readLine()) != null)
				buffer.append(temp).append("\n");
			reader.close();
			return buffer.toString();
		} catch (IOException e) {
			Log.d("READ FAILED", e.toString());
			return null;
		}
	}
	
	static boolean writeToConnection(HttpURLConnection con, String data) {
		try {
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(con.getOutputStream()));
			pw.write(data);
			pw.close();
			return true;
		} catch (IOException e) {
			Log.d("Unable to write", e.toString());
			return false;
		}
	}
}