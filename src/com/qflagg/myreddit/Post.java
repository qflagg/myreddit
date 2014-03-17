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
	
	//returns formatted String containing details of the current post/thread
	String getDetails() {
		return author + "posted this and got " + numComments + " replies";
	}
	
	//returns title of the post/thread
	String getTitle() {
		return title;
	}
	
	//returns karma received from post/thread
	String getKarma() {
		return Integer.toString(karma);
	}
}
