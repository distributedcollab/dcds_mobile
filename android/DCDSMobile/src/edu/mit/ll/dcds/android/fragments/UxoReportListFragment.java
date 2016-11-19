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
package edu.mit.ll.dcds.android.fragments;

import java.util.ArrayList;
import java.util.Comparator;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import edu.mit.ll.dcds.android.MainActivity;
import edu.mit.ll.dcds.android.R;
import edu.mit.ll.dcds.android.adapters.UxoReportListAdapter;
import edu.mit.ll.dcds.android.api.data.UxoReportData;
import edu.mit.ll.dcds.android.api.data.ReportSendStatus;
import edu.mit.ll.dcds.android.api.data.UxoReportFilterData;
import edu.mit.ll.dcds.android.api.payload.forms.UxoReportPayload;
import edu.mit.ll.dcds.android.api.tasks.MarkAllReportsAsReadTask;
import edu.mit.ll.dcds.android.utils.Constants;
import edu.mit.ll.dcds.android.utils.EncryptedPreferences;
import edu.mit.ll.dcds.android.utils.FormType;
import edu.mit.ll.dcds.android.utils.Intents;
import edu.mit.ll.dcds.android.utils.Constants.NavigationOptions;

public class UxoReportListFragment extends FormListFragment {
	private Context mContext;
	private SharedPreferences settings;
	private UxoReportListAdapter mUxoReportListAdapter;
	private boolean uxoReportReceiverRegistered;	
	private IntentFilter mUxoReportReceiverFilter;
	private IntentFilter mUxoReportProgressReceiverFilter;
	private IntentFilter mIncidentSwitchedReceiverFilter;
	private IntentFilter mMarkAllAsReadReceiverFilter;
	private IntentFilter mSentReportsClearedFilter;
	private long mLastIncidentId = 0;
	protected boolean mIsFirstLoad = true;
	protected MarkAllReportsAsReadTask MarkMessagesAsReadTask = null;
	
	private int index;
	private int top;
	private int longPressSelectionPosition;
	
	boolean isListFiltered = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mContext = getActivity();
		settings = mContext.getSharedPreferences(Constants.PREFERENCES_NAME, Constants.PREFERENCES_MODE);
		
		mUxoReportReceiverFilter = new IntentFilter(Intents.dcds_NEW_UXO_REPORT_RECEIVED);
		mUxoReportProgressReceiverFilter = new IntentFilter(Intents.dcds_UXO_REPORT_PROGRESS);
		mIncidentSwitchedReceiverFilter = new IntentFilter(Intents.dcds_INCIDENT_SWITCHED);	
		mMarkAllAsReadReceiverFilter = new IntentFilter(Intents.dcds_MARKING_ALL_REPORTS_READ_FINISHED);
		mSentReportsClearedFilter = new IntentFilter(Intents.dcds_SENT_UXO_REPORTS_CLEARED);
		
		if(!uxoReportReceiverRegistered) {
			mContext.registerReceiver(uxoReportReceiver, mUxoReportReceiverFilter);
			mContext.registerReceiver(uxoReportProgressReceiver, mUxoReportProgressReceiverFilter);
			mContext.registerReceiver(incidentChangedReceiver, mIncidentSwitchedReceiverFilter);
			mContext.registerReceiver(markAllAsReadReceiver, mMarkAllAsReadReceiverFilter);
			mContext.registerReceiver(sentReportsClearedReceiver, mSentReportsClearedFilter);
			uxoReportReceiverRegistered = true;
		}
		
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		
		inflater.inflate(R.menu.uxoreport, menu);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		view.setPadding(10, 0, 10, 0);

		this.setEmptyText(getString(R.string.no_uxo_reports_exist));
		super.onViewCreated(view, savedInstanceState);
		
		   getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

			      @Override
			      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

			    	longPressSelectionPosition = position;
		    	  	AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder((MainActivity) getActivity());
		    	  
					mDialogBuilder.setMessage(null);
				    mDialogBuilder.setPositiveButton(null, null);
				    
				    String[] choices = {getString(R.string.go_to_report_on_map)};
				    
					mDialogBuilder.setItems(choices,dialogSelected);
					
					mDialogBuilder.create();
					mDialogBuilder.show();
			    	  
