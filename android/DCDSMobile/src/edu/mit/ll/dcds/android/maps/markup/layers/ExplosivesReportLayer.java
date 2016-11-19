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
package edu.mit.ll.dcds.android.maps.markup.layers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;

import edu.mit.ll.dcds.android.MainActivity;
import edu.mit.ll.dcds.android.R;
import edu.mit.ll.dcds.android.api.data.UxoReportData;
import edu.mit.ll.dcds.android.api.payload.TrackingLayerPayload;
import edu.mit.ll.dcds.android.api.payload.forms.UxoReportPayload;
import edu.mit.ll.dcds.android.maps.markup.MarkupBaseShape;
import edu.mit.ll.dcds.android.maps.markup.MarkupSymbol;
import edu.mit.ll.dcds.android.utils.Intents;

public class ExplosivesReportLayer extends MarkupLayer {
	
	protected HashMap<String, UxoReportPayload> mLayerFeatures;	
	protected AsyncTask<ArrayList<UxoReportPayload>, Object, Integer> mParseFeaturesTask;
	private BroadcastReceiver srReceiver;
	private boolean receiverRegistered = false;
	
	public ExplosivesReportLayer(Context context, TrackingLayerPayload layer, GoogleMap map) {
		super(context, layer, map);
		mLayerFeatures = new HashMap<String, UxoReportPayload>();
	}

	@Override
	protected void setupReceiver() {
		srReceiver = new BroadcastReceiver() {
			@SuppressWarnings("unchecked")
			@SuppressLint("Wakelock")
			@Override
			public void onReceive(Context context, Intent intent) {
				try {
					PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
					PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "dcds_WAKE");
		
					//Acquire the lock
					wakeLock.acquire();
					
					Bundle extras = intent.getExtras();
					if(extras != null) {
						String type = extras.getString("type", "");
						String layerName = extras.getString("layerName");
						Log.i("dcdsDataManager", "Requesting Data Update: " + type + " " + layerName);

						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
						format.setTimeZone(TimeZone.getTimeZone("UTC"));
						
						ArrayList<UxoReportPayload> uxoReports = mDataManager.getUxoReportHistoryForIncident(mDataManager.getActiveIncidentId());

						clearFromMap();
						mParseFeaturesTask = new ParseFeaturesTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uxoReports);
					}
					
					//Release the lock
					wakeLock.release();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		
		Intent intent = new Intent(Intents.dcds_POLLING_WFS_LAYER + mLayerPayload.getLayername());
    	intent.putExtra("type", "wfslayer");
    	intent.putExtra("layerName", mLayerPayload.getLayername());
    	
    	mContext.registerReceiver(srReceiver, new IntentFilter(Intents.dcds_POLLING_WFS_LAYER + mLayerPayload.getLayername()));
    	receiverRegistered = true;
    	
		mPendingWFSLayerRequestIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		mAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 10, (mDataManager.getWFSDataRate() * 1000), mPendingWFSLayerRequestIntent);
		srReceiver.onReceive(mContext, intent);
	}
	
	protected class ParseFeaturesTask extends AsyncTask<ArrayList<UxoReportPayload>, Object, Integer> {

		@Override
		protected Integer doInBackground(@SuppressWarnings("unchecked") ArrayList<UxoReportPayload>... params) {
			
			for (UxoReportPayload feature: params[0]) {
				try {
					UxoReportPayload existingFeature = mLayerFeatures.get(feature.getFormId());
					
//					if(existingFeature != null) {
//						Date mdtDate = new Date(existingFeature.getCreatedUTC());
//					}
					
					if(existingFeature == null) {
						MarkupBaseShape featureShape = parseFeature(feature);
						mLayerFeatures.put(featureShape.getFeatureId(), feature);
						
						mMarkupShapes.add(featureShape);
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			
			return params[0].size();
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			Log.i("dcdsDataManager", "Rendering " + result + " feature(s) in WFS Layer " + mLayerPayload.getLayername());
			
		}
	}
	
	protected MarkupBaseShape parseFeature(final UxoReportPayload payload) {
		payload.parse();
		UxoReportData data = payload.getMessageData();
		
		JsonObject attr = new JsonObject();
		Resources resources = mContext.getResources();
		try {
			attr.addProperty("title", resources.getString(R.string.UXOREPORT));
			attr.addProperty("reportId", payload.getId());
			attr.addProperty("payload", payload.toJsonString());
			attr.addProperty("type", "ur");
			attr.addProperty("icon", R.drawable.e);
			attr.addProperty(resources.getString(R.string.markup_user), data.getUser());
			attr.addProperty(resources.getString(R.string.markup_timestamp), payload.getSeqTime());
			attr.addProperty(resources.getString(R.string.markup_message), data.getRecommendedPriority());			
		} catch (Exception e) {
		}
		
		LatLng coordinate = null;
		try {
			coordinate = new LatLng(Double.valueOf(data.getLatitude()), Double.valueOf(data.getLongitude()));
		} catch (Exception e) {
			coordinate = new LatLng(0, 0);
		}
		final MarkupSymbol symbol = new MarkupSymbol(mDataManager, mMap, attr.toString(), coordinate, generateTintedBitmap(R.drawable.e, new int[] { 20, 20, 20, 20 }), null, new int[] { 255, 255, 255, 255 });
		symbol.setFeatureId(String.valueOf(payload.getFormId()));
		
		return symbol;
	}

	public void unregister() {
		mAlarmManager.cancel(mPendingWFSLayerRequestIntent);
		if(receiverRegistered){
			mContext.unregisterReceiver(srReceiver);
			receiverRegistered = false;
		}
	}
}
