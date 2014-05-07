package com.qflagg.myreddit;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.qflagg.myreddit.adapters.BaseInflaterAdapter;
import com.qflagg.myreddit.adapters.IAdapterViewInflater;

public class PostInflater implements IAdapterViewInflater<Post> {
	
	@Override
	public View inflate(final BaseInflaterAdapter<Post> adapter, final int pos,
			View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			convertView = inflater.inflate(R.layout.list_item_card, parent, false);
			holder = new ViewHolder(convertView);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final Post item = adapter.getTItem(pos);
		holder.updateDisplay(item);

		return convertView;
	}

	private class ViewHolder {
		private View m_rootView;
		private TextView title;
		private TextView subreddit;
		private TextView karma;
		private TextView subtext;
		private TextView hiddenUrl;
		private TextView num_comments;
		private ImageView m_image;
		private ImageView gif;
		int count = 0;
		int imageHeight = 0;
		int imageWidth = 0;


		public ViewHolder(View rootView) {
			m_rootView = rootView;
			title = (TextView) m_rootView.findViewById(R.id.title);
			subreddit = (TextView) m_rootView.findViewById(R.id.subreddit);
			karma = (TextView) m_rootView.findViewById(R.id.karma);
			subtext = (TextView) m_rootView.findViewById(R.id.subtext);
			hiddenUrl = (TextView) m_rootView.findViewById(R.id.url);
			num_comments = (TextView) m_rootView.findViewById(R.id.numComments);
			m_image = (ImageView) m_rootView.findViewById(R.id.imageView1);
			gif = (ImageView) m_rootView.findViewById(R.id.gif);
			
			rootView.setTag(this);
		}

		public void updateDisplay(Post item) {
			String url = item.getUrl();
			String subtextString = item.getSubtext();
			Log.d(item.getTitle() + " " + count, url);
			count++;
			title.setText(item.getTitle());
			subreddit.setText("to r/" + item.getSubreddit() + " by " + item.getAuthor());
			karma.setText(item.getKarma() + " (" + item.getUp() + "|" + item.getDown() + ")");
			num_comments.setText(item.getNumComments() + " comments");
			
			if(url.contains(".gif")){
				gif.setVisibility(android.view.View.VISIBLE);
			} else {
				gif.setVisibility(android.view.View.GONE);
			}
			
			hiddenUrl.setText(url);
			if(!url.contains("imgur") && !url.contains(".jpg") && !url.contains(".gif") && !subtextString.equals("")) {
				m_image.setVisibility(android.view.View.GONE);
				subtext.setVisibility(android.view.View.VISIBLE);
				subtext.setText(subtextString);
			} else {
				m_image.setVisibility(android.view.View.VISIBLE);
				subtext.setVisibility(android.view.View.GONE);
				UrlImageViewHelper.setUrlDrawable(m_image, url, null, 1800000);
			}
		}
		
	}
}
