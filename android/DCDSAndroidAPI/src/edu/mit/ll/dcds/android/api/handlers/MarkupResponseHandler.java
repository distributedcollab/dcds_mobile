/*|~^~|Copyright (c) 2008-2016, Massachusetts Institute of Technology (MIT)
 |~^~|All rights reserved.
 |~^~|
 |~^~|Redistribution and use in source and binary forms, with or without
 |~^~|modification, are permitted provided that the following conditions are met:
 |~^~|
 |~^~|-1. Redistributions of source code must retain the above copyright notice, this
 |~^~|list of conditions and the following disclaimer.
 |~^~|
 |~^~|-2. Redistributions in binary form must reproduce the above copyright notice,
 |~^~|this list of conditions and the following disclaimer in the documentation
 |~^~|and/or other materials provided with the distribution.
 |~^~|
 |~^~|-3. Neither the name of the copyright holder nor the names of its contributors
 |~^~|may be used to endorse or promote products derived from this software without
 |~^~|specific prior written permission.
 |~^~|
 |~^~|THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 |~^~|AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 |~^~|IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 |~^~|DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 |~^~|FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 |~^~|DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 |~^~|SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 |~^~|CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 |~^~|OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 |~^~|OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.\*/
/**
 *
 */
package edu.mit.ll.dcds.android.api.handlers;

import java.util.ArrayList;

import org.apache.http.Header;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;

import edu.mit.ll.dcds.android.api.DataManager;
import edu.mit.ll.dcds.android.api.RestClient;
import edu.mit.ll.dcds.android.api.data.MarkupFeature;
import edu.mit.ll.dcds.android.utils.Intents;

public class MarkupResponseHandler extends AsyncHttpResponseHandler {
	private DataManager mDataManager;
	private Context mContext;
	private MarkupFeature mFeature;
	
	public MarkupResponseHandler(Context context, DataManager dataManager, MarkupFeature feature) {
		mContext = context;
		mFeature = feature;
		mDataManager = dataManager;
	}

	@Override
	public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
		mDataManager.setLastSuccesfulServerCommsTimestamp(System.currentTimeMillis());
		String content = (responseBody != null) ? new String(responseBody) : "";
		Log.e("dcdsRest", "Successfully posted Map Markup Features");
		
		mDataManager.deleteMarkupFeatureStoreAndForward(mFeature.getId());
		mDataManager.addPersonalHistory("Map Markup successfully sent: " + mFeature.getId() + "\n");
		mDataManager.requestMarkupRepeating(mDataManager.getCollabroomDataRate(), true);
		
		RestClient.setSendingMarkupFeatures(false);
		RestClient.postMarkupFeatures();//send another feature if there are more to send.
	}

	@Override
	public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
		Log.e("dcdsRest", "Failed to post Markup Feature information: " + error.getMessage());
		String content = (responseBody != null) ? new String(responseBody) : "";
				
		mDataManager.deleteMarkupFeatureStoreAndForward(mFeature.getId());
		
		Intent intent = new Intent();
	    intent.setAction(Intents.dcds_FAILED_TO_POST_MARKUP);
	    if(content.contains("{")){
	    	intent.putExtra("message", "Invalid Markup Data");
	    }else{
	    	intent.putExtra("message", content);
	    }
		mContext.sendBroadcast (intent);
		
		RestClient.setSendingMarkupFeatures(false);
	}
}
