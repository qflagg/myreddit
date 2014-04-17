package com.qflagg.myreddit;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListView;

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

	private BaseInflaterAdapter<Post> initialize() {
		
		posts.clear();
		posts.addAll(postsHolder.fetchPosts());

		BaseInflaterAdapter<Post> postAdapter = new BaseInflaterAdapter<Post>(new PostInflater());
		for (int i = 0; i < posts.size(); i++) {
			Post data = posts.get(i);
			postAdapter.addItem(data, false);
		}

		return postAdapter;
	}
}