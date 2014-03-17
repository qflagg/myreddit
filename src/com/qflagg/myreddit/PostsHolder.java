package com.qflagg.myreddit;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

/**
 *creates Post objects out of the Reddit API and stores them in a list for other 
 *classes to make use of
 */
public class PostsHolder {
	//fetching JSON data from the API
	private final String URL_TEMPLATE = "http://www.reddit.com/r/SUBREDDIT_NAME/.json?after=AFTER";
	
	String subreddit;
	String url;
	String after;
	
	PostsHolder(String subreddit) {
		this.subreddit = subreddit;
		after = "";
		generateURL();
	}
	
	//generates the correct URL for specified subreddit
	private void generateURL() {
		url = URL_TEMPLATE.replace("SUBREDDIT_NAME", subreddit);
		url = url.replace("AFTER", after);
	}
	
	//returns list of Post objects after fetching data from Reddit
	List<Post> fetchPosts() {
		String rawData = RemoteData.readContents(url);
		List<Post> list = new ArrayList<Post>();
		try {
			JSONObject data = new JSONObject(rawData).getJSONObject("data");
			JSONArray children = data.getJSONArray("children");
			
			//fetch the next set of posts from the same subreddit
			after = data.getString("after");
			
			//goes through the posts JSON array and populates the List of Posts with the correct info
			for(int i = 0; i < children.length(); ++i) {
				JSONObject current = children.getJSONObject(i).getJSONObject("data");
				Post post = new Post();
				post.title = (String) current.opt("title");
				post.url = (String)current.optString("url");
				post.numComments = (int)current.optInt("num_comments");
				post.karma = (int)current.optInt("score");
				post.author = (String)current.optString("author");
				post.subreddit = (String)current.optString("subreddit");
				post.permalink = (String)current.optString("permalink");
				post.domain = (String)current.optString("domain");
				post.id = (String)current.optString("id");
				if(post.title != null)
					list.add(post);
			}
		} catch(Exception e) {
			Log.e("fetchPosts()", e.toString());
		}
		
		return list;
	}
	
	//used to fetch the posts, calls fetchPosts()
	List<Post> fetchMorePosts() {
		generateURL();
		return fetchPosts();
	}
}
