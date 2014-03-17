package com.qflagg.myreddit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.util.Log;

//serves as a utility class that handles network connections
public class RemoteData {
	
	//Returns a connection to the specified URL
	public static HttpURLConnection getConnection(String url){
		System.out.println("URL: " + url);
		HttpURLConnection connection = null;
		
		try {
			connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setConnectTimeout(30000); //timeout at 30 seconds
			connection.setRequestProperty("User-Agent", "myreddit V1.0");
		} catch (MalformedURLException e) {
			Log.e("getConnection()", "Invalid URL: " + e.toString());
		} catch (IOException e) {
			Log.e("getConnection()", "Could not connect: " + e.toString());
		}
		
		return connection;
	}
	
	//Reads the contents of a URL and returns them as a String
	public static String readContents(String url) {
		HttpURLConnection connection = getConnection(url);
		if(connection == null) return null;
		try {
			StringBuffer buffer = new StringBuffer(8192);
			String temp = "";
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(
							connection.getInputStream()
					));
			while((temp = reader.readLine()) != null) 
				buffer.append(temp).append("\n");
			reader.close();
			return buffer.toString();
		} catch(IOException e) {
			Log.d("READ FAILED", e.toString());
			return null;
		}
	}

}
