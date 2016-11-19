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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import edu.mit.ll.dcds.android.MainActivity;
import edu.mit.ll.dcds.android.R;
import edu.mit.ll.dcds.android.api.DataManager;
import edu.mit.ll.dcds.android.api.RestClient;
import edu.mit.ll.dcds.android.api.messages.CollaborationRoomMessage;
import edu.mit.ll.dcds.android.api.payload.CollabroomPayload;
import edu.mit.ll.dcds.android.api.payload.IncidentPayload;
import edu.mit.ll.dcds.android.utils.Intents;
import edu.mit.ll.dcds.android.utils.Constants.NavigationOptions;
import edu.mit.ll.dcds.android.utils.MyOrgCapabilities;

public class OverviewFragment extends Fragment {
	
	private View mRootView;
	private MainActivity mMainActivity;
	private DataManager mDataManager;
	
	private ImageButton mGeneralMessageButton;
	private LinearLayout mGeneralMessageButtonLayout;
	private ImageButton mReportButton;
	private LinearLayout mReportButtonLayout;
	private ImageButton mChatButton;
	private ImageButton mMapButton;
	private LinearLayout mChatButtonLayout;
	
	private ImageView mNewGeneralMessageImageView;
	private ImageView mNewReportImageView;
	private ImageView mNewChatImageView;
	private ImageView mNewMapImageView;
	
	private Button mJoinIncidentButton;
	private Button mJoinRoomButton;
	private FrameLayout mJoinRoomButtonLayout;
	
	private ProgressBar mRoomsLoadingProgressBar;
	
	private AlertDialog.Builder mDialogBuilder;
	private AlertDialog mIncidentPopupMenu;
	private AlertDialog mRoomPopupMenu;
	private AlertDialog mReportPopupMenu;

	private View mIncidentFrameLayout;
	private View mRoomFrameLayout;
	
	private String[] incidentArray;
	private String[] roomNameDialogArray;
	
	private LinearLayout mIncidentFrameButtonLayout;
	private LinearLayout mRoomFrameButtonLayout;

	List<String> activeReports = new ArrayList<String>();
	Resources resources;
	
	private boolean collabroomsReceiverRegistered = false;
	private IntentFilter mCollabroomsBeganPolling;
	private IntentFilter mCollabroomsSuccessReceiverFilter;
	private IntentFilter mCollabroomsFailReceiverFilter;
	private IntentFilter mSimpleReportReceivedFilter;
	private IntentFilter mChatReceivedFilter;
	private IntentFilter mMapReceivedFilter;
	private IntentFilter mUxoReportReceivedFilter;
	private IntentFilter mDamageReportReceivedFilter;
	private IntentFilter mWeatherReportReceivedFilter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mMainActivity = (MainActivity) getActivity();
		mDataManager = DataManager.getInstance(mMainActivity);
		resources = mMainActivity.getResources();
		
		mCollabroomsBeganPolling = new IntentFilter(Intents.dcds_POLLING_COLLABROOMS);
		mCollabroomsSuccessReceiverFilter = new IntentFilter(Intents.dcds_SUCCESSFULLY_GET_COLLABROOMS);
		mCollabroomsFailReceiverFilter = new IntentFilter(Intents.dcds_FAILED_GET_COLLABROOMS);
		mSimpleReportReceivedFilter = new IntentFilter(Intents.dcds_NEW_SIMPLE_REPORT_RECEIVED);
		mChatReceivedFilter = new IntentFilter(Intents.dcds_LAST_CHAT_RECEIVED);
		mMapReceivedFilter = new IntentFilter(Intents.dcds_NEW_MARKUP_RECEIVED);
		
		mUxoReportReceivedFilter = new IntentFilter(Intents.dcds_NEW_UXO_REPORT_RECEIVED);
		mDamageReportReceivedFilter = new IntentFilter(Intents.dcds_NEW_DAMAGE_REPORT_RECEIVED);
		mWeatherReportReceivedFilter = new IntentFilter(Intents.dcds_NEW_WEATHER_REPORT_RECEIVED);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
		if(mDataManager.getTabletLayoutOn()){
			mRootView = inflater.inflate(R.layout.fragment_overview_tablet, container, false);
		}else{
			mRootView = inflater.inflate(R.layout.fragment_overview, container, false);
		}

