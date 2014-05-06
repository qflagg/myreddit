package com.qflagg.myreddit;

//holds the data of the JSON objects returned by the Reddit API
public class Post {
	String subreddit;
	String title;
	String author;
	int karma;
	int numComments;
	String permalink;
	String url;
	String domain;
	String id;
	int up;
	int down;
	String subtext;
	
	//returns title of the post/thread
	String getTitle() {
		return title;
	}
	
	public int getUp(){
		return up;
	}
	
	public int getDown() {
		return down;
	}
	
	//returns karma received from post/thread
	public String getKarma() {
		return Integer.toString(karma);
	}

	public String getSubreddit() {
		return subreddit;
	}

	public String getAuthor() {
		return author;
	}


	public int getNumComments() {
		return numComments;
	}

	public String getPermalink() {
		return permalink;
	}


	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}

	public String getDomain() {
		return domain;
	}

	public String getId() {
		return id;
	}
	
	public String getSubtext() {
		return subtext;
	}
}
