package com.qflagg.myreddit.activites;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qflagg.myreddit.Post;
import com.qflagg.myreddit.PostInflater;
import com.qflagg.myreddit.PostsHolder;
import com.qflagg.myreddit.R;
import com.qflagg.myreddit.actionbar.model.SpinnerNavItem;
import com.qflagg.myreddit.adapters.BaseInflaterAdapter;
import com.qflagg.myreddit.adapters.TitleNavigationAdapter;

public class MainActivity extends FragmentActivity implements
		ActionBar.OnNavigationListener {

	private ActionBar actionBar; // action bar
	private ArrayList<SpinnerNavItem> navSpinner; // title navigation Spinner
													// data
	private TitleNavigationAdapter adapter; // action bar adapter

	List<Post> posts = new ArrayList<Post>();
	ListView list;
	String redditCookie;
	PostsHolder postsHolder;

	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);
		redditCookie = getIntent().getStringExtra("redditCookie");
		postsHolder = new PostsHolder("", redditCookie);
		
		
		list = (ListView) findViewById(R.id.list_view);
		
		list.addHeaderView(new View(this));
		list.addFooterView(new View(this));

		actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false); // hide action bar title
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST); // enable dropdown navigation

		navSpinner = new ArrayList<SpinnerNavItem>();
		navSpinner.add(new SpinnerNavItem("Front Page"));
		navSpinner.add(new SpinnerNavItem("r/AbandonedPorn"));
		navSpinner.add(new SpinnerNavItem("r/AdviceAnimals"));
		navSpinner.add(new SpinnerNavItem("r/AskReddit"));
		navSpinner.add(new SpinnerNavItem("r/AskScience"));
		navSpinner.add(new SpinnerNavItem("r/Awesome"));
		navSpinner.add(new SpinnerNavItem("r/Aww"));
		navSpinner.add(new SpinnerNavItem("r/Bestof"));

		adapter = new TitleNavigationAdapter(getApplicationContext(),
				navSpinner);
		actionBar.setListNavigationCallbacks(adapter, this);
		
		PopulateCardsTask task = new PopulateCardsTask();
		task.execute();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main_actions, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		PopulateCardsTask task = new PopulateCardsTask();
		// Action to be taken after selecting a spinner item
		switch (itemPosition) {
		case 1:
			postsHolder.setSubreddit("AbandonedPorn");
			task.execute();
			break;
		case 2:
			postsHolder.setSubreddit("AdviceAnimals");
			task.execute();
			break;
		}
		return false;
	}
	
	public void pressedImage(View view) {
			RelativeLayout rl = (RelativeLayout) view.getParent();
			LinearLayout ll = (LinearLayout)rl.getParent();
			ll = (LinearLayout)ll.getParent();
			
			TextView title = (TextView)ll.findViewById(R.id.title);
			TextView url = (TextView)rl.findViewById(R.id.url);
			String urlString = (String) url.getText();
			String titleString = (String) title.getText();
			
			
			Intent intent = new Intent(this, LinkActivity.class);
			intent.putExtra("url", urlString);
			intent.putExtra("title", titleString);
			startActivity(intent);
	}
	
	public void pressedTitle(View view) {
		LinearLayout ll = (LinearLayout) view.getParent();
		ll = (LinearLayout) ll.getParent();
		TextView url = (TextView)ll.findViewById(R.id.url);
		TextView title = (TextView)ll.findViewById(R.id.title);
		
		String titleString = (String)title.getText();
		String urlString = (String) url.getText();
		
		Intent intent = new Intent(this, LinkActivity.class);
		intent.putExtra("url", urlString);
		intent.putExtra("title", titleString);
		startActivity(intent);
	}
	
	public void upVoteClicked(View view) {
		Context context = getApplicationContext();
		CharSequence text = "up vote";
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
	
	public void downVoteClicked(View view) {
		Context context = getApplicationContext();
		CharSequence text = "down vote";
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
	
	public void commentClicked(View view) {
		Context context = getApplicationContext();
		CharSequence text = "comment";
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
	
	private BaseInflaterAdapter<Post> initialize() {
		
		posts.clear();
		posts.addAll(postsHolder.fetchPosts());

		BaseInflaterAdapter<Post> postAdapter = new BaseInflaterAdapter<Post>(
				new PostInflater());
		for (int i = 0; i < posts.size(); i++) {
			Post data = posts.get(i);
			postAdapter.addItem(data, false);
		}

		return postAdapter;
	}
	
	
	private class PopulateCardsTask extends AsyncTask<Void, Void, BaseInflaterAdapter<Post>> {

		@Override
		protected BaseInflaterAdapter<Post> doInBackground(Void... params) {
			return initialize();
		}
		
	    @Override
	    protected void onPostExecute(BaseInflaterAdapter<Post> adapter) {
	    	list.setAdapter(null);
	    	list.setAdapter(adapter);
	    }
	  }
}