		mDialogBuilder = new AlertDialog.Builder(mMainActivity);
		
		mJoinIncidentButton = (Button) mRootView.findViewById(R.id.joinIncidentButton);
		mJoinRoomButton = (Button) mRootView.findViewById(R.id.joinRoomButton);
		
		mJoinRoomButtonLayout = (FrameLayout) mRootView.findViewById(R.id.joinRoomButtonLayout);
		
		mGeneralMessageButton = (ImageButton) mRootView.findViewById(R.id.generalMessageButton);
		mGeneralMessageButtonLayout = (LinearLayout) mRootView.findViewById(R.id.generalMessageButtonLayout);
		mReportButton = (ImageButton) mRootView.findViewById(R.id.reportsButton);
		mReportButtonLayout = (LinearLayout) mRootView.findViewById(R.id.damageReportButtonLayout);
		mChatButton = (ImageButton) mRootView.findViewById(R.id.chatButton);
		
		mChatButtonLayout = (LinearLayout) mRootView.findViewById(R.id.chatButtonLayout);
		
		mIncidentFrameButtonLayout = (LinearLayout) mRootView.findViewById(R.id.incidentFrameButtonLayout);
		mRoomFrameButtonLayout = (LinearLayout) mRootView.findViewById(R.id.roomFrameButtonLayout);
		
		mRoomsLoadingProgressBar = (ProgressBar) mRootView.findViewById(R.id.roomsLoadingProgressBar);
		mRoomsLoadingProgressBar.setVisibility(View.INVISIBLE);
		
		mJoinIncidentButton.setOnClickListener(showIncidentPopupMenu);
		mJoinRoomButton.setOnClickListener(showRoomPopupMenu);
		
		mReportButton.setOnClickListener(showReportPopupMenu);
		mGeneralMessageButton.setOnClickListener(clickNavigationListener);
		mChatButton.setOnClickListener(clickNavigationListener);
		
		mIncidentFrameLayout = mRootView.findViewById(R.id.incidentFrameLayoutBorder);
		mRoomFrameLayout = mRootView.findViewById(R.id.roomFrameLayoutBorder);
		
		mMapButton = (ImageButton) mRootView.findViewById(R.id.mapButton);
		mMapButton.setOnClickListener(clickNavigationListener);
		
		mNewGeneralMessageImageView = (ImageView) mRootView.findViewById(R.id.generalMessageNotificationImage);
		mNewReportImageView = (ImageView) mRootView.findViewById(R.id.reportsNotificationImage);
		mNewChatImageView = (ImageView) mRootView.findViewById(R.id.chatNotificationImage);
		mNewMapImageView = (ImageView) mRootView.findViewById(R.id.mapNotificationImage);
		
		
		TextView orgTextView = (TextView) mRootView.findViewById(R.id.selectedOrg);
		
		if(mDataManager.getCurrentOrganziation() != null){
			String currentOrg = mDataManager.getCurrentOrganziation().getName();
			orgTextView.setText(mDataManager.getUserNickname() + " - " + currentOrg);
		}else{
			orgTextView.setText(mDataManager.getUserNickname() );
		}
		
		if(mDataManager.isNewGeneralMessageAvailable()){
			mNewGeneralMessageImageView.setVisibility(View.VISIBLE);
		}else{
			mNewGeneralMessageImageView.setVisibility(View.INVISIBLE);
		}
		if(mDataManager.isNewReportAvailable()){
			mNewReportImageView.setVisibility(View.VISIBLE);
		}else{
			mNewReportImageView.setVisibility(View.INVISIBLE);
		}
		if(mDataManager.isNewchatAvailable()){
			mNewChatImageView.setVisibility(View.VISIBLE);
		}else{
			mNewChatImageView.setVisibility(View.INVISIBLE);
		}
		if(mDataManager.isNewMapAvailable()){
			mNewMapImageView.setVisibility(View.VISIBLE);
		}else{
			mNewMapImageView.setVisibility(View.INVISIBLE);
		}
		
