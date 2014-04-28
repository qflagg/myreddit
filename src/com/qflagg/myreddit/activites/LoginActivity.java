package com.qflagg.myreddit.activites;

import java.net.HttpURLConnection;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.qflagg.myreddit.Post;
import com.qflagg.myreddit.R;
import com.qflagg.myreddit.RemoteData;
import com.qflagg.myreddit.adapters.BaseInflaterAdapter;

public class LoginActivity extends FragmentActivity {
	private final String REDDIT_LOGIN_URL = "https://ssl.reddit.com/api/login";

	private String redditCookie = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		setContentView(R.layout.activity_login);
	}

	int login(String username, String password) {
		HttpURLConnection connection = RemoteData.getLoginConnection(REDDIT_LOGIN_URL);

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
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				intent.putExtra("redditCookie", redditCookie);
				startActivity(intent);
			} else {
				Context context = getApplicationContext();
				CharSequence text = "Invalid Username or Password";
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}
	    }
	  }

}
