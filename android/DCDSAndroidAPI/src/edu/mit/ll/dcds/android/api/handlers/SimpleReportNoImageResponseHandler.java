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

import org.apache.http.Header;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import edu.mit.ll.dcds.android.api.DataManager;
import edu.mit.ll.dcds.android.api.RestClient;
import edu.mit.ll.dcds.android.api.data.ReportSendStatus;
import edu.mit.ll.dcds.android.api.messages.SimpleReportMessage;
import edu.mit.ll.dcds.android.api.payload.forms.SimpleReportPayload;
import edu.mit.ll.dcds.android.utils.Intents;

public class SimpleReportNoImageResponseHandler extends AsyncHttpResponseHandler {
	private DataManager mDataManager;
	private Context mContext;
	private long mReportId;
	
	public SimpleReportNoImageResponseHandler(Context context, DataManager dataManager, long reportId) {
		mContext = context;
		mReportId = reportId;
		mDataManager = dataManager;
	}
	
	@Override
	public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
		mDataManager.setLastSuccesfulServerCommsTimestamp(System.currentTimeMillis());
		Log.e("dcdsRest", "Success to post Simple Report No Image information");
		
		String content = (responseBody != null) ? new String(responseBody) : "";
		SimpleReportMessage message = new Gson().fromJson(content, SimpleReportMessage.class);
		for(SimpleReportPayload payload : message.getReports()) {
			mDataManager.deleteSimpleReportStoreAndForward(mReportId);
			payload.setSendStatus(ReportSendStatus.SENT);
			payload.setProgress(100);
			payload.parse();
			
			Intent intent = new Intent();
		    intent.setAction(Intents.dcds_SIMPLE_REPORT_PROGRESS);
		    
			intent.putExtra("reportId", mReportId);
			intent.putExtra("progress", (double)100);
			
	        mContext.sendBroadcast (intent);
			
			mDataManager.addSimpleReportToStoreAndForward(payload);
		}
		
		mDataManager.requestSimpleReports();
		RestClient.setSendingSimpleReports(false);
	}
	
	@Override
	public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
		Log.e("dcdsRest", "Failed to post Simple Report No Image information: " + error.getMessage());

		RestClient.setSendingSimpleReports(false);
	}

}
