/**
 * 
 */
package com.codeminders.yfrog.android.view.message;

import java.net.URL;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.controller.service.ServiceFactory;
import com.codeminders.yfrog.android.controller.service.TwitterService;
import com.codeminders.yfrog.android.model.TwitterStatus;
import com.codeminders.yfrog.android.util.StringUtils;
import com.codeminders.yfrog.android.util.image.cache.ImageCache;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author idemydenko
 *
 */

// TODO may be need StatusChangeListener
public class StatusDetailsActivity extends Activity implements OnClickListener {
	public static final String KEY_STATUS = "status";
	
	private TwitterService twitterService;
	private TwitterStatus status;
	private boolean favorited;
	private boolean my;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		twitterService = ServiceFactory.getTwitterService();
		setContentView(R.layout.twitter_status_details);
		
		Bundle extras = getIntent().getExtras();
		status = (TwitterStatus) extras.getSerializable(KEY_STATUS);
		favorited = status.isFavorited();
		my = twitterService.getLoggedUser().equals(status.getUser());
		
		ImageView imageView = (ImageView) findViewById(R.id.tu_user_icon);
		ImageCache.getInstance().putImage(status.getUser().getProfileImageURL(), imageView);
		
		TextView view = (TextView) findViewById(R.id.tu_username);
		view.setText(status.getUser().getUsername());
		
		view = (TextView) findViewById(R.id.tu_fullname);
		view.setText(status.getUser().getFullname());
	
		view = (TextView) findViewById(R.id.tm_created_at);
		view.setText(StringUtils.formatDate(getResources(), status.getCreatedAt()));

		view = (TextView) findViewById(R.id.tm_text);
		view.setText(status.getText());
		
//		view.setMovementMethod(LinkMovementMethod.getInstance());
//		Spanned text = Html.fromHtml(StringUtils.toHtml(status.getText()), new Html.ImageGetter() {
//			@Override
//			public Drawable getDrawable(String source) {
//				
//				BitmapDrawable drawable = null;
//				
//				try {
//					drawable = new BitmapDrawable(new URL(source).openStream());
//				} catch (Exception e) {
//					
//				}
//				
//				drawable.setBounds(0, 0, 48, 48);
//				System.out.println(drawable);
//				return drawable;
//			}
//		}, null);
//		
//		view.setText(text);
		
		Button button = (Button) findViewById(R.id.tm_replay);
		if (my) {
			button.setVisibility(View.INVISIBLE);
		}
		button.setOnClickListener(this);

		
		button = (Button) findViewById(R.id.tm_favorite);
		if (favorited) {
			button.setText(R.string.tm_btn_unfavorite);
		}
		button.setOnClickListener(this);
		
		button = (Button) findViewById(R.id.tm_forward);
		button.setOnClickListener(this);
		
		button = (Button) findViewById(R.id.tm_delete);
		if (!my) {
			button.setVisibility(View.INVISIBLE);
		}
		button.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tm_replay:
			Intent intent = new Intent(this, WriteReplayActivity.class);
			intent.putExtra(WriteReplayActivity.KEY_MESSAGE_ID, status.getId());
			intent.putExtra(WritableActivity.KEY_WRITER_USERNAME, status.getUser().getUsername());
			startActivity(intent);
			break;
		case R.id.tm_favorite:
			favorite();
			break;
		case R.id.tm_forward:
			break;
		case R.id.tm_delete:
			try {
				twitterService.deleteStatus(status.getId());
			} catch (YFrogTwitterException e) {
				// TODO: handle exception
			}
			finish();
			break;
		}
	}
	
	private void favorite() {
		try {
			if (!favorited) {
				twitterService.favorite(status.getId());
			} else {
				twitterService.unfavorite(status.getId());
			}
			favorited = !favorited;
			
			Button button = (Button) findViewById(R.id.tm_favorite);
			button.setText(favorited ? R.string.tm_btn_unfavorite : R.string.tm_btn_favorite);
		} catch (YFrogTwitterException e) {
			// TODO: handle exception
		}		
	}
	
}
