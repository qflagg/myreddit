package com.qflagg.myreddit.activites;

import java.net.HttpURLConnection;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.qflagg.myreddit.R;
import com.qflagg.myreddit.RemoteData;

public class LoginFragment extends Fragment{
	
	public LoginFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_login,
				container, false);
		
		return rootView;
	}

	
	
	

}
