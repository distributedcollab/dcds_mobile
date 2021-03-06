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

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import edu.mit.ll.dcds.android.MainActivity;
import edu.mit.ll.dcds.android.R;
import edu.mit.ll.dcds.android.api.DataManager;
import edu.mit.ll.dcds.android.api.data.UxoReportData;
import edu.mit.ll.dcds.android.api.data.UxoReportFormData;
import edu.mit.ll.dcds.android.api.payload.forms.SimpleReportPayload;
import edu.mit.ll.dcds.android.api.payload.forms.UxoReportPayload;
import edu.mit.ll.dcds.android.utils.Constants.NavigationOptions;
import edu.mit.ll.dcds.android.utils.FormType;
import edu.mit.ll.dcds.android.utils.Intents;

public class UxoReportFragment extends Fragment {

	private FormFragment mFormFragment;
	private LinearLayout mFormButtons;
	private Button mSaveDraftButton;
	private Button mSubmitButton;
	private Button mClearAllButton;

	private View mRootView;
	private DataManager mDataManager;
	private boolean isDraft = true;
	private long mReportId;
	
	private UxoReportPayload mCurrentPayload;
	private UxoReportData mCurrentData;
	private Menu mMenu;
	private boolean mHideCopy;
	private MainActivity mContext;

	private boolean receiverRegistered = false;
	private IntentFilter mIncidentSwitchedReceiverFilter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mContext = (MainActivity) getActivity();
		mReportId = -1;
		isDraft  = true;
		setHasOptionsMenu(true);
		
		mIncidentSwitchedReceiverFilter = new IntentFilter(Intents.dcds_INCIDENT_SWITCHED);
		