			        return true;
			      }
			    });
	}
	
	DialogInterface.OnClickListener dialogSelected = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {

			if(UxoReportFilterData.isFilterEnabled() && which == 0){
			}else{
				MainActivity mMainActivity = (MainActivity) getActivity();
				UxoReportPayload item = mUxoReportListAdapter.getItem(longPressSelectionPosition);
				String cameraPos = item.getMessageData().getLatitude() + "," + item.getMessageData().getLongitude() + "," + 13 + "," + 0 + "," + 0;
				EncryptedPreferences settings = new EncryptedPreferences(mContext.getSharedPreferences(Constants.dcds_MAP_MARKUP_STATE, 0));
				settings.savePreferenceString(Constants.dcds_MAP_PREVIOUS_CAMERA, cameraPos);
				
				mMainActivity.onNavigationItemSelected(NavigationOptions.MAPCOLLABORATION.getValue(), -1);
			}
			
		}
	};

	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);
		
		if(UxoReportFilterData.isFilterEnabled() && position == 0){
			((MainActivity)mContext).onNavigationItemSelected(NavigationOptions.UXOFILTER.getValue(), -1);
		}else{
			UxoReportPayload item = mUxoReportListAdapter.getItem(position);
			item.parse();
			((MainActivity)mContext).openUxoReport(item, item.isDraft());	
			
			if(item.isNew()){
				item.setNew(false);
				mDataManager.deleteUxoReportFromHistory(item.getId());
				mDataManager.addUxoReportToHistory(item);
				
				ImageView blueDot = (ImageView)view.findViewById(R.id.uxoBlueDotImage);
				blueDot.setVisibility(View.INVISIBLE);
			}
		}
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		mDataManager.setNewReportAvailable(false);
		
		if(mUxoReportListAdapter == null) {
			mUxoReportListAdapter = new UxoReportListAdapter(mContext, R.layout.listitem_uxoreport, R.id.uxoReportTitle, new ArrayList<UxoReportPayload>());
			mIsFirstLoad = true;
		}
		
		if(!uxoReportReceiverRegistered) {
			mContext.registerReceiver(uxoReportReceiver, mUxoReportReceiverFilter);
			mContext.registerReceiver(uxoReportProgressReceiver, mUxoReportProgressReceiverFilter);
			mContext.registerReceiver(incidentChangedReceiver, mIncidentSwitchedReceiverFilter);
			mContext.registerReceiver(markAllAsReadReceiver, mMarkAllAsReadReceiverFilter);
			mContext.registerReceiver(sentReportsClearedReceiver, mSentReportsClearedFilter);
			uxoReportReceiverRegistered = true;
		}
		
		index = settings.getInt("uxorpt_scrollIdx", -1);
		top = settings.getInt("uxorpt_scrollTop", -1);
		
		Editor e = settings.edit();
		e.remove("uxorpt_scrollIdx");
		e.remove("uxorpt_scrollTop");
		e.commit();
		
		if(mLastIncidentId != mDataManager.getActiveIncidentId()) {
			mIsFirstLoad = true;
			setListShown(false);
		}
		
		mDataManager.sendUxoReports();
		updateData();
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		
		if(uxoReportReceiverRegistered) {
			mContext.unregisterReceiver(uxoReportReceiver);
			mContext.unregisterReceiver(uxoReportProgressReceiver);
			mContext.unregisterReceiver(incidentChangedReceiver);
			mContext.unregisterReceiver(markAllAsReadReceiver);
			mContext.unregisterReceiver(sentReportsClearedReceiver);
			uxoReportReceiverRegistered = false;
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		Editor e = settings.edit();
		
		int index = getListView().getFirstVisiblePosition();
		View v = getListView().getChildAt(0);
		int top = (v == null) ? 0 : v.getTop();

		e.putInt("uxorpt_scrollIdx", index);
		e.putInt("uxorpt_scrollTop", top);
		
		e.commit();
	}
	
	private BroadcastReceiver uxoReportReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				Bundle bundle = intent.getExtras();
				UxoReportPayload payload = mBuilder.create().fromJson(bundle.getString("payload"), UxoReportPayload.class);
				payload.setSendStatus(ReportSendStatus.lookUp(bundle.getInt("sendStatus", 0)));
				payload.parse();
				
