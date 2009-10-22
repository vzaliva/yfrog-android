/**
 * 
 */
package com.codeminders.yfrog.android.util;

import java.net.URL;
import java.util.Date;
import java.util.regex.*;

import android.content.*;
import android.content.res.Resources;
import android.graphics.*;
import android.graphics.drawable.*;
import android.net.Uri;
import android.text.*;
import android.text.format.DateFormat;
import android.text.style.*;
import android.view.View;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.view.media.ImageViewActivity;

/**
 * @author idemydenko
 *
 */
public final class StringUtils {
	public static final String EMPTY_STRING = "";
	
	private static final String EMAIL_PATTERN = "[\\p{Alnum}\\.\\-_\\+]{1,}@[\\p{Alnum}\\.\\-_]{1,}\\.[\\p{Alpha}]{1,5}";
	private static final String URL_PATTERN = "(http|https|ftp)\\://[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,5}(:[a-zA-Z0-9]*)?/?([a-zA-Z0-9\\-\\._\\?\\,\'/\\\\\\+&amp;%\\$#\\=~])*";
	
	private static final long SECONDS_20 = 1000 * 20;
	private static final long SECONDS_50 = 1000 * 50;
	private static final long MINUTE = 1000 * 60;
	private static final long MINUTES_48 = MINUTE * 48;
	private static final long HOUR = MINUTE * 60;
	private static final long MINUTES_90 = MINUTE * 90;
	private static final long HOURS_23_31 = HOUR * 23 + MINUTE * 31;
	private static final long DAY = HOUR * 24;
	private static final long DAYS_30 = DAY * 30;
	
	private static final String DAY_30_DATE_FORMAT = "MMM dd, yyyy hh:mm";
	
	private static final int HIGHLIGHT_COLOR = 0x770000ff;
	private static final BackgroundColorSpan HIGHLIGHT_SPAN = new BackgroundColorSpan(HIGHLIGHT_COLOR);
	
	private static Pattern emailPattern = null;
	private static Pattern urlPattern = null;
	
	static {
		emailPattern = Pattern.compile(EMAIL_PATTERN);
		urlPattern = Pattern.compile(URL_PATTERN);
	}
	private StringUtils() {
	}
	
	public static final boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}
	
	public static final boolean isEmail(String email) {
		return emailPattern.matcher(email).matches();
	}
	
	public static final CharSequence formatDate(Resources resources, Date date) {
		long mills = System.currentTimeMillis() - date.getTime();
		if (mills < SECONDS_20) {
			return resources.getString(R.string.date_less_20s);
		} else if (mills < SECONDS_50) {
			return resources.getString(R.string.date_less_50s);
		} else if (mills < MINUTE) {
			return resources.getString(R.string.date_less_60s);
		} else if (mills < MINUTES_48) {
			return resources.getString(R.string.date_less_48m, mills / MINUTE);
		} else if (mills < MINUTES_90) {
			return resources.getString(R.string.date_less_90m);
		} else if (mills < HOURS_23_31) {
			return resources.getString(R.string.date_less_23h, mills / HOUR);
		} else if (mills < DAYS_30) {
			return resources.getString(R.string.date_less_30d, mills / DAY);
		} else {
			return DateFormat.format(DAY_30_DATE_FORMAT, date);
		}
		
	}
	
	public static SpannableString highlightText(String text, String toSpan) {
		String spannable = text.toLowerCase();
		String spanned = toSpan.toLowerCase();
		
		SpannableString result = new SpannableString(text);
		
		int index = spannable.indexOf(spanned);
		int nextStart = 0;
		int length = spanned.length();
		
		while (index > -1) {
			nextStart = index + length;
			result.setSpan(HIGHLIGHT_SPAN, index, nextStart, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			index = spannable.indexOf(spanned, nextStart);
		}
		
		return result;
	}
	
	public static Spannable parseURLs(String source, final Context context) {
		SpannableString spannable = new SpannableString(source);
		Matcher matcher = urlPattern.matcher(source);

		while (matcher.find()) {
			final String url = matcher.group();
			
			if (YFrogUtils.hasYFrogContent(url)) {
				YFrogUtils.buildYFrogURL(context, spannable, url, matcher.start(), matcher.end());
			} else {
				spannable.setSpan(new URLSpan(url), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		
		return spannable;
	}
}
