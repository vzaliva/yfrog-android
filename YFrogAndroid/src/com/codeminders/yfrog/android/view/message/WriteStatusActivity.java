/**
 * 
 */
package com.codeminders.yfrog.android.view.message;

import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.model.TwitterStatus;
import com.codeminders.yfrog.android.model.UnsentMessage;

/**
 * @author idemydenko
 *
 */
public class WriteStatusActivity extends WritableActivity {
	private TwitterStatus sent = null;
	
	protected void send(String text) {
		try {
			sent = twitterService.updateStatus(text);
		} catch (YFrogTwitterException e) {
			
		}		
	}
	
	@Override
	protected UnsentMessage createUnsentMessage() {
		UnsentMessage message = new UnsentMessage();
		message.setType(UnsentMessage.TYPE_STATUS);
		return message;
	}
	
	@Override
	protected void callback() {
		if (sent != null) {
			getIntent().putExtra(KET_SENT, sent);
			setResult(RESULT_SEND, getIntent());
		}
	}
}