//				UxoReportData data = payload.getMessageData();
				
//				if(data.getUser().equals(mDataManager.getUsername()) && payload.getSeqTime() >= mDataManager.getLastUxoReportTimestamp() - 10000) {
//					mUxoReportListAdapter.clear();
//					isListFiltered = false;
//					
//					ArrayList<UxoReportPayload> ReportListForAdapter = new ArrayList<UxoReportPayload>();
//					ReportListForAdapter.addAll(getFilteredList(mDataManager.getUxoReportHistoryForIncident(mDataManager.getActiveIncidentId())));
//					ReportListForAdapter.addAll(getFilteredList(mDataManager.getAllUxoReportStoreAndForwardReadyToSend(mDataManager.getActiveIncidentId())));
//					
//					UxoReportFilterData.setFilterEnabled(isListFiltered);
//					if(isListFiltered){
//						UxoReportPayload emptyPayload = new UxoReportPayload();
//						emptyPayload.setSeqTime(System.currentTimeMillis() + 120 * 1000);	//make sure filter description payload shows up above the most recent report
//						ReportListForAdapter.add(0,emptyPayload);
//					}
//					
//					mUxoReportListAdapter.addAll(ReportListForAdapter);
//					mUxoReportListAdapter.sort(reportComparator);
//				} else {
					mUxoReportListAdapter.add(payload);