		setupActiveReportsFromOrgProfile();
		disableChatButton();
		
		if(!mDataManager.getActiveIncidentName().equals(getString(R.string.no_selection))) {
			mJoinIncidentButton.setText(getString(R.string.incident_active, mDataManager.getActiveIncidentName()));
		} else {
			mJoinIncidentButton.setText(getString(R.string.incident_join));
		}

		IncidentPayload activeIncident = null;
		
		if(mDataManager.getActiveIncidentName().equals(getString(R.string.no_selection))) {
			if(mIncidentFrameButtonLayout!=null){
				mIncidentFrameButtonLayout.setAlpha(0.3f);
			}else{
				disableGeneralMessageButton();
				mReportButtonLayout.setAlpha(0.3f);
			}
			
			if(mRoomFrameLayout!=null){
				mRoomFrameLayout.setAlpha(0.3f);
			}
			if(mRoomFrameButtonLayout!=null){
				mRoomFrameButtonLayout.setAlpha(1f);
			}
			
			mGeneralMessageButton.setClickable(false);
			mReportButton.setClickable(false);
			
			disableChatButton();
			
		} else {
			HashMap<String, IncidentPayload> incidents = mDataManager.getIncidents();
			
			disableGeneralMessageButton();	//way to toggle generalmessage for org capabilites
			enableGeneralMessageButton();
			
			if(incidents != null) {
				if((activeIncident = incidents.get(mDataManager.getActiveIncidentName())) != null) {
					ArrayList<CollabroomPayload> incidentRooms = activeIncident.getCollabrooms();
					Collections.sort(incidentRooms, new Comparator<CollabroomPayload>() {
		
						@Override
						public int compare(CollabroomPayload lhs, CollabroomPayload rhs) {
							return lhs.getName().compareTo(rhs.getName());
						}
					});
		
//					mDataManager.clearCollabRoomList();
//					for(CollabroomPayload room : incidentRooms) {
//						mDataManager.addCollabroom(room);
//					}
					mDataManager.requestDamageReportRepeating(mDataManager.getIncidentDataRate(), true);
					mDataManager.requestFieldReportRepeating(mDataManager.getIncidentDataRate(), true);
					mDataManager.requestSimpleReportRepeating(mDataManager.getIncidentDataRate(), true);
					mDataManager.requestResourceRequestRepeating(mDataManager.getIncidentDataRate(), true);
//					mDataManager.requestCatanRequestRepeating(mDataManager.getIncidentDataRate(), true);
//					mDataManager.requestUxoReportRepeating(mDataManager.getIncidentDataRate(), true);
					
				} else {
					RestClient.getAllIncidents(mDataManager.getUserId());
					mDialogBuilder.setTitle(R.string.incident_error_not_found);
					mDialogBuilder.setMessage(getString(R.string.incident_no_longer_exists, mDataManager.getActiveIncidentName()));
					mDialogBuilder.setItems(null, null);
					mDialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					mDialogBuilder.create().show();
					
					mDataManager.setCurrentIncidentData(null, -1, "");
					mDataManager.setSelectedCollabRoom(null);

					mJoinIncidentButton.setText(getString(R.string.incident_join));
				}
			}
		}

