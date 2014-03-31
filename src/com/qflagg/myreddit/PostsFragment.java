package com.qflagg.myreddit;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PostsFragment extends Fragment {

	ListView postsList;
	ArrayAdapter<Post> adapter;
	Handler handler;

	String subreddit;
	List<Post> posts;
	PostsHolder postsHolder;

	public PostsFragment() {
		handler = new Handler();
		posts = new ArrayList<Post>();
	}

	public static Fragment newInstance(String subreddit) {
		PostsFragment postFragment = new PostsFragment();
		postFragment.subreddit = subreddit;
		postFragment.postsHolder = new PostsHolder(postFragment.subreddit);
		return postFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.posts, container, false);
		postsList = (ListView) view.findViewById(R.id.posts_list);
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initialize();
	}

	private void initialize() {
		if (posts.size() == 0) {
			new Thread() {
				public void run() {
					posts.addAll(postsHolder.fetchPosts());
					handler.post(new Runnable() {
						public void run() {
							createAdapter();
						}
					});
				}
			}.start();
		} else {
			createAdapter();
		}
	}

	/**
	 * This method creates the adapter from the list of posts , and assigns it
	 * to the list.
	 */
	private void createAdapter() {

		// Make sure this fragment is still a part of the activity.
		if (getActivity() == null)
			return;

		adapter = new ArrayAdapter<Post>(getActivity(), R.layout.post_item,
				posts) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {

				if (convertView == null) {
					convertView = getActivity().getLayoutInflater().inflate(
							R.layout.post_item, null);
				}

				TextView postTitle;
				postTitle = (TextView) convertView
						.findViewById(R.id.post_title);

				TextView postDetails;
				postDetails = (TextView) convertView
						.findViewById(R.id.post_details);

				TextView postScore;
				postScore = (TextView) convertView
						.findViewById(R.id.post_score);

				postTitle.setText(posts.get(position).title);
				postDetails.setText(posts.get(position).getDetails());
				postScore.setText(posts.get(position).getKarma());
				return convertView;
			}
		};
		postsList.setAdapter(adapter);
	}

}
