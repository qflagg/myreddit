package com.qflagg.myreddit;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.afollestad.cardsui.Card;
import com.afollestad.cardsui.CardAdapter;
import com.afollestad.cardsui.CardHeader;
import com.afollestad.cardsui.CardListView;

public class MainActivity extends FragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		CardListView cardsList = (CardListView) findViewById(R.id.cardsList);
		CardAdapter cardsAdapter = new CardAdapter(this, 0);
		cardsList.setAdapter(cardsAdapter);

		cardsAdapter.add(new CardHeader("Subreddit"));
		cardsAdapter.add(new Card("Title 1", "Author 1"));
		cardsAdapter.add(new Card("Title 2","Author 2"));
		cardsAdapter.add(new Card("Title 3", "Author 3").setLayout(R.layout.list_item_card));
		
		
	}
}

// void addFragment(){
// getSupportFragmentManager()
// .beginTransaction()
// .add(R.id.fragments_holder
// , PostsFragment.newInstance("askreddit"))
// .commit();
// }