		if(activeIncident != null) {
			
			mDataManager.requestCollabrooms(activeIncident.getIncidentId());		
			
			ArrayList<CollabroomPayload> incidentRooms = activeIncident.getCollabrooms();
			Collections.sort(incidentRooms, new Comparator<CollabroomPayload>() {

				@Override
				public int compare(CollabroomPayload lhs, CollabroomPayload rhs) {
					return lhs.getName().compareTo(rhs.getName());
				}
			});
			
			CollabroomPayload defaultRoom = null;
			
			if(defaultRoom == null && incidentRooms.size() > 0) {
				defaultRoom = incidentRooms.get(0);
			}
			
			boolean noRoomSelected = mDataManager.getSelectedCollabRoom().getName().equals(getString(R.string.no_selection));
			mJoinRoomButton.setText(getString(R.string.room_join));
			
			if(noRoomSelected) {
				if(mMapButton!=null){
					mMapButton.setClickable(true);
				}
				disableChatButton();
				if(mRoomFrameLayout!=null){
					mRoomFrameLayout.setAlpha(0.3f);
				}
				if(mRoomFrameButtonLayout!=null){
					mRoomFrameButtonLayout.setAlpha(1f);
				}
				
			}else {
				mJoinRoomButton.setText(getString(R.string.room_active, mDataManager.getSelectedCollabRoom().getName().replace(mDataManager.getActiveIncidentName() + "-", "")));
				if(mMapButton != null){
					mMapButton.setClickable(true);
				}
				
				enableChatButton();
				if(mRoomFrameLayout != null){
					mRoomFrameLayout.setAlpha(1f);
				}
				if(mRoomFrameButtonLayout != null){
					mRoomFrameButtonLayout.setAlpha(1f);
				}
				
				mDataManager.requestMarkupRepeating(mDataManager.getCollabroomDataRate(), true);
				mDataManager.requestChatMessagesRepeating(mDataManager.getCollabroomDataRate(), true);
			}
		} else {
			
			mJoinRoomButton.setClickable(false);
			mJoinRoomButtonLayout.setAlpha(0.3f);
//			mJoinRoomButton.setAlpha(0.3f);
			if(mIncidentFrameLayout != null){
				mIncidentFrameLayout.setAlpha(0.0f);
			}
			if(mRoomFrameLayout != null){
				mRoomFrameLayout.setAlpha(0.0f);
			}
			if(mRoomFrameButtonLayout != null){
				mRoomFrameButtonLayout.setAlpha(1f);
			}
			
			mJoinRoomButton.setText(getString(R.string.room_join));
		}
		
		setHasOptionsMenu(true);
		
