package com.qflagg.myreddit.activites;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qflagg.myreddit.Post;
import com.qflagg.myreddit.PostInflater;
import com.qflagg.myreddit.PostsHolder;
import com.qflagg.myreddit.R;
import com.qflagg.myreddit.adapters.BaseInflaterAdapter;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class PostsFragment extends Fragment implements OnScrollListener {
	PostsHolder postsHolder;
	List<Post> posts = new ArrayList<Post>();
	PullToRefreshListView list;
	ProgressBar progressBar;
	BaseInflaterAdapter<Post> postAdapter;

	public PostsFragment() {
	}

	public static PostsFragment newInstance(PostsHolder postsHolder) {
		PostsFragment pf = new PostsFragment();
		pf.postsHolder = postsHolder;
		return pf;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_list, container,
				false);

		list = (PullToRefreshListView) rootView.findViewById(R.id.list_view);
		list.setOnScrollListener(this);
		list.setItemsCanFocus(true);
		list.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				FragmentManager fm = getActivity().getFragmentManager();
				PostsFragment pf = PostsFragment.newInstance(postsHolder);
				fm.beginTransaction()
						.replace(R.id.content_frame, pf,
								PostsFragment.class.getName()).commit();

			}
		});
		
		list.setItemsCanFocus(true);
		list.setFocusable(false);
		list.setFocusableInTouchMode(false);
		list.setClickable(false);

		progressBar = (ProgressBar) rootView
				.findViewById(R.id.main_progress_bar);

		list.setVisibility(android.view.View.GONE);
		progressBar.setVisibility(android.view.View.VISIBLE);
		list.addHeaderView(new View(getActivity()));
		list.addFooterView(new View(getActivity()));
		PopulateCardsTask task = new PopulateCardsTask();
		task.execute();

		return rootView;
	}

	private class RetrieveMoreCardsTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			int oldPostSize = posts.size();
			posts.addAll(oldPostSize, postsHolder.fetchMorePosts());
			for (int i = oldPostSize; i < posts.size(); i++) {
				Post data = posts.get(i);
				postAdapter.addItem(data, false);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void params) {
			postAdapter.notifyDataSetChanged();
		}
	}

	private class PopulateCardsTask extends
			AsyncTask<Void, Void, BaseInflaterAdapter<Post>> {

		@Override
		protected BaseInflaterAdapter<Post> doInBackground(Void... params) {
			return initialize();
		}

		@Override
		protected void onPostExecute(BaseInflaterAdapter<Post> adapter) {
			list.destroyDrawingCache();
			list.setVisibility(android.view.View.VISIBLE);
			progressBar.setVisibility(android.view.View.GONE);
			list.setAdapter(adapter);
		}
	}

	private BaseInflaterAdapter<Post> initialize() {

		posts.clear();
		posts.addAll(postsHolder.fetchPosts());

		postAdapter = new BaseInflaterAdapter<Post>(new PostInflater());

		for (int i = 0; i < posts.size(); i++) {
			Post data = posts.get(i);
			postAdapter.addItem(data, false);
		}

		return postAdapter;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE) {
			if (list.getLastVisiblePosition() >= list.getCount() - 1 - 5) {
				RetrieveMoreCardsTask task = new RetrieveMoreCardsTask();
				task.execute();
			}
		}

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub

	}

}
