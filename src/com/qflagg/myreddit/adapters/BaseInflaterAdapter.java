package com.qflagg.myreddit.adapters;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qflagg.myreddit.R;

public class BaseInflaterAdapter<T> extends BaseAdapter {
	private List<T> m_items = new ArrayList<T>();
	private IAdapterViewInflater<T> m_viewInflater;
	private List<Boolean> up_vote_clicked = new ArrayList<Boolean>();
	private List<Boolean> down_vote_clicked = new ArrayList<Boolean>();

	public BaseInflaterAdapter(IAdapterViewInflater<T> viewInflater) {
		m_viewInflater = viewInflater;
	}

	public BaseInflaterAdapter(List<T> items,
			IAdapterViewInflater<T> viewInflater) {
		m_items.addAll(items);
		m_viewInflater = viewInflater;
	}

	public void setViewInflater(IAdapterViewInflater<T> viewInflater,
			boolean notifyChange) {
		m_viewInflater = viewInflater;

		if (notifyChange)
			notifyDataSetChanged();
	}

	public void addItem(T item, boolean notifyChange) {
		m_items.add(item);

		if (notifyChange)
			notifyDataSetChanged();
	}

	public void addItems(List<T> items, boolean notifyChange) {
		m_items.addAll(items);

		if (notifyChange)
			notifyDataSetChanged();
	}

	public void clear(boolean notifyChange) {
		m_items.clear();

		if (notifyChange)
			notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return m_items.size();
	}

	@Override
	public Object getItem(int pos) {
		return getTItem(pos);
	}

	public T getTItem(int pos) {
		return m_items.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		if (m_viewInflater != null) {
			convertView = m_viewInflater
					.inflate(this, pos, convertView, parent);
		}
		up_vote_clicked.add(false);
		down_vote_clicked.add(false);
		final Button upvote = (Button) convertView.findViewById(R.id.upvote);
		final Button downvote = (Button) convertView.findViewById(R.id.downvote);
		
		if(up_vote_clicked.get(pos) == false) {
			upvote.setBackgroundResource(R.drawable.ic_action_up);
		} else {
			upvote.setBackgroundResource(R.drawable.ic_action_up_pressed);
		}
		
		if(down_vote_clicked.get(pos) == false) {
			downvote.setBackgroundResource(R.drawable.ic_action_down);
		} else {
			downvote.setBackgroundResource(R.drawable.ic_action_down_pressed);
		}
		
		upvote.setTag(R.id.UP_VOTE_CLICKED, up_vote_clicked);
		upvote.setTag(R.id.DOWN_VOTE_CLICKED, down_vote_clicked);
		upvote.setTag(R.id.POSITION, pos);
		upvote.setTag(R.id.downvote, downvote);
		
		downvote.setTag(R.id.UP_VOTE_CLICKED, up_vote_clicked);
		downvote.setTag(R.id.DOWN_VOTE_CLICKED, down_vote_clicked);
		downvote.setTag(R.id.POSITION, pos);
		downvote.setTag(R.id.upvote, upvote);
		
//		final int position = pos;
//		upvote.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				final boolean isUpClicked = up_vote_clicked.get(position);
//				final boolean isDownClicked = down_vote_clicked.get(position);
//					if(!isUpClicked) {
//						if(isDownClicked){
//							downvote.setBackgroundResource(R.drawable.ic_action_down);
//							down_vote_clicked.set(position, false);
//						}
//						upvote.setBackgroundResource(R.drawable.ic_action_up_pressed);
//						up_vote_clicked.set(position, true);
//					} else {
//						upvote.setBackgroundResource(R.drawable.ic_action_up);
//						up_vote_clicked.set(position, false);
//					}
//				}
//
//		});
//		
//		downvote.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				final boolean isDownClicked = down_vote_clicked.get(position);
//				final boolean isUpClicked = up_vote_clicked.get(position);
//					if(!isDownClicked) {
//						if(isUpClicked){
//							upvote.setBackgroundResource(R.drawable.ic_action_up);
//							up_vote_clicked.set(position, false);
//						}
//						downvote.setBackgroundResource(R.drawable.ic_action_down_pressed);
//						down_vote_clicked.set(position, true);
//					} else {
//						downvote.setBackgroundResource(R.drawable.ic_action_down);
//						down_vote_clicked.set(position, false);
//					}
//				}
//
//		});
		return convertView;

	}
}