		if (!receiverRegistered) {
			mContext.registerReceiver(incidentChangedReceiver, mIncidentSwitchedReceiverFilter);
			receiverRegistered = true;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		mFormFragment = (FormFragment) getChildFragmentManager().findFragmentById(R.id.uxoReportFragment);

		if (mFormFragment == null) {
			int id = -1;
			if (savedInstanceState != null) {
				id = savedInstanceState.getInt("fragmentId", -1);
			}

			if (id == -1) {
				mRootView = inflater.inflate(R.layout.fragment_uxoreport, container, false);
			} else {
				mFormFragment = (FormFragment) getChildFragmentManager().findFragmentById(id);

				if (mFormFragment != null) {
					mRootView = container.findViewById(R.layout.fragment_uxoreport);
				}
			}
		} else if(mRootView == null) {
			mRootView = container.findViewById(R.layout.fragment_uxoreport);
		}
		
		mSaveDraftButton = (Button) mRootView.findViewById(R.id.uxoReportSaveButton);
		mSubmitButton = (Button) mRootView.findViewById(R.id.uxoReportSubmitButton);
		mClearAllButton = (Button) mRootView.findViewById(R.id.uxoReportClearButton);
		
		mFormButtons = (LinearLayout) mRootView.findViewById(R.id.uxoReportButtons);

		mSaveDraftButton.setOnClickListener(onActionButtonClick);
		mSubmitButton.setOnClickListener(onActionButtonClick);
		mClearAllButton.setOnClickListener(onActionButtonClick);
		mDataManager = DataManager.getInstance(getActivity());

		return mRootView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (mFormFragment == null) {
			mFormFragment = (FormFragment) getChildFragmentManager().findFragmentById(R.id.uxoReportFragment);
		}
		
		if (!receiverRegistered) {
			mContext.registerReceiver(incidentChangedReceiver, mIncidentSwitchedReceiverFilter);
			receiverRegistered = true;
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Save the ID of the existing MapFragment so it can properly be restored when app resumes
		if (mFormFragment != null) {
			outState.putInt("fragmentId", mFormFragment.getId());
		}
	}

	public void removeAssignmentFragment() {
		if (mFormFragment != null) {
			getChildFragmentManager().beginTransaction().remove(mFormFragment).commit();
			mFormFragment = null;
		}
	}

	public void populate(String incidentInfoJson, long id, boolean editable) {
		if (mFormFragment == null) {
			mFormFragment = (FormFragment) getChildFragmentManager().findFragmentById(R.id.uxoReportFragment);
		}
		
		mFormFragment.populate(incidentInfoJson, editable);
		mReportId = id;

		if(!editable) {
			mFormButtons.setVisibility(View.GONE);
			mHideCopy = false;
			getActivity().supportInvalidateOptionsMenu();

		} else {
			mFormButtons.setVisibility(View.VISIBLE);
			mHideCopy = true;
			getActivity().supportInvalidateOptionsMenu();
		}
		
		//Temporary fix to make DamagReports operate smoother while the images aren't working properly
		//should be removed
		if(editable == false){
			mCurrentData.setFullPath(null);
		}
	}
	
	public void hideCopy(boolean hide) {
		mHideCopy = hide;
		getActivity().supportInvalidateOptionsMenu();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

		inflater.inflate(R.menu.uxoreport_copy, menu);
		
		mMenu = menu;
		
		MenuItem item = mMenu.findItem(R.id.copyUxoReportOption);
		if(mHideCopy) {
			item.setVisible(false);
		} else {
			item.setVisible(true);
		}
		
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		if (receiverRegistered) {
			mContext.unregisterReceiver(incidentChangedReceiver);
			receiverRegistered = false;
		}
		
		((ViewGroup) mRootView.getParent()).removeView(mRootView);
	}

	private OnClickListener onActionButtonClick = new OnClickListener() {

		private AlertDialog mAlertDialog;
		
		@Override
		public void onClick(View v) {
			UxoReportPayload payload;
			UxoReportData messageData;
	    	long currentTime = System.currentTimeMillis ();
			
	    	switch (v.getId()) {
				case R.id.uxoReportSaveButton:
					isDraft = true;
					
					if(isDraft) {
						mDataManager.deleteUxoReportStoreAndForward(mReportId);
					}
					
					JSONObject saveTest = mFormFragment.save();
					String saveStringTest = saveTest.toString();
					
					messageData = new UxoReportData((new Gson().fromJson(saveStringTest, UxoReportFormData.class)));
					messageData.setUser(mDataManager.getUsername());
					messageData.setUserFull(mDataManager.getUserNickname());
					
					payload = new UxoReportPayload();
					
					payload.setId(mReportId);
					payload.setDraft(isDraft);
					payload.setIncidentId(mDataManager.getActiveIncidentId());
					payload.setIncidentName(mDataManager.getActiveIncidentName());
					payload.setFormTypeId(FormType.UXO.ordinal());
//					payload.setSenderUserId(mDataManager.getUserId());
					payload.setUserSessionId(mDataManager.getUserSessionId());
					payload.setMessageData(messageData);
//					payload.setCreatedUTC(currentTime);
//					payload.setLastUpdatedUTC(currentTime);
					payload.setSeqTime(currentTime);
					
					mDataManager.addUxoReportToStoreAndForward(payload);

					mContext.onNavigationItemSelected(NavigationOptions.UXOREPORT.getValue(), -2);
					break;
	
				case R.id.uxoReportSubmitButton:

					messageData = new UxoReportData((new Gson().fromJson(mFormFragment.save().toString(), UxoReportFormData.class)));
					messageData.setUser(mDataManager.getUsername());
					messageData.setUserFull(mDataManager.getUserNickname());
					
					if(messageData.getLatitude() == null){
						messageData.setLatitude("0");
					}
					if(messageData.getLongitude() == null){
						messageData.setLongitude("0");
					}
					
					messageData.setStatus("Open");
					
					boolean submit = true;

//						if(messageData.getFullPath() != null && !messageData.getFullPath().isEmpty()){
							
							if(submit) {
								if(isDraft) {
									mDataManager.deleteUxoReportStoreAndForward(mReportId);
									isDraft = false;
								}
								
								payload = new UxoReportPayload();
			
								payload.setId(mReportId);
								payload.setDraft(isDraft);
								payload.setIncidentId(mDataManager.getActiveIncidentId());
								payload.setIncidentName(mDataManager.getActiveIncidentName());
								payload.setFormTypeId(FormType.UXO.ordinal());
//								payload.setSenderUserId(mDataManager.getUserId());
								payload.setUserSessionId(mDataManager.getUserSessionId());
								payload.setMessageData(messageData);
//								payload.setCreatedUTC(currentTime);
//								payload.setLastUpdatedUTC(currentTime);
								payload.setSeqTime(currentTime);
								
								mDataManager.addUxoReportToStoreAndForward(payload);
								
								mDataManager.sendUxoReports();
			
								mContext.onNavigationItemSelected(NavigationOptions.UXOREPORT.getValue(), -2);
//							}else {
//								AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//								
//								builder.setMessage("Explosive Report is not complete");
//								builder.setTitle("Failed to complete report");
//								builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//								   public void onClick(DialogInterface dialog, int id) {
//								   }
//								});
//								
//								builder.create().show();
//						}

					}else{
						
						AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
						
						builder.setMessage(R.string.missing_image_capture_description);
						builder.setTitle(R.string.missing_image_capture_title);
						builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
						   public void onClick(DialogInterface dialog, int id) {
						   }
						});
						
						mAlertDialog = builder.create();
	                    mAlertDialog.show();
					}
					break;
	
				case R.id.uxoReportClearButton:
					mFormFragment.populate("", true);
					break;
			}
		}
	};

	public String toJson() {
		return mFormFragment.save().toString();
	}

	public long getReportId() {
		return mReportId;
	}

	public void setPayload(UxoReportPayload uxoReportPayload, boolean editable) {
		this.isDraft = uxoReportPayload.isDraft();
		mReportId = uxoReportPayload.getId();
		
		uxoReportPayload.parse();
		UxoReportData data = uxoReportPayload.getMessageData();
		
		mCurrentPayload = uxoReportPayload;
		mCurrentData = data;
		
		populate(new UxoReportFormData(data).toJsonString(), mReportId, editable);
	}
	
	public String getFormString() {
		return new UxoReportFormData(mCurrentData).toJsonString();
	}

	public UxoReportPayload getPayload() {
    	long currentTime = System.currentTimeMillis ();
    	
		if(mCurrentPayload != null) {
			mCurrentPayload.setId(mReportId);
			mCurrentPayload.setDraft(isDraft);
//			mCurrentPayload.setSenderUserId(mDataManager.getUserId());
			mCurrentPayload.setUserSessionId(mDataManager.getUserSessionId());
//			mCurrentPayload.setCreatedUTC(currentTime);
//			mCurrentPayload.setLastUpdatedUTC(currentTime);
			mCurrentPayload.setSeqTime(currentTime);
			
			mCurrentData = new UxoReportData(new Gson().fromJson(mFormFragment.save().toString(), UxoReportFormData.class));
			mCurrentData.setUser(mDataManager.getUsername());
			mCurrentData.setUserFull(mDataManager.getUserNickname());
			
			mCurrentPayload.setMessageData(mCurrentData);
		} else {
			mCurrentPayload = new UxoReportPayload();
			mCurrentData = new UxoReportData();
			mCurrentPayload.setMessageData(mCurrentData);
		}
		
		return mCurrentPayload;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		mFormFragment.onActivityResult(requestCode, resultCode, data);
	}

	private BroadcastReceiver incidentChangedReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			
			UxoReportPayload payload = mDataManager.getLastUxoReportPayload();
			if(payload != null){
				setPayload(payload,payload.isDraft());
			}else{
				mContext.addUxoReportToDetailView(false);
			}
		}
	};

	
}
