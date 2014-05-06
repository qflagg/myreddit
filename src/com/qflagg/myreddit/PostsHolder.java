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
    
    /**
     * We will be fetching JSON data from the API.
     */
    private final String URL_TEMPLATE=
                "http://www.reddit.com/r/SUBREDDIT_NAME/"
               +".json"
               +"?after=AFTER";
     
    String subreddit;
    String url;
    String after;
    String redditCookie;
    RemoteData remoteData;
     
    public PostsHolder(String sr, String rc){
        subreddit=sr;    
        after="";
        
        remoteData = new RemoteData();
        remoteData.setCookie(rc);
        generateURL();
    }
     
    /**
     * Generates the actual URL from the template based on the
     * subreddit name and the 'after' property.
     */
    private void generateURL(){
    	if(subreddit.length() > 0){
    		url=URL_TEMPLATE.replace("SUBREDDIT_NAME", subreddit);
    	} else {
    		url=URL_TEMPLATE.replace("r/SUBREDDIT_NAME/", subreddit);
    	}
    	url=url.replace("AFTER", after);
    }
     
    /**
     * Returns a list of Post objects after fetching data from
     * Reddit using the JSON API.
     * 
     * @return
     */
    public List<Post> fetchPosts(){
        String raw=remoteData.readContents(url);
        List<Post> list=new ArrayList<Post>();
        try{
            JSONObject data=new JSONObject(raw)
                                .getJSONObject("data");
            JSONArray children=data.getJSONArray("children");
             
            //Using this property we can fetch the next set of
            //posts from the same subreddit
            after=data.getString("after");
             
            for(int i=0;i<children.length();i++){
                JSONObject cur=children.getJSONObject(i)
                                    .getJSONObject("data");
                Post p=new Post();
                p.title=cur.optString("title");
                p.numComments=cur.optInt("num_comments");
                p.karma=cur.optInt("score");
                p.author=cur.optString("author");
                p.subreddit=cur.optString("subreddit");
                p.permalink=cur.optString("permalink");
                p.domain=cur.optString("domain");
                p.id=cur.optString("id");
                p.up=cur.optInt("ups");
                p.down=cur.optInt("downs");
                p.subtext=cur.optString("selftext");
                if(p.title!=null)
                    list.add(p);
                
                p.url=cur.optString("url");
                String newUrl = p.getUrl();
                if(!newUrl.contains("i.i") && newUrl.contains("imgur") && !newUrl.contains("/a/")){
                	newUrl = newUrl.replace("http://", "");
                	p.setUrl("http://i." + newUrl + ".jpg");
                }
                
            }
        }catch(Exception e){
            Log.e("fetchPosts()",e.toString());
        }
        return list;
    }
     
    /**
     * This is to fetch the next set of posts
     * using the 'after' property
     * @return
     */
    List<Post> fetchMorePosts(){
        generateURL();
        return fetchPosts();
    }
    
    public void setSubreddit(String subreddit) {
    	this.subreddit = subreddit;
    	after="";
    	generateURL();
    }
    
    public void setRedditCookie(String redditCookie) {
    	this.redditCookie = redditCookie;
    	remoteData.setCookie(redditCookie);
    }
}