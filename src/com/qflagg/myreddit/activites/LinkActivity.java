package com.qflagg.myreddit.activites;

import java.io.File;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.qflagg.myreddit.R;

public class LinkActivity extends ActionBarActivity {
	private ActionBar actionBar; // action bar
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_link);
		
		actionBar = getActionBar();
		
		WebView wv = (WebView)findViewById(R.id.webView);
		wv.setWebViewClient(new WebViewClient());
		wv.getSettings().setBuiltInZoomControls(true);
		wv.getSettings().setDisplayZoomControls(false);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(true);
		wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		
		Intent intent = getIntent();
		String url = intent.getStringExtra("url");
		String title = intent.getStringExtra("title");
		
		if(title.length() > 20) {
			title = shortenTitle(title);
		}
		actionBar.setTitle(title);
		
		String location = getFileStreamPath(getFilenameForUrl(url)).getAbsolutePath();
		File file = new File(location);
		if(file.exists()){
			wv.setBackgroundColor(Color.BLACK); 
			wv.loadUrl("file:///" + location);
		} else {
			wv.loadUrl(url);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_link_actions, menu);
		return true;
	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
	
	private static String getFilenameForUrl(final String url) {
        return url.hashCode() + ".urlimage";
    }
	
	private String shortenTitle(String title) {
		String temp = "";
		for(int i = 0; i < 20; i++){
			temp += title.charAt(i);
		}
		temp += "...";
		return temp;
	}


}
