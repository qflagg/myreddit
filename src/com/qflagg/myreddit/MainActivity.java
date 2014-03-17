package com.qflagg.myreddit;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.support.v4.app.FragmentActivity;

public class MainActivity extends FragmentActivity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addFragment();
    }
    
    void addFragment(){
        getSupportFragmentManager()
            .beginTransaction()
            .add(R.id.fragments_holder
                 , PostsFragment.newInstance("askreddit"))
            .commit();
    }
}
