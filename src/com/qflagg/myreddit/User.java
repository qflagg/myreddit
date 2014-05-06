package com.qflagg.myreddit;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class User {
	String redditCookie;
	List<String> subscribedSubreddits;
	RemoteData remoteData;

	public User(String redditCookie) {
		this.redditCookie = redditCookie;
		remoteData = new RemoteData();
		remoteData.setCookie(this.redditCookie);
	}

	public List<String> fetchSubscribedSubreddts() {
		String after = "";
		String URL_TEMPLATE = "http://www.reddit.com/reddits/mine/.json";
		String url = URL_TEMPLATE + "?after=" + after;
		subscribedSubreddits = new ArrayList<String>();

		while (!after.equals("null")){
			String raw = remoteData.readContents(url);
			try {
				JSONObject data = new JSONObject(raw).getJSONObject("data");
				JSONArray children = data.getJSONArray("children");

				after = data.getString("after");

				for (int i = 0; i < children.length(); i++) {
					JSONObject cur = children.getJSONObject(i).getJSONObject("data");
					subscribedSubreddits.add(cur.optString("display_name"));
				}
			} catch (Exception e) {
				Log.e("fetchPosts()", e.toString());
			}
			url = URL_TEMPLATE + "?after=" + after;
			Log.d("AFTER", after);
		}
		java.util.Collections.sort(subscribedSubreddits);
		return subscribedSubreddits;
	}

	public List<String> getSubscribedSubreddits() {
		return subscribedSubreddits;
	}
	
	public void setRedditCookie(String redditCookie) {
		this.redditCookie = redditCookie;
		remoteData.setCookie(redditCookie);
	}
}
