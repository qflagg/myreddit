package com.qflagg.myreddit.activites;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qflagg.myreddit.Post;
import com.qflagg.myreddit.PostsHolder;
import com.qflagg.myreddit.R;
import com.qflagg.myreddit.RemoteData;
import com.qflagg.myreddit.User;
import com.qflagg.myreddit.actionbar.model.SpinnerNavItem;
import com.qflagg.myreddit.adapters.TitleNavigationAdapter;

public class MainActivity extends FragmentActivity implements
		ActionBar.OnNavigationListener {

	private final String REDDIT_LOGIN_URL = "https://ssl.reddit.com/api/login";

	private ActionBar actionBar; // action bar
	private ArrayList<SpinnerNavItem> navSpinner; // title navigation Spinner
	private TitleNavigationAdapter listAdapter; // action bar adapter

	List<Post> posts = new ArrayList<Post>();
	String[] mDrawerListContent;
	ListView postsList;
	String redditCookie = "";
	PostsHolder postsHolder;
	User user;

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		postsHolder = new PostsHolder("", redditCookie);
		user = new User(redditCookie);

		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false); // hide action bar title
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		mTitle = mDrawerTitle = getTitle();

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener

		if (!redditCookie.equals("")) {
			GenerateNavDrawerTask task = new GenerateNavDrawerTask();
			task.execute();
		} else {
			mDrawerListContent = generateDefaultDrawerListContent();

			mDrawerList.setAdapter(new ArrayAdapter<String>(
					getApplicationContext(), R.layout.drawer_list_item,
					mDrawerListContent));
		}
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		navSpinner = new ArrayList<SpinnerNavItem>();
		navSpinner.add(new SpinnerNavItem("Front Page"));
		navSpinner.add(new SpinnerNavItem("r/AbandonedPorn"));
		navSpinner.add(new SpinnerNavItem("r/AdviceAnimals"));
		navSpinner.add(new SpinnerNavItem("r/AskReddit"));
		navSpinner.add(new SpinnerNavItem("r/AskScience"));
		navSpinner.add(new SpinnerNavItem("r/Awesome"));
		navSpinner.add(new SpinnerNavItem("r/Aww"));
		navSpinner.add(new SpinnerNavItem("r/Bestof"));

		listAdapter = new TitleNavigationAdapter(getApplicationContext(),
				navSpinner);
		actionBar.setListNavigationCallbacks(listAdapter, this);

		if (savedInstanceState == null) {
			selectItem(2);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main_actions, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// PopulateCardsTask task = new PopulateCardsTask();
		//
		// switch (itemPosition) {
		// case 1:
		// postsHolder.setSubreddit("AbandonedPorn");
		// task.execute();
		// break;
		// case 2:
		// postsHolder.setSubreddit("AdviceAnimals");
		// task.execute();
		// break;
		// }
		return false;
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	// onClick Listeners start
	public void pressedImage(View view) {
		RelativeLayout rl = (RelativeLayout) view.getParent();
		LinearLayout ll = (LinearLayout) rl.getParent();
		ll = (LinearLayout) ll.getParent();

		TextView title = (TextView) ll.findViewById(R.id.title);
		TextView url = (TextView) rl.findViewById(R.id.url);
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
		TextView url = (TextView) ll.findViewById(R.id.url);
		TextView title = (TextView) ll.findViewById(R.id.title);

		String titleString = (String) title.getText();
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

	// onClick Listeners end

	private static String[] generateDefaultDrawerListContent() {
		String[] content = new String[25];

		content[0] = "Login";
		content[1] = "";
		content[2] = "Front Page";
		content[3] = "AdviceAnimals";
		content[4] = "AskReddit";
		content[5] = "Aww";
		content[6] = "BestOf";
		content[7] = "Books";
		content[8] = "EarthPorn";
		content[9] = "ExplainLikeImFive";
		content[10] = "Funny";
		content[11] = "Gaming";
		content[12] = "Gifs";
		content[13] = "IAmA";
		content[14] = "Movies";
		content[15] = "Music";
		content[16] = "News";
		content[17] = "Pics";
		content[18] = "Science";
		content[19] = "Technology";
		content[20] = "Television";
		content[21] = "TodayILearned";
		content[22] = "Videos";
		content[23] = "WorldNews";
		content[24] = "WTF";

		return content;
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		Fragment fragment = null;
		// update the main content by replacing fragments
		switch (position) {
		case 0:
			if (mDrawerListContent[position].equals("Login")) {
				fragment = new LoginFragment();
			}
			break;
		case 2:
			postsHolder.setSubreddit("");
			fragment = PostsFragment.newInstance(postsHolder);
			break;
		default:
			postsHolder.setSubreddit(mDrawerListContent[position]);
			fragment = PostsFragment.newInstance(postsHolder);
			break;
		}
		Bundle args = new Bundle();
		fragment.setArguments(args);

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		// update selected item and title, then close the drawer
		// mDrawerList.setItemChecked(position, true);
		// setTitle(mDrawerListContent[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	private int login(String username, String password) {
		HttpURLConnection connection = RemoteData
				.getLoginConnection(REDDIT_LOGIN_URL);

		if (connection == null)
			return 0;

		// Parameters that the API needs
		String data = "user=" + username + "&passwd=" + password;

		if (!RemoteData.writeToConnection(connection, data))
			return 0;

		String cookie = connection.getHeaderField("set-cookie");

		if (cookie == null)
			return 0;

		cookie = cookie.split(";")[0];
		if (cookie.startsWith("reddit_first")) {
			// Login failed
			Log.d("Error", "Unable to login.");
			return 0;
		} else if (cookie.startsWith("reddit_session")) {
			// Login success
			Log.d("Success", cookie);
			redditCookie = cookie;
			return 1;
		}
		return 0;
	}

	public void loginUser(View view) {
		Context context = getApplicationContext();
		CharSequence text = "Logging in...";
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();

		EditText username = (EditText) findViewById(R.id.editText1);
		EditText password = (EditText) findViewById(R.id.editText2);

		String usernameText = username.getText().toString();
		String passwordText = password.getText().toString();

		LoginTask task = new LoginTask();
		task.execute(usernameText, passwordText);
	}

	private class LoginTask extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... params) {
			String username = params[0];
			String password = params[1];
			return login(username, password);
		}

		@Override
		protected void onPostExecute(Integer validLogin) {
			if (validLogin == 1) {
				user.setRedditCookie(redditCookie);
				postsHolder.setRedditCookie(redditCookie);
				selectItem(2);
				GenerateNavDrawerTask task = new GenerateNavDrawerTask();
				task.execute();
			} else {
				Context context = getApplicationContext();
				CharSequence text = "Invalid Username or Password";
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}
		}
	}

	private class GenerateNavDrawerTask extends
			AsyncTask<Void, Void, ArrayAdapter<String>> {

		@Override
		protected ArrayAdapter<String> doInBackground(Void... params) {
			List<String> subscribedSubreddits = user.fetchSubscribedSubreddts();
			subscribedSubreddits.add(0, "");
			subscribedSubreddits.add(0, "Dislikes");
			subscribedSubreddits.add(0, "Likes");
			subscribedSubreddits.add(0, "Profile");
			mDrawerListContent = subscribedSubreddits
					.toArray(new String[subscribedSubreddits.size()]);
			ArrayAdapter<String> drawerAdapter = new ArrayAdapter<String>(
					getApplicationContext(), R.layout.drawer_list_item,
					mDrawerListContent);
			return drawerAdapter;
		}

		@Override
		protected void onPostExecute(ArrayAdapter<String> adapter) {
			mDrawerList.setAdapter(null);
			mDrawerList.setAdapter(adapter);
		}
	}

}