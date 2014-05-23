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
import android.widget.Button;
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
import com.qflagg.myreddit.adapters.TitleNavigationAdapter;

public class MainActivity extends FragmentActivity implements
		ActionBar.OnNavigationListener {

	private final String REDDIT_LOGIN_URL = "https://ssl.reddit.com/api/login";

	private ActionBar actionBar; // action bar
	private ArrayList<String> navSpinner; // title navigation Spinner
	private TitleNavigationAdapter actionBarAdapter; // action bar adapter

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

	private final String[] DEFAULT_SUBREDDITS = { "Front Page",
			"Announcements", "Art", "AskReddit", "AskScience", "Aww", "Blog",
			"Books", "Creepy", "DataIsBeautiful", "DIY", "Documentaries",
			"EarthPorn", "ExplainLikeImFive", "Fitness", "Food", "Funny",
			"Futurology", "Gadgets", "Gaming", "GetMotivated", "Gifs",
			"History", "IAmA", "InternetIsBeautiful", "Jokes", "LifeProTips",
			"ListenToThis", "MildyInteresting", "Movies", "Music", "News",
			"NoSleep", "NoTheOnion", "OldSchoolCool", "PersonalFinance",
			"Philosophy", "PhotoshopBattles", "Pics", "Science",
			"ShowerThoughts", "Space", "Sports", "Television", "Tifu",
			"TodayILearned", "TwoXChromosomes", "UpliftingNews", "Videos",
			"WorldNews", "WritingPrompts" };

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		postsHolder = new PostsHolder("", redditCookie);
		user = new User(redditCookie);
		actionBar = setUpActionBar(actionBar);
		setUpNavigationDrawer();
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
		if (redditCookie.length() > 0) {
			LinearLayout ll = (LinearLayout) view.getParent();
			TextView post_id = (TextView) ll.findViewById(R.id.post_id);
			String id = (String) post_id.getText();
			
			ArrayList<Boolean> up_vote_clicked = (ArrayList<Boolean>) view.getTag(R.id.UP_VOTE_CLICKED);
			ArrayList<Boolean> down_vote_clicked = (ArrayList<Boolean>) view.getTag(R.id.DOWN_VOTE_CLICKED);
			int position = (Integer) view.getTag(R.id.POSITION);
			Button downvote = (Button) view.getTag(R.id.downvote);
			Button upvote = (Button) view;
			final boolean isUpClicked = up_vote_clicked.get(position);
			final boolean isDownClicked = down_vote_clicked.get(position);

			if (!isUpClicked) {
				if (isDownClicked) {
					downvote.setBackgroundResource(R.drawable.ic_action_down);
					down_vote_clicked.set(position, false);
				}
				upvote.setBackgroundResource(R.drawable.ic_action_up_pressed);
				up_vote_clicked.set(position, true);
				user.votePost(id, 1);
			} else {
				upvote.setBackgroundResource(R.drawable.ic_action_up);
				up_vote_clicked.set(position, false);
				user.votePost(id, 0);
			}

			upvote.setTag(R.id.UP_VOTE_CLICKED, up_vote_clicked);
			upvote.setTag(R.id.DOWN_VOTE_CLICKED, down_vote_clicked);
			upvote.setTag(R.id.POSITION, position);
			upvote.setTag(R.id.downvote, downvote);
		} else {
			Context context = getApplicationContext();
			CharSequence text = "Please login to upvote posts...";
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
	}

	public void downVoteClicked(View view) {
		if (redditCookie.length() > 0) {
			ArrayList<Boolean> up_vote_clicked = (ArrayList<Boolean>) view
					.getTag(R.id.UP_VOTE_CLICKED);
			ArrayList<Boolean> down_vote_clicked = (ArrayList<Boolean>) view
					.getTag(R.id.DOWN_VOTE_CLICKED);
			int position = (Integer) view.getTag(R.id.POSITION);
			Button upvote = (Button) view.getTag(R.id.upvote);
			Button downvote = (Button) view;
			final boolean isUpClicked = up_vote_clicked.get(position);
			final boolean isDownClicked = down_vote_clicked.get(position);
			if (!isDownClicked) {
				if (isUpClicked) {
					upvote.setBackgroundResource(R.drawable.ic_action_up);
					up_vote_clicked.set(position, false);
				}
				downvote.setBackgroundResource(R.drawable.ic_action_down_pressed);
				down_vote_clicked.set(position, true);
			} else {
				downvote.setBackgroundResource(R.drawable.ic_action_down);
				down_vote_clicked.set(position, false);
			}

			downvote.setTag(R.id.UP_VOTE_CLICKED, up_vote_clicked);
			downvote.setTag(R.id.DOWN_VOTE_CLICKED, down_vote_clicked);
			downvote.setTag(R.id.POSITION, position);
			downvote.setTag(R.id.upvote, upvote);
		} else {
			Context context = getApplicationContext();
			CharSequence text = "Please login to downvote posts...";
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
	}

	public void commentClicked(View view) {
		Context context = getApplicationContext();
		CharSequence text = "comment";
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}

	// onClick Listeners end

	private String[] generateDefaultDrawerListContent() {
		String[] content = new String[53];

		content[0] = "Login";
		content[1] = "";

		int j = 0;
		for (int i = 2; i < content.length; i++)
			content[i] = DEFAULT_SUBREDDITS[j++];

		return content;
	}

	private void setUpNavigationDrawer() {
		mTitle = mDrawerTitle = getTitle();

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
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
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu();
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	private ActionBar setUpActionBar(ActionBar actionBar) {
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		return actionBar;
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
		for (int i = 0; i < mDrawerListContent.length; i++)
			switch (position) {
			case 0:
				if (mDrawerListContent[position].equals("Login")) {
					fragment = new LoginFragment();
				}
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
				new Thread(new Runnable() {
					public void run() {
						user.getModHash();
					}
				}).start();
				
				postsHolder.setRedditCookie(redditCookie);
				new GenerateNavDrawerTask().execute();
				selectItem(2);
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
			subscribedSubreddits.add(0, "Front Page");
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
			mDrawerList.setAdapter(adapter);
		}
	}

}