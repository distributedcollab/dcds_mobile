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
import edu.mit.ll.dcds.android.api.data.SimpleReportData;
import edu.mit.ll.dcds.android.api.payload.TrackingLayerPayload;
import edu.mit.ll.dcds.android.api.payload.forms.SimpleReportPayload;
import edu.mit.ll.dcds.android.maps.markup.MarkupBaseShape;
import edu.mit.ll.dcds.android.maps.markup.MarkupSymbol;
import edu.mit.ll.dcds.android.utils.Intents;

public class SimpleReportLayer extends MarkupLayer {
	
	protected HashMap<String, SimpleReportPayload> mLayerFeatures;	
	protected AsyncTask<ArrayList<SimpleReportPayload>, Object, Integer> mParseFeaturesTask;
	private BroadcastReceiver srReceiver;
	private boolean receiverRegistered = false;
	
	public SimpleReportLayer(Context context, TrackingLayerPayload layerPayload, GoogleMap map) {
		super(context, layerPayload, map);
		mLayerFeatures = new HashMap<String, SimpleReportPayload>();
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
						
						ArrayList<SimpleReportPayload> simpleReports = mDataManager.getSimpleReportHistoryForIncident(mDataManager.getActiveIncidentId());

						clearFromMap();
						mParseFeaturesTask = new ParseFeaturesTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, simpleReports);
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
	
	protected class ParseFeaturesTask extends AsyncTask<ArrayList<SimpleReportPayload>, Object, Integer> {

		@Override
		protected Integer doInBackground(@SuppressWarnings("unchecked") ArrayList<SimpleReportPayload>... params) {
			
			for (SimpleReportPayload feature: params[0]) {
				try {
					SimpleReportPayload existingFeature = mLayerFeatures.get(feature.getFormId());
					
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
	
	protected MarkupBaseShape parseFeature(final SimpleReportPayload payload) {
		payload.parse();
		SimpleReportData data = payload.getMessageData();
		
		JsonObject attr = new JsonObject();
		Resources resources = mContext.getResources();
		try {
			attr.addProperty("title", resources.getString(R.string.GENERALMESSAGE));
			attr.addProperty("reportId", payload.getId());
			attr.addProperty("payload", payload.toJsonString());
			attr.addProperty("type", "sr");
			attr.addProperty("icon", getSRBitmap(data.getCategory()));
			attr.addProperty(resources.getString(R.string.markup_user), data.getUser());
			attr.addProperty(resources.getString(R.string.markup_timestamp), payload.getSeqTime());
			attr.addProperty(resources.getString(R.string.markup_message), data.getDescription());
			if(data.getAssign() != null && !data.getAssign().isEmpty()) {
				attr.addProperty(resources.getString(R.string.markup_assign), data.getAssign());
			}
		} catch (Exception e) {
		}
		
		final MarkupSymbol symbol = new MarkupSymbol(mDataManager, mMap, attr.toString(), new LatLng(data.getLatitude(), data.getLongitude()), generateTintedBitmap(getSRBitmap(data.getCategory()), new int[] { 20, 20, 20, 20 }), null, new int[] { 255, 255, 255, 255 });
		symbol.setFeatureId(String.valueOf(payload.getFormId()));
		
		return symbol;
	}
	
	private int getSRBitmap(String type) {
		
		Resources resources = mContext.getResources();
		
		if(type.equals(resources.getString(R.string.sr_categorytype_other))){
			return R.drawable.message;
		}else if(type.equals(resources.getString(R.string.sr_categorytype_incident_commander))){
			return R.drawable.incident_commander;
		}else if(type.equals(resources.getString(R.string.sr_categorytype_public_information_officer))){
			return R.drawable.public_information_officer;
		}else if(type.equals(resources.getString(R.string.sr_categorytype_liason_officer))){
			return R.drawable.liaison_officer;
		}else if(type.equals(resources.getString(R.string.sr_categorytype_agency_representative))){
			return R.drawable.agency_representative;
		}else if(type.equals(resources.getString(R.string.sr_categorytype_safety_officer))){
			return R.drawable.safety_officer;
		}else if(type.equals(resources.getString(R.string.sr_categorytype_operations_section))){
			return R.drawable.operations;
		}else if(type.equals(resources.getString(R.string.sr_categorytype_plans_section))){
			return R.drawable.planning;
		}else if(type.equals(resources.getString(R.string.sr_categorytype_suppression_repair))){
			return R.drawable.suppression_repair;
		}else if(type.equals(resources.getString(R.string.sr_categorytype_finance_section))){
			return R.drawable.finance_section;
		}else if(type.equals(resources.getString(R.string.sr_categorytype_comp_claim_damage_advisory))){
			return R.drawable.damage_advisory;
		}else if(type.equals(resources.getString(R.string.sr_categorytype_logistics_section))){
			return R.drawable.logistics_section;
		}else if(type.equals(resources.getString(R.string.sr_categorytype_ground_support_request))){
			return R.drawable.ground_support;
		}else if(type.equals(resources.getString(R.string.sr_categorytype_none))){
			return R.drawable.none;
		}else{
			return R.drawable.none;
		}
	}

	public void unregister() {
		mAlarmManager.cancel(mPendingWFSLayerRequestIntent);
		if(receiverRegistered){
			mContext.unregisterReceiver(srReceiver);
			receiverRegistered = false;
		}
	}
}
