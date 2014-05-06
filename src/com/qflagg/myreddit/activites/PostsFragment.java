package com.qflagg.myreddit.activites;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.qflagg.myreddit.Post;
import com.qflagg.myreddit.PostInflater;
import com.qflagg.myreddit.PostsHolder;
import com.qflagg.myreddit.R;
import com.qflagg.myreddit.adapters.BaseInflaterAdapter;

public class PostsFragment extends Fragment {
	PostsHolder postsHolder;
	List<Post> posts = new ArrayList<Post>();
	ListView list;
	
	public PostsFragment() {
	}

	public PostsFragment newInstance(PostsHolder postsHolder) {
		PostsFragment pf = new PostsFragment();
		pf.postsHolder = postsHolder;
		return pf;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_list,
				container, false);
		list = (ListView) rootView.findViewById(R.id.list_view);

		list.addHeaderView(new View(getActivity()));
		list.addFooterView(new View(getActivity()));
		PopulateCardsTask task = new PopulateCardsTask();
		task.execute();

		return rootView;
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
			list.setVisibility(ListView.INVISIBLE);
			list.setVisibility(ListView.VISIBLE);
			list.setAdapter(adapter);
		}
	}

	private BaseInflaterAdapter<Post> initialize() {

		posts.clear();
		posts.addAll(postsHolder.fetchPosts());

		BaseInflaterAdapter<Post> postAdapter = new BaseInflaterAdapter<Post>(
				new PostInflater());
		postAdapter.clear(false);

		for (int i = 0; i < posts.size(); i++) {
			Post data = posts.get(i);
			postAdapter.addItem(data, false);
		}

		return postAdapter;
	}

}