//				}
				mUxoReportListAdapter.sort(reportComparator);
				mUxoReportListAdapter.notifyDataSetChanged();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
	private BroadcastReceiver uxoReportProgressReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				Bundle bundle = intent.getExtras();
				long reportId = bundle.getLong("reportId");
				double progress = bundle.getDouble("progress");
				boolean failed = bundle.getBoolean("failed");

				for (UxoReportPayload payload : mUxoReportListAdapter.getItems()) {
					if (payload.getId() == reportId) {
						payload.setProgress((int) Math.round(progress));
						payload.setFailedToSend(failed);
					}
				}

				if(mUxoReportListAdapter != null) {
					mUxoReportListAdapter.notifyDataSetChanged();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
	private BroadcastReceiver incidentChangedReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			updateData();
		}
	};
	
	protected void updateData() {
		long currentIncidentId;

		currentIncidentId = mDataManager.getActiveIncidentId();
		mDataManager.requestUxoReports();
		
		mUxoReportListAdapter.clear();
		isListFiltered = false;
		
		ArrayList<UxoReportPayload> ReportListForAdapter = new ArrayList<UxoReportPayload>();
		ReportListForAdapter.addAll(getFilteredList(mDataManager.getUxoReportHistoryForIncident(currentIncidentId)));
		ReportListForAdapter.addAll(getFilteredList(mDataManager.getAllUxoReportStoreAndForwardReadyToSend(currentIncidentId)));
		ReportListForAdapter.addAll(getFilteredList(mDataManager.getAllUxoReportStoreAndForwardHasSent(currentIncidentId)));
		
		UxoReportFilterData.setFilterEnabled(isListFiltered);
		if(isListFiltered){
			UxoReportPayload emptyPayload = new UxoReportPayload();
			emptyPayload.setSeqTime(System.currentTimeMillis() + 120 * 1000);	//make sure filter description payload shows up above the most recent report
			ReportListForAdapter.add(0,emptyPayload);
		}
		
		mUxoReportListAdapter.addAll(ReportListForAdapter);
		mUxoReportListAdapter.sort(reportComparator);
			
		if(mIsFirstLoad) {
			setListAdapter(mUxoReportListAdapter);
			mLastIncidentId = currentIncidentId;
			mIsFirstLoad = false;
			setListShown(true);
		}
		
		mUxoReportListAdapter.notifyDataSetChanged();
		if(index != -1 && top != -1) {
			getListView().post(new Runnable() {
				
				@Override
				public void run() {
				try{
					getListView().setSelectionFromTop(index, top);
					index = -1;
					top = -1;
				}catch(IllegalStateException e){
					Log.e("UxoReportListFragment",e.toString());
					e.printStackTrace();
				}
				}
			});
		}
	}
	
	private ArrayList<UxoReportPayload> getFilteredList(ArrayList<UxoReportPayload> list){
		
		for(int i = 0; i < list.size(); i++){
			boolean itemRemoved = false;
			UxoReportPayload payload = list.get(i);
			UxoReportData data = payload.getMessageData();
			
			if(UxoReportFilterData.getUxoTypeCurrentFilter() != getString(R.string.all) && itemRemoved == false){
				isListFiltered = true;
				if(!data.getUxoType().toString().equals(UxoReportFilterData.getUxoTypeCurrentFilter())){
					list.remove(i);
					i--;
					itemRemoved = true;
				}
			}
			
			if(UxoReportFilterData.getUxoPriorityCurrentFilter() != getString(R.string.all) && itemRemoved == false){
				isListFiltered = true;
				if(!data.getRecommendedPriority().toString().equals(UxoReportFilterData.getUxoPriorityCurrentFilter())){
					list.remove(i);
					i--;
					itemRemoved = true;
				}
			}
			
			if(UxoReportFilterData.getUxoUnitCurrentFilter() != getString(R.string.all) && itemRemoved == false){
				isListFiltered = true;
				if(!data.getReportingUnit().equals(UxoReportFilterData.getUxoUnitCurrentFilter())){
					list.remove(i);
					i--;
					itemRemoved = true;
				}
			}
			
			if(UxoReportFilterData.getUxoStatusCurrentFilter() != getString(R.string.all) && itemRemoved == false){
				isListFiltered = true;
				if(!data.getStatus().equals(UxoReportFilterData.getUxoStatusCurrentFilter())){
					list.remove(i);
					i--;
					itemRemoved = true;
				}
			}	
			
			if(UxoReportFilterData.isFilterByDateRange() && itemRemoved == false){
				isListFiltered = true;
				if(payload.getSeqTime() < UxoReportFilterData.getUxoFromDateCurrentFilter() ){
					list.remove(i);
					i--;
					itemRemoved = true;
				}
			}
			
			if(UxoReportFilterData.isFilterByDateRange() && itemRemoved == false){
				isListFiltered = true;
				if(payload.getSeqTime() > UxoReportFilterData.getUxoToDateCurrentFilter() ){
					list.remove(i);
					i--;
					itemRemoved = true;
				}
			}
		}
		
		return list;
	}
	
	private BroadcastReceiver sentReportsClearedReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			long reportId = intent.getExtras().getLong("reportId");
			for(int i = 0; i < mUxoReportListAdapter.getCount() ; i++){
				UxoReportPayload payload = mUxoReportListAdapter.getItem(i);
				if(payload.getFormId() == reportId && payload.isNew() == false){
					mUxoReportListAdapter.remove(payload);
					mUxoReportListAdapter.notifyDataSetChanged();
					return;
				}
			}	
		}
	};
	
	private Comparator<? super UxoReportPayload> reportComparator = new Comparator<UxoReportPayload>() {
		
		@Override
		public int compare(UxoReportPayload lhs, UxoReportPayload rhs) {
			return (Long.valueOf(rhs.getSeqTime()).compareTo(Long.valueOf(lhs.getSeqTime())));
		}
	};
	
	@Override
	protected boolean itemIsDraft(int position) {
		UxoReportPayload item = mUxoReportListAdapter.getItem(position);
		
		return item.isDraft();
	}

	@Override
	protected boolean handleItemDeletion(int position) {
		UxoReportPayload item = mUxoReportListAdapter.getItem(position);
		mUxoReportListAdapter.remove(item);
		mUxoReportListAdapter.notifyDataSetChanged();
		
		return mDataManager.deleteUxoReportStoreAndForward(item.getId());
	}
	
	public void MarkAllMessagesAsRead()
	{		
		if(MarkMessagesAsReadTask == null){
			MarkMessagesAsReadTask = new MarkAllReportsAsReadTask(mContext);
			MarkMessagesAsReadTask.execute(FormType.UXO);
		}
	}
	
	private BroadcastReceiver markAllAsReadReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			MarkMessagesAsReadTask = null;
			updateData();	
		}
	};
	
	public int getReportCount(){
		return mUxoReportListAdapter.getCount();
	}
}
