package com.qflagg.myreddit;

import java.net.HttpURLConnection;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends FragmentActivity {
	private final String REDDIT_LOGIN_URL = "https://ssl.reddit.com/api/login";

	private String redditCookie = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		setContentView(R.layout.login);
	}

	boolean login(String username, String password) {
		HttpURLConnection connection = RemoteData.getLoginConnection(REDDIT_LOGIN_URL);

		if (connection == null)
			return false;

		// Parameters that the API needs
		String data = "user=" + username + "&passwd=" + password;

		if (!RemoteData.writeToConnection(connection, data))
			return false;

		String cookie = connection.getHeaderField("set-cookie");

		if (cookie == null)
			return false;

		cookie = cookie.split(";")[0];
		if (cookie.startsWith("reddit_first")) {
			// Login failed
			Log.d("Error", "Unable to login.");
			return false;
		} else if (cookie.startsWith("reddit_session")) {
			// Login success
			Log.d("Success", cookie);
			redditCookie = cookie;
			return true;
		}
		return false;
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
		
		boolean validLogin = login(usernameText, passwordText);
		if (validLogin) {
			Intent intent = new Intent(this, MainActivity.class);
			intent.putExtra("redditCookie", redditCookie);
			startActivity(intent);
		} else {
			context = getApplicationContext();
			text = "Invalid Username or Password";

			toast = Toast.makeText(context, text, duration);
			toast.show();
		}
	}

}
