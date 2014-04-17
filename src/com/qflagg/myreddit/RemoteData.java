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
	
	private String cookie;
	
	public RemoteData() {
		cookie = "";
	}

	// Returns a connection to the specified URL
	 public  HttpURLConnection getConnection(String url){
	        System.out.println("URL: "+url);
	        HttpURLConnection hcon = null;
	        try {            
	            hcon=(HttpURLConnection)new URL(url).openConnection();
	            if(cookie.length() > 0)
		        	hcon.setRequestProperty("Cookie", cookie);
	            hcon.setReadTimeout(30000); // Timeout at 30 seconds
	            hcon.setRequestProperty("User-Agent", "Alien V1.0");
	        } catch (MalformedURLException e) {
	            Log.e("getConnection()",
	                  "Invalid URL: "+e.toString());
	        } catch (IOException e) {
	            Log.e("getConnection()",
	                  "Could not connect: "+e.toString());
	        }
	        //hcon.setDoOutput(true);
	        return hcon;        
	    }
	 
	 public static HttpURLConnection getLoginConnection(String url){
	        System.out.println("URL: "+url);
	        HttpURLConnection hcon = null;
	        try {            
	            hcon=(HttpURLConnection)new URL(url).openConnection();
	            hcon.setReadTimeout(30000); // Timeout at 30 seconds
	            hcon.setRequestProperty("User-Agent", "Alien V1.0");
	        } catch (MalformedURLException e) {
	            Log.e("getConnection()",
	                  "Invalid URL: "+e.toString());
	        } catch (IOException e) {
	            Log.e("getConnection()",
	                  "Could not connect: "+e.toString());
	        }
	        hcon.setDoOutput(true);
	        return hcon;        
	    }
	     
	     
	    /**
	     * A very handy utility method that reads the contents of a URL
	     * and returns them as a String.
	     * 
	     * @param url
	     * @return
	     */
	    public String readContents(String url){        
	        
	    	HttpURLConnection hcon = null;
	        try {            
	            hcon=(HttpURLConnection)new URL(url).openConnection();
	            if(cookie.length() > 0)
		        	hcon.setRequestProperty("Cookie", cookie);
	            hcon.setReadTimeout(30000); // Timeout at 30 seconds
	            hcon.setRequestProperty("User-Agent", "Alien V1.0");
	        } catch (MalformedURLException e) {
	            Log.e("getConnection()",
	                  "Invalid URL: "+e.toString());
	        } catch (IOException e) {
	            Log.e("getConnection()",
	                  "Could not connect: "+e.toString());
	        }
	    	
	        if(hcon==null) return null;
	        try{
	            StringBuffer sb=new StringBuffer(8192);
	            String tmp="";
	            BufferedReader br=new BufferedReader(
	                                new InputStreamReader(
	                                        hcon.getInputStream()
	                                )
	                              );
	            while((tmp=br.readLine())!=null)
	                sb.append(tmp).append("\n");
	            br.close();                        
	            return sb.toString();
	        }catch(IOException e){
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
	
	public void setCookie(String cookie) {
		this.cookie = cookie;
	}
}