		return mRootView;
	}
	
	private void setupActiveReportsFromOrgProfile()
	{	
	    MyOrgCapabilities myCaps = mDataManager.getMyOrgCapabilities();

		activeReports.clear();
		
		if(myCaps.isDMGRPT()){activeReports.add( resources.getString(R.string.DAMAGESURVEY));}
		if(myCaps.isFR()){activeReports.add( resources.getString(R.string.FIELDREPORT));}
		if(myCaps.isRESREQ()){activeReports.add( resources.getString(R.string.RESOURCEREQUEST));}
		if(myCaps.isWR()){activeReports.add( resources.getString(R.string.WEATHERREPORT));}
		if(myCaps.isUXO()){activeReports.add( resources.getString(R.string.UXOREPORT));}
	}
	
    @Override
	public void onResume() {
        super.onResume();
    
		if(!collabroomsReceiverRegistered) {
			mDataManager.getContext().registerReceiver(CollabroomPollingReceiver, mCollabroomsBeganPolling);
			mDataManager.getContext().registerReceiver(CollabroomReceiver, mCollabroomsSuccessReceiverFilter);
			mDataManager.getContext().registerReceiver(CollabroomReceiver, mCollabroomsFailReceiverFilter);
			mDataManager.getContext().registerReceiver(simpleReportReceived, mSimpleReportReceivedFilter);
			mDataManager.getContext().registerReceiver(chatReceived, mChatReceivedFilter);
			mDataManager.getContext().registerReceiver(mapReceived, mMapReceivedFilter);
			
			mDataManager.getContext().registerReceiver(reportReceived, mUxoReportReceivedFilter);
			mDataManager.getContext().registerReceiver(reportReceived, mDamageReportReceivedFilter);
			mDataManager.getContext().registerReceiver(reportReceived, mWeatherReportReceivedFilter);
			
			collabroomsReceiverRegistered = true;
		}
        
		RestClient.getAllIncidents(mDataManager.getUserId());
		
        mDataManager.sendChatMessages();
        mDataManager.sendFieldReports();
        mDataManager.sendDamageReports();
        mDataManager.sendMarkupFeatures();
        mDataManager.sendResourceRequests();
        mDataManager.sendSimpleReports();
        mDataManager.sendUxoReports();
    }
    
	@Override
	public void onPause() {
		super.onPause();

		if(mIncidentPopupMenu != null) {
			mIncidentPopupMenu.dismiss();
		}
		
		if(mRoomPopupMenu != null) {
			mRoomPopupMenu.dismiss();
		}
		
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		
		try{
			if(collabroomsReceiverRegistered) {
				mDataManager.getContext().unregisterReceiver(CollabroomPollingReceiver);
				mDataManager.getContext().unregisterReceiver(CollabroomReceiver);
				mDataManager.getContext().unregisterReceiver(simpleReportReceived);
				mDataManager.getContext().unregisterReceiver(chatReceived);
				mDataManager.getContext().unregisterReceiver(mapReceived);	
				collabroomsReceiverRegistered = false;
			}
		}catch(IllegalArgumentException exception){
			exception.printStackTrace();
			collabroomsReceiverRegistered = false;
		}
		
		((ViewGroup)mRootView.getParent()).removeView(mRootView);
	}
	
	OnClickListener clickNavigationListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int navigationId = NavigationOptions.OVERVIEW.getValue();
			int viewId = v.getId();
			
			if(viewId == R.id.generalMessageButton) {
				navigationId = NavigationOptions.GENERALMESSAGE.getValue();
				mNewGeneralMessageImageView.setVisibility(View.INVISIBLE);
			} else if(viewId == R.id.chatButton) {
				navigationId = NavigationOptions.CHATLOG.getValue();
				mNewChatImageView.setVisibility(View.INVISIBLE);
			} else if(viewId == R.id.mapButton) {
				navigationId = NavigationOptions.MAPCOLLABORATION.getValue();
				mNewMapImageView.setVisibility(View.INVISIBLE);
			}
			mMainActivity.onNavigationItemSelected(navigationId, -1);
		}
		
	};
	
	OnClickListener showIncidentPopupMenu = new OnClickListener() {

		@Override
		public void onClick(View v) {
			mDialogBuilder.setTitle(R.string.select_an_incident);
			mDialogBuilder.setMessage(null);
		    mDialogBuilder.setPositiveButton(null, null);
		    
		    HashMap<String, IncidentPayload> incidentsMap = mDataManager.getIncidents();
		    if(incidentsMap == null){
		    	incidentsMap = new HashMap<String, IncidentPayload>();
		    }
		    
		    incidentArray = new String[incidentsMap.size()];
		    incidentsMap.keySet().toArray(incidentArray);
		    Arrays.sort(incidentArray);
			mDialogBuilder.setItems(incidentArray, incidentSelected);
			mIncidentPopupMenu = mDialogBuilder.create();
			mIncidentPopupMenu.show();
		}
	};
	
	DialogInterface.OnClickListener incidentSelected = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			
			mGeneralMessageButton.setClickable(true);
			mReportButton.setClickable(true);
			
			if(mIncidentFrameLayout != null){
				mIncidentFrameLayout.setAlpha(1f);
			}
			if(mIncidentFrameButtonLayout != null){
				mIncidentFrameButtonLayout.setAlpha(1f);
			}else{
				enableGeneralMessageButton();
			}
				
			mJoinRoomButtonLayout.setAlpha(1f);
			mJoinRoomButton.setClickable(true);
			
			IncidentPayload currentIncident = mDataManager.getIncidents().get(incidentArray[which]);
			
			mDataManager.requestCollabrooms(currentIncident.getIncidentId());		
			
			ArrayList<CollabroomPayload> incidentRooms = currentIncident.getCollabrooms();
			Collections.sort(incidentRooms, new Comparator<CollabroomPayload>() {

				@Override
				public int compare(CollabroomPayload lhs, CollabroomPayload rhs) {
					return lhs.getName().compareTo(rhs.getName());
				}
			});
			
			CollabroomPayload defaultRoom = null;
			
			if(defaultRoom == null && incidentRooms.size() > 0) {
				defaultRoom = incidentRooms.get(0);
			}
			
			mDataManager.setCurrentIncidentData(currentIncident, -1, "");	
			mDataManager.setSelectedCollabRoom(null);

			mDataManager.stopPollingChat();
			mDataManager.stopPollingMarkup();
			mDataManager.requestFieldReportRepeating(mDataManager.getIncidentDataRate(), true);
			mDataManager.requestDamageReportRepeating(mDataManager.getIncidentDataRate(), true);
			mDataManager.requestSimpleReportRepeating(mDataManager.getIncidentDataRate(), true);
			mDataManager.requestResourceRequestRepeating(mDataManager.getIncidentDataRate(), true);
			mDataManager.requestUxoReportRepeating(mDataManager.getIncidentDataRate(), true);
			
			mJoinIncidentButton.setText(getString(R.string.incident_active, mDataManager.getActiveIncidentName()));
			mJoinRoomButton.setText(getString(R.string.room_join));
			
			if(mMapButton != null){
				mMapButton.setClickable(true);
			}
			disableChatButton();
			if(mRoomFrameButtonLayout != null){
				mRoomFrameButtonLayout.setAlpha(1f);
			}
			if(mRoomFrameLayout != null){
				mRoomFrameLayout.setAlpha(0.3f);
			}
			
	        Intent intent = new Intent();
	        intent.setAction(Intents.dcds_INCIDENT_SWITCHED);
	        mDataManager.getContext().sendBroadcast(intent);
		}
	};
	
	OnClickListener showReportPopupMenu = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			mDialogBuilder.setTitle(R.string.select_report_type);
			mDialogBuilder.setMessage(null);
		    mDialogBuilder.setPositiveButton(null, null);
		    
		    for(int i = 0; i < activeReports.size(); i++) {
		    	activeReports.set(i, activeReports.get(i).replace(mDataManager.getActiveIncidentName() + "-", ""));
		    }
		    
		    if(activeReports.size() == 0)
		    {
		    	mDataManager.requestOrgCapabilitiesUpdate();
		    	activeReports.add(getString(R.string.reports_not_available_for_your_organization));
		    }

			mDialogBuilder.setItems(activeReports.toArray(new String[activeReports.size()]), reportSelected);
			mReportPopupMenu = mDialogBuilder.create();
			
		    mReportPopupMenu.show();
		}
	};
	
	DialogInterface.OnClickListener reportSelected = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			if(activeReports.get(which).equals( resources.getString(R.string.DAMAGESURVEY)) ){
				mMainActivity.onNavigationItemSelected(NavigationOptions.DAMAGESURVEY.getValue(), -1);
			}
			else if(activeReports.get(which).equals( resources.getString(R.string.FIELDREPORT)) ){
				mMainActivity.onNavigationItemSelected(NavigationOptions.FIELDREPORT.getValue(), -1);
			}
			else if(activeReports.get(which).equals( resources.getString(R.string.RESOURCEREQUEST)) ){
				mMainActivity.onNavigationItemSelected(NavigationOptions.RESOURCEREQUEST.getValue(), -1);
			}
			else if(activeReports.get(which).equals( resources.getString(R.string.WEATHERREPORT)) ){
				mMainActivity.onNavigationItemSelected(NavigationOptions.WEATHERREPORT.getValue(), -1);
			}
			else if(activeReports.get(which).equals( resources.getString(R.string.UXOREPORT)) ){
				mMainActivity.onNavigationItemSelected(NavigationOptions.UXOREPORT.getValue(), -1);
			}
			mNewReportImageView.setVisibility(View.INVISIBLE);
		}
	};
	
	OnClickListener showRoomPopupMenu = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			mDialogBuilder.setTitle(R.string.select_a_room);
			mDialogBuilder.setMessage(null);
		    mDialogBuilder.setPositiveButton(null, null);
		    CollaborationRoomMessage rooms = mDataManager.getCollabRoomsForIncident(mDataManager.getActiveIncidentId());
		    
		    if(rooms == null){
		    	mDialogBuilder.setMessage(R.string.no_rooms_accessible);
		    	mDialogBuilder.setItems(null, null);
				mRoomPopupMenu = mDialogBuilder.create();
			    mRoomPopupMenu.show();
		    	return;
		    }
		    
		    roomNameDialogArray = new String[rooms.getresults().size()];

		    for(int i = 0; i < rooms.getresults().size();i++){
		    	roomNameDialogArray[i] = rooms.getresults().get(i).getName();
		    }
		    
	    	mDialogBuilder.setMessage(null);
	    	mDialogBuilder.setItems(roomNameDialogArray, roomSelected);
		    
			mRoomPopupMenu = mDialogBuilder.create();
			
		    mRoomPopupMenu.show();
		}
	};
	
	DialogInterface.OnClickListener roomSelected = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {

			CollaborationRoomMessage rooms = mDataManager.getCollabRoomsForIncident(mDataManager.getActiveIncidentId());
			
			CollabroomPayload currentRoom = rooms.getresults().get(which);

			mDataManager.stopPollingChat();
			mDataManager.stopPollingMarkup();
			mDataManager.setSelectedCollabRoom(currentRoom);
			mDataManager.requestMarkupRepeating(mDataManager.getCollabroomDataRate(), true);

			mJoinRoomButton.setText(getString(R.string.room_active, mDataManager.getSelectedCollabRoom().getName().replace(mDataManager.getActiveIncidentName() + "-", "")));

			if(mRoomFrameLayout != null){
				mRoomFrameLayout.setAlpha(1.0f);
			}
			if(mRoomFrameButtonLayout != null){
				mRoomFrameButtonLayout.setAlpha(1.0f);
			}
			
			if(mMapButton != null){
				mMapButton.setClickable(true);
			}
			
			enableChatButton();
			
	        Intent intent = new Intent();
	        intent.setAction(Intents.dcds_COLLABROOM_SWITCHED);
	        mDataManager.getContext().sendBroadcast(intent);
		}
	};
	
	private BroadcastReceiver CollabroomReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				mRoomsLoadingProgressBar.setVisibility(View.INVISIBLE);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	private BroadcastReceiver CollabroomPollingReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				mRoomsLoadingProgressBar.setVisibility(View.VISIBLE);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
	private BroadcastReceiver simpleReportReceived = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			mNewGeneralMessageImageView.setVisibility(View.VISIBLE);
		}
	};
	
	private BroadcastReceiver chatReceived = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			mNewChatImageView.setVisibility(View.VISIBLE);
		}
	};
	
	private BroadcastReceiver mapReceived = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			mNewMapImageView.setVisibility(View.VISIBLE);
		}
	};

	private BroadcastReceiver reportReceived = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			mNewReportImageView.setVisibility(View.VISIBLE);
		}
	};
	
	private void enableChatButton(){
		if(mDataManager.getMyOrgCapabilities().isCHAT() == true){
			mChatButton.setClickable(true);
			mChatButtonLayout.setAlpha(1);
		}
	}
	
	private void disableChatButton(){
		mChatButton.setClickable(false);
		mChatButtonLayout.setAlpha(0.3f);
	}
	
	private void enableGeneralMessageButton(){
		if(mDataManager.getMyOrgCapabilities().isSR() == true){
			mGeneralMessageButtonLayout.setAlpha(1f);
			mGeneralMessageButton.setClickable(true);
		}
	}
	
	private void disableGeneralMessageButton(){
		mGeneralMessageButton.setClickable(false);
		mGeneralMessageButtonLayout.setAlpha(0.3f);
	}
}
