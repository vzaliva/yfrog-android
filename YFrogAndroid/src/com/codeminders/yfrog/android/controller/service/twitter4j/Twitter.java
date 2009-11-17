/**
 * 
 */
package com.codeminders.yfrog.android.controller.service.twitter4j;

import com.codeminders.yfrog.android.util.AlertUtils;

import twitter4j.TwitterException;
import twitter4j.http.Response;
import twitter4j.org.json.JSONException;
import twitter4j.org.json.JSONObject;

/**
 * @author idemydenko
 *
 */
public class Twitter extends twitter4j.Twitter {
	
	public Twitter() {
		super();
	}
	
	public Twitter(String username, String password) {
		
	}
	
	public boolean isNotificationEnabled(String username) throws TwitterException {
		Response response = get(getBaseURL() + "friendships/show.json", "target_screen_name", username, true);
		
		JSONObject json = response.asJSONObject();
		try {
			if (json.isNull("relationship")) {
				throw new TwitterException("Invalid response format");
			}
			
			JSONObject relationship = json.getJSONObject("relationship");
			
			if (relationship.isNull("source")) {
				throw new TwitterException("Invalid response format");
			}
			
			return relationship.getJSONObject("source").getBoolean("notifications_enabled");
		} catch (JSONException jsone) {
			throw new TwitterException(jsone.getMessage() + ":" + json.toString(), jsone);
		} catch (TwitterException e) {
			throw new TwitterException("Invalid response format", -1);
		}
	}

}
