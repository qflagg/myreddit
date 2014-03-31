package com.qflagg.myreddit;

import java.net.HttpURLConnection;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends FragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
	}


	public void loginUser(View view) {
		EditText username = (EditText) findViewById(R.id.editText1);
		EditText password = (EditText) findViewById(R.id.editText2);

		String usernameText = username.getText().toString();
		String passwordText = password.getText().toString();
		Login login = new Login();
		boolean validLogin = login.login(usernameText, passwordText);
		if (validLogin) {
			Intent intent = new Intent(this, MainActivity.class);
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
