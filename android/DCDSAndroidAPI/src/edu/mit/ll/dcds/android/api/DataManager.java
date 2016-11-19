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
package edu.mit.ll.dcds.android.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.mit.ll.dcds.android.api.data.MarkupFeature;
import edu.mit.ll.dcds.android.api.data.UserHealth;
import edu.mit.ll.dcds.android.api.database.DatabaseManager;
import edu.mit.ll.dcds.android.api.messages.CollaborationRoomMessage;
import edu.mit.ll.dcds.android.api.messages.IncidentMessage;
import edu.mit.ll.dcds.android.api.messages.OrganizationMessage;
import edu.mit.ll.dcds.android.api.payload.ChatPayload;
import edu.mit.ll.dcds.android.api.payload.CollabroomPayload;
import edu.mit.ll.dcds.android.api.payload.IncidentPayload;
import edu.mit.ll.dcds.android.api.payload.LoginPayload;
import edu.mit.ll.dcds.android.api.payload.MobileDeviceTrackingPayload;
import edu.mit.ll.dcds.android.api.payload.OrganizationPayload;
import edu.mit.ll.dcds.android.api.payload.TrackingLayerPayload;
import edu.mit.ll.dcds.android.api.payload.UserPayload;
import edu.mit.ll.dcds.android.api.payload.WeatherPayload;
import edu.mit.ll.dcds.android.api.payload.forms.DamageReportPayload;
import edu.mit.ll.dcds.android.api.payload.forms.FieldReportPayload;
import edu.mit.ll.dcds.android.api.payload.forms.WeatherReportPayload;
import edu.mit.ll.dcds.android.api.payload.forms.UxoReportPayload;
import edu.mit.ll.dcds.android.api.payload.forms.ResourceRequestPayload;
import edu.mit.ll.dcds.android.api.payload.forms.SimpleReportPayload;
import edu.mit.ll.dcds.android.auth.AuthManager;
import edu.mit.ll.dcds.android.utils.Constants;
import edu.mit.ll.dcds.android.utils.EncryptedPreferences;
import edu.mit.ll.dcds.android.utils.Intents;
import edu.mit.ll.dcds.android.utils.LocationHandler;
import edu.mit.ll.dcds.android.utils.MyOrgCapabilities;
import edu.mit.ll.dcds.android.utils.NotificationsHandler;

public class DataManager {

	public String PACKAGE_NAME;
	
	private AlarmManager mAlarmManager;
	private static DataManager mInstance;
	private DatabaseManager mDatabaseManager;
	private LocationHandler mLocationHandler;
	private Locale mLocale;
	
	private static Context mContext;
	private static Activity mActiveActivity;
	
	private EncryptedPreferences mSharedPreferences;
	
	private boolean isLoggedIn;
	
	private PendingIntent mPendingResourceRequestIntent;
	private PendingIntent mPendingSimpleReportIntent;
	private PendingIntent mPendingFieldReportIntent;
	private PendingIntent mPendingDamageReportIntent;
	private PendingIntent mPendingWeatherReportIntent;
	private PendingIntent mPendingUxoReportIntent;
	private PendingIntent mPendingChatMessagesRequestIntent;
	private PendingIntent mPendingMarkupRequestIntent;
	
	private MyOrgCapabilities mMyOrgCapabilities;
	private OrganizationPayload mCurrentOrganization;
	
	private SharedPreferences mGlobalPreferences;
	
	private ConnectivityManager mConnectivityManager;
	
	private ArrayList<MarkupFeature> mMarkupFeatures;
	private HashMap<String, IncidentPayload> mIncidents;
	private HashMap<String, OrganizationPayload> mOrganizations;
	
	private boolean LowDataMode = false;
	private int LowDataRate = 120;
	public int selectedCatanView= 0;
	private String currentNavigationView = "Login";
	
	private String[] supportedLanguages;
	
	private Boolean isTabletLayout = null;
	
	private boolean newGeneralMessageAvailable = false;
	private boolean newReportAvailable = false;
	private boolean newchatAvailable = false;
	private boolean newMapAvailable = false;
	
	private long lastSuccesfulServerCommsTimestamp = 0;
	private long timeTillDisconnect;
	private Timer checkServerCommsTimer = new Timer();
	
	private ArrayList<TrackingLayerPayload> TrackingLayers;
	
	private DataManager() {
		mAlarmManager = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
		
		mDatabaseManager = new DatabaseManager(mContext);
		mGlobalPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		mLocale = mContext.getResources().getConfiguration().locale;
		
		mConnectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

		mContext.registerReceiver(connectivityReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
		mContext.registerReceiver(receiver, new IntentFilter(Intents.dcds_POLLING_TASK_DAMAGE_REPORT));
		mContext.registerReceiver(receiver, new IntentFilter(Intents.dcds_POLLING_TASK_FIELD_REPORT));
		mContext.registerReceiver(receiver, new IntentFilter(Intents.dcds_POLLING_TASK_WEATHER_REPORT));
		mContext.registerReceiver(receiver, new IntentFilter(Intents.dcds_POLLING_TASK_RESOURCE_REQUEST));
		mContext.registerReceiver(receiver, new IntentFilter(Intents.dcds_POLLING_TASK_SIMPLE_REPORT));
		mContext.registerReceiver(receiver, new IntentFilter(Intents.dcds_POLLING_TASK_UXO_REPORT));
		mContext.registerReceiver(receiver, new IntentFilter(Intents.dcds_POLLING_MARKUP_REQUEST));
		mContext.registerReceiver(receiver, new IntentFilter(Intents.dcds_POLLING_TASK_CHAT_MESSAGES));
		
		mSharedPreferences = new EncryptedPreferences( mContext.getSharedPreferences(Constants.PREFERENCES_NAME, Constants.PREFERENCES_MODE));
		
		if(getLocale().getLanguage() != getSelectedLanguage()){
			setCurrentLocale(getSelectedLanguage());
		}
		
		
		timeTillDisconnect = (getIncidentDataRate() * 1000) * 2;	//incidentData rate + 5 seconds
		checkServerCommsTimer.scheduleAtFixedRate(new TimerTask() {
			  @Override
			  public void run() {
			    CheckLastTimeServerWasPinged();
			  }
			}, getIncidentDataRate() * 1000,getIncidentDataRate() * 1000);
	}
	
	public Locale getLocale() {
		return mLocale;
	}
	
	public Context getContext() {
		return mContext;
	}
	
	public static DataManager getInstance() {
		if(mInstance != null) {
			return mInstance;
		}
		return null;
	}
	public static DataManager getInstance(Context context, Activity currentActivity) {
		mActiveActivity = currentActivity;
		return getInstance(context);
	}
	
	public static DataManager getInstance(Context context) {
		if(mInstance == null) {
			mContext = context;
			mInstance = new DataManager();
		}
		
		RestClient.setDataManager(mInstance);
		
		return mInstance;
	}

	public void requestLogin(String username, String password) {
		RestClient.login(mContext, username, password, false);
	}

	public void requestLogout() {
		RestClient.logout(getUsername(), false, false);
		if(mLocationHandler != null) {
			mLocationHandler.deactivate();
			mLocationHandler = null;
		}

		NotificationsHandler.getInstance(mContext).cancelAllNotifications();

		setUserSessionId(-1);
		setUsername(Constants.dcds_NO_RESULTS);
		mSharedPreferences.savePreferenceLong(Constants.INCIDENT_ID, (long)-1);	
		mSharedPreferences.savePreferenceString(Constants.INCIDENT_NAME, getContext().getResources().getString(R.string.no_selection));
		mSharedPreferences.removePreference(Constants.SELECTED_COLLABROOM);

		setAuthToken(null);
		
		stopPollingAlarms();
	}
	
	public void requestSimpleReports() {
		RestClient.getSimpleReports(-1, -1, getActiveIncidentId());
	}
	
	public void requestFieldReports() {
		RestClient.getFieldReports(-1, -1, getActiveIncidentId());
	}
	
	public void requestDamageReports() {
		RestClient.getDamageReports(-1, -1, getActiveIncidentId());
	}

	public void requestResourceRequests() {
		RestClient.getResourceRequests(-1, -1, getActiveIncidentId());
	}
	
	public void requestWeatherReports() {
		RestClient.getWeatherReports(-1, -1, getActiveIncidentId());
	}
	
	public void requestUxoReports() {
		RestClient.getUxoReports(-1, -1, getActiveIncidentId());
 	}
		
	public void requestChatHistory(long incidentId, long collabRoomId) {
		RestClient.getChatHistory(incidentId, collabRoomId);
	}
	
	public void requestWeatherUpdate(double latitude, double longitude) {
		RestClient.getWeatherUpdate(latitude, longitude);
	}
	
	public void requestMarkupUpdate() {
		RestClient.getMarkupHistory(getSelectedCollabRoom().getCollabRoomId());
	}
	
	public void requestCollabrooms(long incidentId){
		RestClient.getCollabRooms(incidentId);
	}
	
	public void requestWfsLayers(){
		RestClient.getWFSLayers();
	}
	
	public boolean isLoggedIn() {
		return isLoggedIn;
	}

	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}
		
	public void requestOrgCapabilitiesUpdate(){
		getMyOrgCapabilities().resetCapabilitiesToOn();
	}
		
	public MyOrgCapabilities getMyOrgCapabilities(){
		if(mMyOrgCapabilities == null){
			mMyOrgCapabilities = new MyOrgCapabilities();
		}
		return mMyOrgCapabilities;
	}

	public void deleteAllReportsFromLocalStorage(){
		mDatabaseManager.deleteAllDamageReportStoreAndForward();
		mDatabaseManager.deleteAllDamageReportHistory();
		mDatabaseManager.deleteAllFieldReportStoreAndForward();
		mDatabaseManager.deleteAllFieldReportHistory();
		mDatabaseManager.deleteAllResourceRequestHistory();
		mDatabaseManager.deleteAllResourceRequestStoreAndForward();
		mDatabaseManager.deleteAllWeatherReportsHistory();
		mDatabaseManager.deleteAllWeatherReportsStoreAndForward();
		mDatabaseManager.deleteAllSimpleReportsHistory();
		mDatabaseManager.deleteAllSimpleReportsStoreAndForward();
		mDatabaseManager.deleteAllUxoReportsHistory();
		mDatabaseManager.deleteAllUxoReportsStoreAndForward();
	}
	
	// Simple Report Functions
	
	public ArrayList<SimpleReportPayload> getSimpleReportHistoryForIncident(long incidentId) {
		return mDatabaseManager.getSimpleReportHistoryForIncident(incidentId);
	}
	
	public ArrayList<SimpleReportPayload> getAllSimpleReportHistory() {
		return mDatabaseManager.getAllSimpleReportHistory();
	}
	
	public ArrayList<SimpleReportPayload> getAllSimpleReportStoreAndForwardReadyToSend() {
		return mDatabaseManager.getAllSimpleReportStoreAndForwardReadyToSend();
	}

	public ArrayList<SimpleReportPayload> getAllSimpleReportStoreAndForwardReadyToSendForUser(String user) {
		return mDatabaseManager.getAllSimpleReportStoreAndForwardReadyToSendForUser(user);
	}
	
	public ArrayList<SimpleReportPayload> getAllSimpleReportStoreAndForwardReadyToSendForUserInIncident(long incidentId, String user) {
		return mDatabaseManager.getAllSimpleReportStoreAndForwardReadyToSendForUserInIncident(incidentId, user);
	}
	
	public ArrayList<SimpleReportPayload> getAllSimpleReportStoreAndForwardReadyToSend(long incidentId) {
		return mDatabaseManager.getAllSimpleReportStoreAndForwardReadyToSend(incidentId);
	}
	
	public ArrayList<SimpleReportPayload> getAllSimpleReportStoreAndForwardHasSent() {
		return mDatabaseManager.getAllSimpleReportStoreAndForwardHasSent();
	}

	public ArrayList<SimpleReportPayload> getAllSimpleReportStoreAndForwardHasSent(long incidentId) {
		return mDatabaseManager.getAllSimpleReportStoreAndForwardHasSent(incidentId);
	}

	public void addSimpleReportToHistory(SimpleReportPayload payload) {
		mDatabaseManager.addSimpleReportHistory(payload);
	}

	public boolean deleteSimpleReportFromHistory(long mReportId) {
		return mDatabaseManager.deleteSimpleReportHistory(mReportId);
	}
	
	public boolean deleteSimpleReportFromHistoryByIncident(long incidentId) {
		return mDatabaseManager.deleteSimpleReportHistoryByIncident(incidentId);
	}
	
	public boolean deleteSimpleReportStoreAndForward(long mReportId) {
		return mDatabaseManager.deleteSimpleReportStoreAndForward(mReportId);
	}

	public boolean addSimpleReportToStoreAndForward(SimpleReportPayload payload) {
		return mDatabaseManager.addSimpleReportToStoreAndForward(payload);
	}

	public long getLastSimpleReportTimestamp() {
		return mDatabaseManager.getLastSimpleReportTimestamp(getActiveIncidentId());
	}
	
	public SimpleReportPayload getLastSimpleReportPayload() {
		return mDatabaseManager.getLastSimpleReportPayload(getActiveIncidentId());
	}
	
	// Field Report Functions
	public ArrayList<FieldReportPayload> getFieldReportHistoryForIncident(long incidentId) {
		return mDatabaseManager.getFieldReportHistoryForIncident(incidentId);
	}
	
	public ArrayList<FieldReportPayload> getAllFieldReportHistory() {
		return mDatabaseManager.getAllFieldReportHistory();
	}

	public ArrayList<FieldReportPayload> getAllFieldReportStoreAndForwardReadyToSend() {
		return mDatabaseManager.getAllFieldReportStoreAndForwardReadyToSend();
	}
	
	public ArrayList<FieldReportPayload> getAllFieldReportStoreAndForwardReadyToSend(long incidentId) {
		return mDatabaseManager.getAllFieldReportStoreAndForwardReadyToSend(incidentId);
	}
	
	public ArrayList<FieldReportPayload> getAllFieldReportStoreAndForwardReadyToSendForUser(String user) {
		return mDatabaseManager.getAllFieldReportStoreAndForwardReadyToSendForUser(user);
	}
	
	public ArrayList<FieldReportPayload> getAllFieldReportStoreAndForwardReadyToSendForUserInIncident(long incidentId, String user) {
		return mDatabaseManager.getAllFieldReportStoreAndForwardReadyToSendForUserInIncident(incidentId, user);
	}
	
	public ArrayList<FieldReportPayload> getAllFieldReportStoreAndForwardHasSent() {
		return mDatabaseManager.getAllFieldReportStoreAndForwardHasSent();
	}
	
	public ArrayList<FieldReportPayload> getAllFieldReportStoreAndForwardHasSent(long incidentId) {
		return mDatabaseManager.getAllFieldReportStoreAndForwardHasSent(incidentId);
	}

	public boolean addFieldReportToHistory(FieldReportPayload payload) {
		return mDatabaseManager.addFieldReportHistory(payload);
	}
	
	public long getLastFieldReportTimestamp() {
		return mDatabaseManager.getLastFieldReportTimestamp(getActiveIncidentId());
	}
	
	public FieldReportPayload getLastFieldReportPayload() {
		return mDatabaseManager.getLastFieldReportPayload(getActiveIncidentId());
	}
	
	public boolean deleteFieldReportFromHistoryByIncident(long incidentId) {
		return mDatabaseManager.deleteFieldReportHistoryByIncident(incidentId);
	}
	
	public boolean deleteFieldReportFromHistory(long mReportId) {
		return mDatabaseManager.deleteFieldReportHistory(mReportId);
	}
	
	public boolean deleteFieldReportStoreAndForward(long mReportId) {
		return mDatabaseManager.deleteFieldReportStoreAndForward(mReportId);
	}

	public boolean addFieldReportToStoreAndForward(FieldReportPayload payload) {
		return mDatabaseManager.addFieldReportToStoreAndForward(payload);
	}
	
	// Damage Report Functions
	public ArrayList<DamageReportPayload> getDamageReportHistoryForIncident(long incidentId) {
		return mDatabaseManager.getDamageReportHistoryForIncident(incidentId);
	}
	
	public ArrayList<DamageReportPayload> getAllDamageReportHistory() {
		return mDatabaseManager.getAllDamageReportHistory();
	}

	public ArrayList<DamageReportPayload> getAllDamageReportStoreAndForwardReadyToSend() {
		return mDatabaseManager.getAllDamageReportStoreAndForwardReadyToSend();
	}

	public ArrayList<DamageReportPayload> getAllDamageReportStoreAndForwardReadyToSend(long incidentId) {
		return mDatabaseManager.getAllDamageReportStoreAndForwardReadyToSend(incidentId);
	}
	
	public ArrayList<DamageReportPayload> getAllDamageReportStoreAndForwardReadyToSendForUser(String user) {
		return mDatabaseManager.getAllDamageReportStoreAndForwardReadyToSendForUser(user);
	}
	
	public ArrayList<DamageReportPayload> getAllDamageReportStoreAndForwardReadyToSendForUserInIncident(long incidentId, String user) {
		return mDatabaseManager.getAllDamageReportStoreAndForwardReadyToSendForUserInIncident(incidentId, user);
	}
	
	public ArrayList<DamageReportPayload> getAllDamageReportStoreAndForwardHasSent() {
		return mDatabaseManager.getAllDamageReportStoreAndForwardHasSent();
	}

	public ArrayList<DamageReportPayload> getAllDamageReportStoreAndForwardHasSent(long incidentId) {
		return mDatabaseManager.getAllDamageReportStoreAndForwardHasSent(incidentId);
	}
	
	public boolean addDamageReportToHistory(DamageReportPayload payload) {
		return mDatabaseManager.addDamageReportHistory(payload);
	}
	
	public long getLastDamageReportTimestamp() {
		return mDatabaseManager.getLastDamageReportTimestamp(getActiveIncidentId());
	}
	
	public DamageReportPayload getLastDamageReportPayload() {
		return mDatabaseManager.getLastDamageReportPayload(getActiveIncidentId());
	}
	
	public boolean deleteDamageReportFromHistoryByIncident(long incidentId) {
		return mDatabaseManager.deleteDamageReportHistoryByIncident(incidentId);
	}
	
	public boolean deleteDamageReportFromHistory(long mReportId) {
		return mDatabaseManager.deleteDamageReportHistory(mReportId);
	}
	
	public boolean deleteDamageReportStoreAndForward(long mReportId) {
		return mDatabaseManager.deleteDamageReportStoreAndForward(mReportId);
	}

	public boolean addDamageReportToStoreAndForward(DamageReportPayload payload) {
		return mDatabaseManager.addDamageReportToStoreAndForward(payload);
	}
	
	// Weather Report Functions
	public ArrayList<WeatherReportPayload> getWeatherReportHistoryForIncident(long incidentId) {
		return mDatabaseManager.getWeatherReportHistoryForIncident(incidentId);
	}
	
	public ArrayList<WeatherReportPayload> getAllWeatherReportHistory() {
		return mDatabaseManager.getAllWeatherReportHistory();
	}

	public ArrayList<WeatherReportPayload> getAllWeatherReportStoreAndForwardReadyToSend() {
		return mDatabaseManager.getAllWeatherReportStoreAndForwardReadyToSend();
	}
	
	public ArrayList<WeatherReportPayload> getAllWeatherReportStoreAndForwardReadyToSend(long incidentId) {
		return mDatabaseManager.getAllWeatherReportStoreAndForwardReadyToSend(incidentId);
	}
	
	public ArrayList<WeatherReportPayload> getAllWeatherReportStoreAndForwardReadyToSendForUser(String user) {
		return mDatabaseManager.getAllWeatherReportStoreAndForwardReadyToSendForUser(user);
	}
	
	public ArrayList<WeatherReportPayload> getAllWeatherReportStoreAndForwardReadyToSendForUserInIncident(long incidentId, String user) {
		return mDatabaseManager.getAllWeatherReportStoreAndForwardReadyToSendForUserInIncident(incidentId, user);
	}
	
	public ArrayList<WeatherReportPayload> getAllWeatherReportStoreAndForwardHasSent() {
		return mDatabaseManager.getAllWeatherReportStoreAndForwardHasSent();
	}
	
	public ArrayList<WeatherReportPayload> getAllWeatherReportStoreAndForwardHasSent(long incidentId) {
		return mDatabaseManager.getAllWeatherReportStoreAndForwardHasSent(incidentId);
	}

	public boolean addWeatherReportToHistory(WeatherReportPayload payload) {
		return mDatabaseManager.addWeatherReportHistory(payload);
	}
	
	public long getLastWeatherReportTimestamp() {
		return mDatabaseManager.getLastWeatherReportTimestamp(getActiveIncidentId());
	}
	
	public WeatherReportPayload getLastWeatherReportPayload() {
		return mDatabaseManager.getLastWeatherReportPayload(getActiveIncidentId());
	}
	
	public boolean deleteWeatherReportFromHistoryByIncident(long incidentId) {
		return mDatabaseManager.deleteWeatherReportHistoryByIncident(incidentId);
	}
	
	public boolean deleteWeatherReportFromHistory(long mReportId) {
		return mDatabaseManager.deleteWeatherReportHistory(mReportId);
	}
	
	public boolean deleteWeatherReportStoreAndForward(long mReportId) {
		return mDatabaseManager.deleteWeatherReportStoreAndForward(mReportId);
	}

	public boolean addWeatherReportToStoreAndForward(WeatherReportPayload payload) {
		return mDatabaseManager.addWeatherReportToStoreAndForward(payload);
	}

	// Uxo Report Functions
	public ArrayList<UxoReportPayload> getUxoReportHistoryForIncident(long incidentId) {
		return mDatabaseManager.getUxoReportHistoryForIncident(incidentId);
	}
	
	public ArrayList<UxoReportPayload> getAllUxoReportHistory() {
		return mDatabaseManager.getAllUxoReportHistory();
	}

	public ArrayList<UxoReportPayload> getAllUxoReportStoreAndForwardReadyToSend() {
		return mDatabaseManager.getAllUxoReportStoreAndForwardReadyToSend();
	}

	public ArrayList<UxoReportPayload> getAllUxoReportStoreAndForwardReadyToSend(long incidentId) {
		return mDatabaseManager.getAllUxoReportStoreAndForwardReadyToSend(incidentId);
	}
	
	public ArrayList<UxoReportPayload> getAllUxoReportStoreAndForwardReadyToSendForUser(String user) {
		return mDatabaseManager.getAllUxoReportStoreAndForwardReadyToSendForUser(user);
	}
	
	public ArrayList<UxoReportPayload> getAllUxoReportStoreAndForwardReadyToSendForUserInIncident(long incidentId, String user) {
		return mDatabaseManager.getAllUxoReportStoreAndForwardReadyToSendForUserInIncident(incidentId, user);
	}
	
	public ArrayList<UxoReportPayload> getAllUxoReportStoreAndForwardHasSent() {
		return mDatabaseManager.getAllUxoReportStoreAndForwardHasSent();
	}

	public ArrayList<UxoReportPayload> getAllUxoReportStoreAndForwardHasSent(long incidentId) {
		return mDatabaseManager.getAllUxoReportStoreAndForwardHasSent(incidentId);
	}
	
	public boolean addUxoReportToHistory(UxoReportPayload payload) {
		return mDatabaseManager.addUxoReportHistory(payload);
	}
	
	public long getLastUxoReportTimestamp() {
		return mDatabaseManager.getLastUxoReportTimestamp(getActiveIncidentId());
	}
	
	public UxoReportPayload getLastUxoReportPayload() {
		return mDatabaseManager.getLastUxoReportPayload(getActiveIncidentId());
	}
	
	public boolean deleteUxoReportFromHistoryByIncident(long incidentId) {
		return mDatabaseManager.deleteUxoReportHistoryByIncident(incidentId);
	}
	
	public boolean deleteUxoReportFromHistory(long mReportId) {
		return mDatabaseManager.deleteUxoReportHistory(mReportId);
	}
	
	public boolean deleteUxoReportStoreAndForward(long mReportId) {
		return mDatabaseManager.deleteUxoReportStoreAndForward(mReportId);
	}

	public boolean addUxoReportToStoreAndForward(UxoReportPayload payload) {
		return mDatabaseManager.addUxoReportToStoreAndForward(payload);
	}
	
	public Header[] getAuthData() {
		return RestClient.getAuthData();
	}
	
	public boolean addChatToStoreAndForward(ChatPayload payload) {
		return mDatabaseManager.addChatToStoreAndForward(payload);
	}
		
	public long getLastChatTimestamp(long incidentId, long collabRoomId) {
		long timestamp = -1;
		
		CollaborationRoomMessage message = getCollabRoomsForIncident(incidentId);
	
		if(mDatabaseManager != null && message.getresults().size() > 0) {
			timestamp = mDatabaseManager.getLastChatHistoryTimestamp(collabRoomId);
		} else {
			timestamp = -99;
		}
		
		return timestamp;
	}
	
	public ChatPayload getLastChatMessage(long collabroomId) {
		return mDatabaseManager.getLastChatHistory(collabroomId);
	}
	
	public ChatPayload addChatMsgToStoreAndForward(String msg, String selectedCollabroomName) {
    	ChatPayload data = null;
    	
    	long currentTime = System.currentTimeMillis ();
        String incidentName = getActiveIncidentName();

        if(selectedCollabroomName != null && incidentName != null) {
	        
	        data = new ChatPayload();
	        data.setcreated(currentTime);
	        data.setlastupdated(currentTime);
	        data.setIncidentId(getActiveIncidentId());
	        data.setchatid(currentTime);
	        data.setmessage(msg);
	        data.setuserId(getUserId());
	        data.setcollabroomid(getSelectedCollabRoom().getCollabRoomId());
	        data.setseqnum(currentTime);
	        data.setchatid(currentTime);
	        data.setNickname(getUsername());
	        data.setUserOrgName(getCurrentOrganziation().getName());
	        data.setUserorgid((long)getCurrentOrganziation().getUserorgs()[0].getUserorgid());
        }
	    
        if(data != null) {
        	mDatabaseManager.addChatToStoreAndForward(data);
        }
        
    	return data;
	}

	public ChatPayload addPersonalHistory(String msg) {
    	ChatPayload data = null;
    	
    	long currentTime = System.currentTimeMillis ();
        data = new ChatPayload();
        data.setcreated(currentTime);
        data.setlastupdated(currentTime);
        data.setchatid(currentTime);
        data.setmessage(msg);
        data.setuserId(getUserId());
        data.setcollabroomid(-1);
        data.setseqnum(currentTime);
        data.setTopic("debugLog");
        data.setNickname(getUserNickname());
	            
        Intent intent = new Intent();
        intent.setAction(Intents.dcds_NEW_PERSONAL_HISTORY_RECEIVED);
        intent.putExtra("payload", data.toJsonString());
        mContext.sendBroadcast (intent);
        
    	return data;
	}

	public ArrayList<ChatPayload> getRecentChatHistory() {
		return mDatabaseManager.getRecentChatHistory(getSelectedCollabRoom().getCollabRoomId());
	}
	
	public ArrayList<ChatPayload> getNewChatMessages(long timestamp) {
		return mDatabaseManager.getNewChatMessagesFromDate(getSelectedCollabRoom().getCollabRoomId(), timestamp);
	}
	
	public ArrayList<ChatPayload> getRecentChatHistoryStartingFromAndGoingBack(long timestamp, String limit) {
		return mDatabaseManager.getRecentChatHistoryStartingFromAndGoingBack(getSelectedCollabRoom().getCollabRoomId(), timestamp, limit);
	}
	
	public ArrayList<ChatPayload> getChatStoreAndForwardReadyToSend() {
		return mDatabaseManager.getChatStoreAndForwardReadyToSend(getSelectedCollabRoom().getCollabRoomId());
	}

	public boolean addChatHistory(ChatPayload payload) {
		return mDatabaseManager.addChatHistory(payload);
	}
	
	public boolean addResourceRequestToStoreAndForward(ResourceRequestPayload data) {
		return mDatabaseManager.addResourceRequestToStoreAndForward(data);
	}
	
	public boolean deleteResourceRequestFromHistoryByIncident(long incidentId) {
		return mDatabaseManager.deleteResourceRequestHistoryByIncident(incidentId);
	}

	public boolean deleteResourceRequestFromHistory(long mReportId) {
		return mDatabaseManager.deleteResourceRequestHistory(mReportId);
	}
	
	public boolean deleteResourceRequestStoreAndForward(long mReportId) {
		return mDatabaseManager.deleteResourceRequestStoreAndForward(mReportId);
	}
	
	public boolean addResourceRequestToHistory(ResourceRequestPayload data) {
		return mDatabaseManager.addResourceRequestHistory(data);
	}

	public ArrayList<ResourceRequestPayload> getResourceRequestHistoryForIncident(long incidentId) {
		return mDatabaseManager.getResourceRequestHistoryForIncident(incidentId);
	}
	
	public ArrayList<ResourceRequestPayload> getAllResourceRequestHistory() {
		return mDatabaseManager.getAllResourceRequestHistory();
	}

	public ArrayList<ResourceRequestPayload> getAllResourceRequestStoreAndForwardReadyToSend() {
		return mDatabaseManager.getAllResourceRequestStoreAndForwardReadyToSend();
	}
	
	public ArrayList<ResourceRequestPayload> getAllResourceRequestStoreAndForwardReadyToSend(long incidentId) {
		return mDatabaseManager.getAllResourceRequestStoreAndForwardReadyToSend(incidentId);
	}
	
	public ArrayList<ResourceRequestPayload> getAllResourceRequestStoreAndForwardReadyToSendForUser(String user) {
		return mDatabaseManager.getAllResourceRequestStoreAndForwardReadyToSendForUser(user);
	}
	
	public ArrayList<ResourceRequestPayload> getAllResourceRequestStoreAndForwardReadyToSendForUserInIncident(long incidentId, String user) {
		return mDatabaseManager.getAllResourceRequestStoreAndForwardReadyToSendForUserInIncident(incidentId, user);
	}
	
	public ArrayList<ResourceRequestPayload> getAllResourceRequestStoreAndForwardHasSent() {
		return mDatabaseManager.getAllResourceRequestStoreAndForwardHasSent();
	}
	
	public ArrayList<ResourceRequestPayload> getAllResourceRequestStoreAndForwardHasSent(long incidentId) {
		return mDatabaseManager.getAllResourceRequestStoreAndForwardHasSent(incidentId);
	}

	public long getLastResourceRequestTimestamp() {
		return mDatabaseManager.getLastResourceRequestTimestamp(getActiveIncidentId());
	}

	public ResourceRequestPayload getLastResourceRequestPayload() {
		return mDatabaseManager.getLastResourceRequestPayload(getActiveIncidentId());
	}
	
    public String getIncidentInfoJson() {
		JSONObject incidentInfo = new JSONObject();
		
		try {
			incidentInfo.put("incident_id", this.getActiveIncidentId());
			incidentInfo.put("incident_name", this.getActiveIncidentName());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return incidentInfo.toString();
    }
        
    public void requestFieldReportRepeating(int seconds, boolean immediately) {
    	Intent intent = new Intent(Intents.dcds_POLLING_TASK_FIELD_REPORT);
    	intent.putExtra("type", "fieldreport");
    	
    	mPendingFieldReportIntent = PendingIntent.getBroadcast(mContext, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    	long secondsFromNow = SystemClock.elapsedRealtime() + 200;
    	
    	if(!immediately) {
    		secondsFromNow += (seconds * 1000);
    	}
    	mAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, secondsFromNow, (seconds * 1000), mPendingFieldReportIntent);
    	
    	Log.i("dcdsDataManager", "Set field report repeating fetch interval:" + seconds + " seconds.");
    }
    
    public void requestDamageReportRepeating(int seconds, boolean immediately) {
    	Intent intent = new Intent(Intents.dcds_POLLING_TASK_DAMAGE_REPORT);
    	intent.putExtra("type", "damagereport");
    	
    	mPendingDamageReportIntent = PendingIntent.getBroadcast(mContext, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    	long secondsFromNow = SystemClock.elapsedRealtime() + 200;
    	
    	if(!immediately) {
    		secondsFromNow += (seconds * 1000);
    	}
    	mAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, secondsFromNow, (seconds * 1000), mPendingDamageReportIntent);
    	
    	Log.i("dcdsDataManager", "Set damage report repeating fetch interval:" + seconds + " seconds.");
    }
    
    public void requestSimpleReportRepeating(int seconds, boolean immediately) {
    	Intent intent = new Intent(Intents.dcds_POLLING_TASK_SIMPLE_REPORT);
    	intent.putExtra("type", "simplereport");
    	
    	mPendingSimpleReportIntent = PendingIntent.getBroadcast(mContext, 2, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    	long secondsFromNow = SystemClock.elapsedRealtime() + 200;
    	
    	if(!immediately) {
    		secondsFromNow += (seconds * 1000);
    	}
    	mAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, secondsFromNow, (seconds * 1000), mPendingSimpleReportIntent);
    	
    	Log.i("dcdsDataManager", "Set simple report repeating fetch interval:" + seconds + " seconds.");
    }
    
    public void requestResourceRequestRepeating(int seconds, boolean immediately) {
    	Intent intent = new Intent(Intents.dcds_POLLING_TASK_RESOURCE_REQUEST);
    	intent.putExtra("type", "resourcerequest");
    	
    	mPendingResourceRequestIntent = PendingIntent.getBroadcast(mContext, 3, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    	long secondsFromNow = SystemClock.elapsedRealtime() + 200;
    	
    	if(!immediately) {
    		secondsFromNow += (seconds * 1000);
    	}
    	mAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, secondsFromNow, (seconds * 1000), mPendingResourceRequestIntent);
    	
    	Log.i("dcdsDataManager", "Set resource request repeating fetch interval:" + seconds + " seconds.");
    }
    
    public void requestWeatherReportRepeating(int seconds, boolean immediately) {
    	Intent intent = new Intent(Intents.dcds_POLLING_TASK_WEATHER_REPORT);
    	intent.putExtra("type", "weatherreport");
    	
    	mPendingWeatherReportIntent = PendingIntent.getBroadcast(mContext, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    	long secondsFromNow = SystemClock.elapsedRealtime() + 200;
    	
    	if(!immediately) {
    		secondsFromNow += (seconds * 1000);
    	}
    	mAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, secondsFromNow, (seconds * 1000), mPendingWeatherReportIntent);
    	
    	Log.i("dcdsDataManager", "Set weather report repeating fetch interval:" + seconds + " seconds.");
    }
    
    public void requestUxoReportRepeating(int seconds, boolean immediately) {
    	Intent intent = new Intent(Intents.dcds_POLLING_TASK_UXO_REPORT);
    	intent.putExtra("type", "uxoreport");
    	
    	mPendingUxoReportIntent = PendingIntent.getBroadcast(mContext, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    	long secondsFromNow = SystemClock.elapsedRealtime() + 200;
    	
    	if(!immediately) {
    		secondsFromNow += (seconds * 1000);
    	}
    	mAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, secondsFromNow, (seconds * 1000), mPendingUxoReportIntent);
    	
    	Log.i("dcdsDataManager", "Set uxo report repeating fetch interval:" + seconds + " seconds.");
    }
        
    public void requestMarkupRepeating(int seconds, boolean immediately) {
    	Intent intent = new Intent(Intents.dcds_POLLING_MARKUP_REQUEST);
    	intent.putExtra("type", "markup");
    	
    	mPendingMarkupRequestIntent = PendingIntent.getBroadcast(mContext, 4, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    	long secondsFromNow = SystemClock.elapsedRealtime() + 200;
    	
    	if(!immediately) {
    		secondsFromNow += (seconds * 1000);
    	}
    	mAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, secondsFromNow, (seconds * 1000), mPendingMarkupRequestIntent);
    	
    	Log.i("dcdsDataManager", "Set map markup repeating fetch interval:" + seconds + " seconds.");
    }
    
    public void requestChatMessagesRepeating(int seconds, boolean immediately) {
    	Intent intent = new Intent(Intents.dcds_POLLING_TASK_CHAT_MESSAGES);
    	intent.putExtra("type", "chatmessages");
    	
    	mPendingChatMessagesRequestIntent = PendingIntent.getBroadcast(mContext, 5, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    	
    	long secondsFromNow = SystemClock.elapsedRealtime() + 200;
    	
    	if(!immediately) {
    		secondsFromNow += (seconds * 1000);
    	}
    	mAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, secondsFromNow, (seconds * 1000), mPendingChatMessagesRequestIntent);
    	
    	Log.i("dcdsDataManager", "Set chat message request repeating fetch interval:" + seconds + " seconds.");
    }
     
    public void stopPollingAlarms() {
    	mAlarmManager.cancel(mPendingSimpleReportIntent);
    	mAlarmManager.cancel(mPendingDamageReportIntent);
    	mAlarmManager.cancel(mPendingFieldReportIntent);
    	mAlarmManager.cancel(mPendingResourceRequestIntent);
    	mAlarmManager.cancel(mPendingWeatherReportIntent);
    	mAlarmManager.cancel(mPendingUxoReportIntent);
    	mAlarmManager.cancel(mPendingChatMessagesRequestIntent);
    	mAlarmManager.cancel(mPendingMarkupRequestIntent);
    }
    
    public void stopPollingMarkup() {
    	mAlarmManager.cancel(mPendingMarkupRequestIntent);
    	RestClient.clearParseMarkupFeaturesTask();
    }
    
    public void stopPollingChat() {
    	mAlarmManager.cancel(mPendingChatMessagesRequestIntent);
    	RestClient.clearParseChatMessagesTask();
    }
        
    BroadcastReceiver receiver = new BroadcastReceiver() {
		@SuppressLint("Wakelock")
		@Override
		public void onReceive(Context context, Intent intent) {
					
			if(CheckWifiStatus() && isLoggedIn()){
				try {
					PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
					PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "dcds_WAKE");
		
					//Acquire the lock
					wakeLock.acquire();
					
					Bundle extras = intent.getExtras();
					if(extras != null) {
						String type = extras.getString("type");
		
						if(type != null) {
							Log.i("dcdsDataManager", "Requesting Data Update: " + type);
							if(type.equals("fieldreport")) {
								requestFieldReports();
							} else if(type.equals("weatherreport")) {
								requestWeatherReports();
							} else if(type.equals("damagereport")) {
								requestDamageReports();
							} else if(type.equals("simplereport")) {
								requestSimpleReports();
							} else if(type.equals("resourcerequest")) {
								requestResourceRequests();
							} else if(type.equals("uxoreport")) {
								requestUxoReports();
							} else if(type.equals("chatmessages")) {
								requestChatHistory(getActiveIncidentId(), getSelectedCollabRoom().getCollabRoomId());
							} else if(type.equals("markup")) {
								requestMarkupUpdate();
							}
						}
					}
					
					//Release the lock
					wakeLock.release();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				switchToOfflineMode();
				Bundle extras = intent.getExtras();
				String type = extras.getString("type");
				Log.i("dcdsDataManager", "Cancel Data Request, not in wifi: " + type);
			}
		}
	};
	
	BroadcastReceiver connectivityReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			NetworkInfo info = mConnectivityManager.getActiveNetworkInfo();
					
			if(info != null) {
				if(info.isConnectedOrConnecting()) {
					
					if(getUserSessionId() == -1){
							
						if(getUsername().equals(Constants.dcds_NO_RESULTS) ==false && 
							getPassword().equals(Constants.dcds_NO_RESULTS) ==false && 
							getPassword().equals("") == false &&
							getUsername().equals("") == false){
							
								
							if(AuthManager.getInstance()!=null){
				    			if(AuthManager.getInstance().getClient()!= null){
				    				RestClient.attemptLogin(getUsername(),getPassword(),false);	
				    			}else{
				    				requestLogin(getUsername(),getPassword());
				    			}
							}else{
								if(getAutoLogin()){
									requestLogin(getUsername(),getPassword());
								}
							}
						}
					}else{
						switchToOnlineMode();
					}
					
					Log.i("dcdsDataManager","Device reconnected to " + info.getTypeName() + " network.");
				}else{
					switchToOfflineMode();
				}
					
			} else {
				switchToOfflineMode();
				Log.i("dcdsDataManager","Device disconnected from the data network.");
			}
		}	
	};
	
	public void sendAllLocalContent(){
		sendChatMessages();
		sendDamageReports();
		sendSimpleReports();
		sendFieldReports();
		sendWeatherReports();
		sendUxoReports();
		sendMarkupFeatures();
		sendResourceRequests();
//		sendCatanRequests();
	}
	
	public void checkIfOnlineAndLogin(){
		NetworkInfo info = mConnectivityManager.getActiveNetworkInfo();
		if(info != null){
			if(info.isConnectedOrConnecting()){
				
				if(getUserSessionId() == -1){
					requestLogin(getUsername(),getPassword());
				}
			}
		}
	}
	
	public boolean checkIfOnline(){
		NetworkInfo info = mConnectivityManager.getActiveNetworkInfo();
		if(info != null){
			if(info.isConnectedOrConnecting()){
				if(getUserSessionId() == -1){
					return false;
				}else{
					return true;
				}
			}
		}
		return false;
	}
	
	public void switchToOfflineMode(){
		setLoggedIn(false);
		setUserSessionId(-1);
		
        Intent intent = new Intent();
        intent.setAction(Intents.dcds_SWITCH_TO_OFFLINE_MODE);
        mContext.sendBroadcast (intent);
	}
	
	public void switchToOnlineMode(){
		
        Intent intent = new Intent();
        intent.setAction(Intents.dcds_SWITCH_TO_ONLINE_MODE);
        mContext.sendBroadcast (intent);
        
        Log.d("online_mode_debug", "userSessionID: " + getUserSessionId());
        
        if(getUsername().equals(Constants.dcds_NO_RESULTS)){
        	return;
        }
        if(getPassword().equals("")){
        	return;
        }
	}
	
	public LocationHandler getLocationSource() {
		if(mLocationHandler == null && isMDTEnabled()) {
			mLocationHandler = new LocationHandler(mContext);
		}
		return mLocationHandler;
	}
	
	public void forceLocationUpdate() {
		if(mLocationHandler != null) {
			mLocationHandler.forceUpdate();
		}
	}
	
    public void addMarkupFeatureToStoreAndForward(MarkupFeature feature) {
		mDatabaseManager.addMarkupToStoreAndForward(feature);
    }
    
    public boolean deleteMarkupFeatureStoreAndForward(long featureId) {
		return mDatabaseManager.deleteMarkupStoreAndForward(featureId);
	}
    
	public ArrayList<MarkupFeature> getAllMarkupFeaturesStoreAndForwardReadyToSend() {
		return mDatabaseManager.getAllMarkupStoreAndForward();
	}
	
	public ArrayList<MarkupFeature> getAllMarkupFeaturesStoreAndForwardReadyToSendForUser(String username) {
		return mDatabaseManager.getAllMarkupStoreAndForwardForUser(username);
	}

	public long getLastMarkupTimestamp(long collabRoomId) {
		return mDatabaseManager.getLastMarkupTimestamp(collabRoomId);
	}

	public boolean addMarkupFeatureToHistory(MarkupFeature feature) {
		return mDatabaseManager.addMarkupFeatureToHistory(feature);
	}
	
	public void addAllMarkupFeaturesToHistory(ArrayList<MarkupFeature> featureSet) {
		mDatabaseManager.addAllMarkupFeatureToHistory(featureSet);
	}
	
	public ArrayList<MarkupFeature> getMarkupHistory() {
		return mDatabaseManager.getAllMarkupFeatureHistory();
	}
	
	public ArrayList<MarkupFeature> getMarkupHistoryForCollabroom(long collabroomId) {
		return mDatabaseManager.getMarkupHistoryForCollabroom(collabroomId);
	}
	
	public ArrayList<MarkupFeature> getMarkupHistoryForCollabroomWithFeatureIds(long collabroomId, ArrayList<String> featureIds) {
		return mDatabaseManager.getMarkupHistoryForCollabroomWithFeatureIds(collabroomId, featureIds);
	}
	
	public void setSelectedCollabRoom(CollabroomPayload newCollabroomPayload) {
		if(newCollabroomPayload != null){
			mSharedPreferences.savePreferenceString(Constants.SELECTED_COLLABROOM, newCollabroomPayload.toJsonString());
		}else{
			newCollabroomPayload = new CollabroomPayload();
			newCollabroomPayload.setCollabRoomId(-1);
			newCollabroomPayload.setName(getContext().getResources().getString(R.string.no_selection));
			mSharedPreferences.savePreferenceString(Constants.SELECTED_COLLABROOM, newCollabroomPayload.toJsonString());
		}
		
		if(newCollabroomPayload.getCollabRoomId() != -1) {
			requestChatMessagesRepeating(getCollabroomDataRate(), true);
		} else {
			stopPollingMarkup();
			stopPollingChat();
		}
	}
	
	public CollabroomPayload getSelectedCollabRoom() {
		
		String collabroomString = mSharedPreferences.getPreferenceString(Constants.SELECTED_COLLABROOM, "");
		if(!collabroomString.equals("")){
			return new Gson().fromJson(collabroomString, CollabroomPayload.class);
		}
		else{
			CollabroomPayload newPayload = new CollabroomPayload();
			newPayload.setCollabRoomId(-1);
			newPayload.setName(getContext().getResources().getString(R.string.no_selection));
			return newPayload;
		}
	}
		
	public boolean CheckWifiStatus(){
		boolean ProceedWithSync = true;
		
		if(isSyncWifiOnlyEnabled()){
			ConnectivityManager connManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (!mWifi.isConnected()) {
				ProceedWithSync = false;
			}
		}
		return ProceedWithSync;
	}
	
	public void sendSimpleReports() {
		if(CheckWifiStatus()){
			RestClient.postSimpleReports();
		}
	}
	
	public void sendFieldReports() {
		if(CheckWifiStatus()){
			RestClient.postFieldReports();
		}
	}
	
	public void sendDamageReports() {
		if(CheckWifiStatus()){
			RestClient.postDamageReports();
		}
	}
	
	public void sendResourceRequests() {
		if(CheckWifiStatus()){
			RestClient.postResourceRequests();
		}
	}
	
	public void sendWeatherReports() {
		if(CheckWifiStatus()){
			RestClient.postWeatherReports();
		}
	}
	
	public void sendUxoReports() {
		if(CheckWifiStatus()){
			RestClient.postUxoReports();
		}
	}
	
	public void sendMarkupFeatures() {
		if(CheckWifiStatus()){
			RestClient.postMarkupFeatures();
		}
	}
	
	public void sendChatMessages() {
		if(CheckWifiStatus()){
			RestClient.postChatMessages();
		}
	}
	
	public void deleteAllMarkupFeatureStoreAndForward() {
		mDatabaseManager.deleteAllMarkupFeatureStoreAndForward();
	}

	public void deleteAllMarkupFeatureHistory() {
		mDatabaseManager.deleteAllMarkupFeatureHistory();
	}

	public ArrayList<ChatPayload> getAllChatStoreAndForward() {
		return mDatabaseManager.getAllChatStoreAndForward();
	}

	public void deleteChatStoreAndForward(long id) {
		mDatabaseManager.deleteChatStoreAndForward(id);
	}
	
	public void deleteAllChatStoreAndForward() {
		mDatabaseManager.deleteAllChatStoreAndForward();
	}
	
	 public void deleteAllChatHistory () {
		 mDatabaseManager.deleteAllChatHistory();
	 }

	public ArrayList<MobileDeviceTrackingPayload> getAllMDTStoreAndForward() {
		return mDatabaseManager.getAllMobileDeviceTrackingStoreAndForward();
	}

	public void deleteMDTStoreAndForward(long id) {
		mDatabaseManager.deleteMobileDeviceTrackingStoreAndForward(id);
	}

	public void setMarkupFeatures(ArrayList<MarkupFeature> features) {
		if(mMarkupFeatures == null) {
			mMarkupFeatures = new ArrayList<MarkupFeature>();
		}
		mMarkupFeatures.clear();
		mMarkupFeatures.addAll(features);
	}

	public void deleteMarkupHistoryForCollabroom(long collabroomId) {
		mDatabaseManager.deleteMarkupHistoryForCollabroom(collabroomId);
	}

	public void deleteMarkupHistoryForCollabroomByFeatureIds(long collabroomId, ArrayList<String> featuresToRemove) {
		mDatabaseManager.deleteMarkupHistoryForCollabroomByFeatureIds(collabroomId, featuresToRemove);
	}
	
	public void deleteMarkupHistoryForCollabroomByFeatureId(long collabroomId, String featureToRemove) {
		mDatabaseManager.deleteMarkupHistoryForCollabroomByFeatureId(collabroomId, featureToRemove);
	}

	public void addAllChatHistory(ArrayList<ChatPayload> payloads) {
		mDatabaseManager.addAllChatHistory(payloads);
	}

	public HashMap<String, IncidentPayload> getIncidents() {
		
		if(mIncidents == null){
			String json = mSharedPreferences.getPreferenceString(Constants.SAVED_INCIDENTS);
			
			IncidentMessage message = new Gson().fromJson(json, IncidentMessage.class);
			
			HashMap<String, IncidentPayload> incidents = new HashMap<String, IncidentPayload>();
			for(IncidentPayload incident : message.getIncidents()) {
				incidents.put(incident.getIncidentName(), incident);
			}
			mIncidents = incidents;
		}
		
		return mIncidents;
	}
	public void setIncidents(IncidentMessage message) {
		
		HashMap<String, IncidentPayload> incidents = new HashMap<String, IncidentPayload>();
		for(IncidentPayload incident : message.getIncidents()) {
			incidents.put(incident.getIncidentName(), incident);
		}
		mSharedPreferences.savePreferenceString(Constants.SAVED_INCIDENTS, message.toJsonString());
		
		mIncidents = incidents;
	}
		
	public void setCollabRoomsForIncident(long incidentId,CollaborationRoomMessage rooms){
		mSharedPreferences.savePreferenceString(Constants.SAVED_COLLABROOMS + incidentId, rooms.toJsonString());
	}
	
	public CollaborationRoomMessage getCollabRoomsForIncident(long incidentId){
		String savedRooms = mSharedPreferences.getPreferenceString(Constants.SAVED_COLLABROOMS + incidentId);
		if(savedRooms.equals(Constants.dcds_NO_RESULTS)){
			return null;
		}
		
		CollaborationRoomMessage message =  new GsonBuilder().create().fromJson(savedRooms, CollaborationRoomMessage.class);		
		return message;
	}	
	
	public void setOrganizations(OrganizationMessage organizations) {
		
		mSharedPreferences.savePreferenceString(Constants.SAVED_ORGANIZATIONS , organizations.toJsonString());
		
		if(mOrganizations == null) {
			mOrganizations = new HashMap<String, OrganizationPayload>();
		} else {
			mOrganizations.clear();
		}
		
		for(OrganizationPayload payload : organizations.getOrgs()) {
			mOrganizations.put(payload.getName(), payload);
		}
		
		if(getUserOrgId() == -1){
			setUserOrgId(organizations.getOrgs().get(0).getOrgid());
		}
		requestOrgCapabilitiesUpdate();
	}
	
	public HashMap<String, OrganizationPayload> getOrganizations() {
		
		if(mOrganizations == null){
			String json = mSharedPreferences.getPreferenceString(Constants.SAVED_ORGANIZATIONS);
			if(json.equals(Constants.dcds_NO_RESULTS)){
				return null;
			}
			
			OrganizationMessage message = new Gson().fromJson(json, OrganizationMessage.class);			
			mOrganizations = new HashMap<String, OrganizationPayload>();

			for(OrganizationPayload payload : message.getOrgs()) {
				mOrganizations.put(payload.getName(), payload);
			}
		}
		return mOrganizations;
	}
	
	public Context getActiveActivity() {
		return mActiveActivity;
	}
	
	public void setCurrentOrganization(OrganizationPayload mCurrentOrganization){
		
		mSharedPreferences.savePreferenceString(Constants.CURRENT_USER_ORG, mCurrentOrganization.toJsonString());
		this.mCurrentOrganization = mCurrentOrganization;
		mSharedPreferences.savePreferenceLong(Constants.USER_ORG_ID, mCurrentOrganization.getOrgid());
	}
	
	public OrganizationPayload getCurrentOrganziation(){
		
		if(mCurrentOrganization == null){ 
			String json = mSharedPreferences.getPreferenceString(Constants.CURRENT_USER_ORG);
			if(json.equals(Constants.dcds_NO_RESULTS)){
				return null;
			}
			mCurrentOrganization = new Gson().fromJson(json, OrganizationPayload.class);
		}
		return mCurrentOrganization;
	}
	
	public void setLoginData(LoginPayload loginPayload) {
		
		setUserId(loginPayload.getUserId());
		setUsername(loginPayload.getUsername());
		setWorkspaceId(loginPayload.getWorkspaceId());
		setUserSessionId(loginPayload.getUserSessionId());
		mSharedPreferences.savePreferenceLong(Constants.USER_ORG_ID, loginPayload.getOrgId());
	}
	
	public void setWorkspaceId(long workspaceId) {
		mSharedPreferences.savePreferenceLong(Constants.WORKSPACE_ID, (long)workspaceId);
	}

	public void setUserData(UserPayload userPayload) {
		mSharedPreferences.savePreferenceString(Constants.USER_DATA, userPayload.toJsonString());
	}
	
	public UserPayload getUserPayload() {
		UserPayload userDataPayload = null;
		
		String userDataString = mSharedPreferences.getPreferenceString(Constants.USER_DATA, null);
		
		if(userDataString != null) {
			userDataPayload = new Gson().fromJson(userDataString, UserPayload.class);
		}
		
		return userDataPayload;
	}
	
	public void setCurrentIncidentData(IncidentPayload incident, long collabRoomId, String collabRoomName) {
		if(incident != null) {
			mSharedPreferences.savePreferenceLong(Constants.INCIDENT_ID, incident.getIncidentId());
			mSharedPreferences.savePreferenceString(Constants.INCIDENT_NAME, incident.getIncidentName());
			mSharedPreferences.savePreferenceString(Constants.INCIDENT_LATITUDE, String.valueOf(incident.getLat()));
			mSharedPreferences.savePreferenceString(Constants.INCIDENT_LONGITUDE, String.valueOf(incident.getLon()));
		} else {
			mSharedPreferences.removePreference(Constants.INCIDENT_ID);
			mSharedPreferences.removePreference(Constants.INCIDENT_NAME);
			mSharedPreferences.removePreference(Constants.INCIDENT_LATITUDE);
			mSharedPreferences.removePreference(Constants.INCIDENT_LONGITUDE);
		}
		
		if(collabRoomId != -1) {
			mSharedPreferences.savePreferenceLong(Constants.COLLABROOM_ID, collabRoomId);
			mSharedPreferences.savePreferenceString(Constants.COLLABROOM_NAME, collabRoomName);
		} else {
			mSharedPreferences.removePreference(Constants.COLLABROOM_ID);
			mSharedPreferences.removePreference(Constants.COLLABROOM_NAME);
		}
	}
	
	public long getUserId() {
		return mSharedPreferences.getPreferenceLong(Constants.USER_ID);
	}
	
	public void setUserId(long userId){
		mSharedPreferences.savePreferenceLong(Constants.USER_ID, userId);
	}

	public long getUserOrgId() {
		return mSharedPreferences.getPreferenceLong(Constants.USER_ORG_ID);
	}
	
	public void setUserOrgId(long id){
		mSharedPreferences.savePreferenceLong(Constants.USER_ORG_ID, id);
	}
	
	public long getWorkspaceId() {
		return mSharedPreferences.getPreferenceLong(Constants.WORKSPACE_ID);
	}

	public void setUserSessionId(long userSessionID) {
		mSharedPreferences.savePreferenceLong(Constants.USER_SESSION_ID,userSessionID);
	}
	
	public long getUserSessionId() {
		return mSharedPreferences.getPreferenceLong(Constants.USER_SESSION_ID);
	}

	public void setUsername(String username) {
		mSharedPreferences.savePreferenceString(Constants.USER_NAME, username);
	}
	
	public String getUsername() {
		return mSharedPreferences.getPreferenceString(Constants.USER_NAME);
	}
	
	public void setPassword(String password) {
		mSharedPreferences.savePreferenceString(Constants.PASSWORD, password);
	}
	
	public String getPassword() {
		return mSharedPreferences.getPreferenceString(Constants.PASSWORD);
	}
	
	public void setAutoLogin(boolean autoLogin){
		mSharedPreferences.savePreferenceBoolean(Constants.dcds_AUTO_LOGIN, autoLogin);
	}
	
	public boolean getAutoLogin(){
		return mSharedPreferences.getPreferenceBoolean(Constants.dcds_AUTO_LOGIN,"false");
	}

	public String getUserDataJsonString() {
		return new Gson().toJson(getUserPayload(), UserPayload.class);
	}
	
	public long getActiveCollabroomId() {
		return mSharedPreferences.getPreferenceLong(Constants.COLLABROOM_ID);
	}
	
	public CollabroomPayload getPreviousCollabroom() {
	
		String collabroomString = mSharedPreferences.getPreferenceString(Constants.PREVIOUS_COLLABROOM, "");
		if(!collabroomString.equals("")){
			return new Gson().fromJson(collabroomString, CollabroomPayload.class);
		}
		else{
			CollabroomPayload newPayload = new CollabroomPayload();
			newPayload.setCollabRoomId(-1);
			newPayload.setName(getContext().getResources().getString(R.string.no_selection));
			return newPayload;
		}
	}
	
	public String getActiveCollabroomName() {
		return mSharedPreferences.getPreferenceString(Constants.COLLABROOM_NAME);
	}
	
	public long getActiveIncidentId() {
		long incidentId = mSharedPreferences.getPreferenceLong(Constants.INCIDENT_ID);		
		return incidentId;
	}
	
	public long getPreviousIncidentId() {
		return mSharedPreferences.getPreferenceLong(Constants.PREVIOUS_INCIDENT_ID);
	}
	
	public String getActiveIncidentName() {
		return mSharedPreferences.getPreferenceString(Constants.INCIDENT_NAME, getContext().getResources().getString(R.string.no_selection));
	}

	public void setHeartRate(Double hr, Integer confidence) {
		if(hr != null) {
			mSharedPreferences.savePreferenceFloat(Constants.LAST_HR, hr.floatValue());
		} else {
			mSharedPreferences.removePreference(Constants.LAST_HR);
		}
	}
	
	public double getHeartRate() {
		return mSharedPreferences.getPreferenceFloat(Constants.LAST_HR, "-1.0");
	}
	
	public void setHSI(Double hsi) {
		if(hsi != null) {
			mSharedPreferences.savePreferenceFloat(Constants.LAST_HSI, hsi.floatValue());
		} else {
			mSharedPreferences.removePreference(Constants.LAST_HSI);
		}
	}
	
	public double getHSI() {
		return mSharedPreferences.getPreferenceFloat(Constants.LAST_HSI, "-1.0");
	}
	
	public void setMDT(Location location) {
		
		if(isMDTEnabled()) {
						
			mSharedPreferences.savePreferenceLong(Constants.LAST_MDT_TIME, location.getTime());
			mSharedPreferences.savePreferenceString(Constants.LAST_LATITUDE, String.valueOf(location.getLatitude()));
			mSharedPreferences.savePreferenceString(Constants.LAST_LONGITUDE, String.valueOf(location.getLongitude()));
			mSharedPreferences.savePreferenceString(Constants.LAST_ALTITUDE, String.valueOf(location.getAltitude()));
			mSharedPreferences.savePreferenceString(Constants.LAST_ACCURACY, String.valueOf(location.getAccuracy()));
			mSharedPreferences.savePreferenceString(Constants.LAST_COURSE, String.valueOf(location.getBearing()));
			
			MobileDeviceTrackingPayload payload = new MobileDeviceTrackingPayload();
			payload.setLatitude(location.getLatitude());
			payload.setLongitude(location.getLongitude());
			payload.setAltitude(location.getAltitude());
			payload.setAccuracy(location.getAccuracy());
			payload.setCourse(location.getBearing());
			payload.setProvider(location.getProvider());
			payload.setSpeed(location.getSpeed());
			payload.setUserId(getUserId());
			payload.setDeviceId(RestClient.getDeviceId());
			payload.setIncidentId(getActiveIncidentId());
			payload.setSensorTimestamp(location.getTime());
			payload.setUserHealth(new UserHealth(getHeartRate(), getHSI()));
			
			long timeNow = System.currentTimeMillis();
			payload.setCreatedUTC(timeNow);
			payload.setLastUpdatedUTC(timeNow);
			
			if(getMDTLatitude() != location.getLatitude() && getMDTLongitude() != location.getLongitude() && getMDTAccuracy() != location.getAccuracy()) {
				this.addPersonalHistory("Sending MDT: " + getUserNickname() + " - Source: " + location.getProvider() + " (" + location.getLatitude() + ", " + location.getLongitude() + ", " + location.getAltitude() + ", " + location.getBearing() + ") - Accuracy: +/- " + location.getAccuracy() + " meters.");
			}
			
			if(isLoggedIn) {
				mDatabaseManager.addMobileDeviceTrackingStoreAndForward(payload);
				RestClient.postMDTs();
			}
		}
	}
	
	public long getMDTTime() {
		return mSharedPreferences.getPreferenceLong(Constants.LAST_MDT_TIME);
	}
	
	public double getMDTLatitude() {
		return Double.valueOf(mSharedPreferences.getPreferenceString(Constants.LAST_LATITUDE,"NaN"));
	}

	public double getMDTLongitude() {
		return Double.valueOf(mSharedPreferences.getPreferenceString(Constants.LAST_LONGITUDE,"NaN"));
	}

	public double getMDTAltitude() {
		return Double.valueOf(mSharedPreferences.getPreferenceString(Constants.LAST_ALTITUDE,"NaN"));
	}
	
	public double getMDTCourse() {
		return Double.valueOf(mSharedPreferences.getPreferenceString(Constants.LAST_COURSE,"NaN"));
	}
	
	public double getMDTAccuracy() {
		return Double.valueOf(mSharedPreferences.getPreferenceString(Constants.LAST_ACCURACY,"NaN"));
	}
	
	public double getIncidentPositionLatitude() {
		return Double.valueOf(mSharedPreferences.getPreferenceString(Constants.INCIDENT_LATITUDE,"NaN"));
	}

	public double getIncidentPositionLongitude() {
		return Double.valueOf(mSharedPreferences.getPreferenceString(Constants.INCIDENT_LONGITUDE,"NaN"));
	}

	public String getUserNickname() {
		UserPayload userData = getUserPayload();

		if(userData != null) {
			return userData.getFirstName() + " " + userData.getLastName();
		} else {
			return "Unknown User";
		}
	}

	public void setWeather(WeatherPayload payload) {
		mSharedPreferences.savePreferenceString(Constants.WEATHER_PAYLOAD, payload.toJsonString());
	}
	
	public WeatherPayload getWeather() {
		String payloadJson = mSharedPreferences.getPreferenceString(Constants.WEATHER_PAYLOAD);
		
		if(payloadJson != null) {
			return new Gson().fromJson(payloadJson, WeatherPayload.class);
		} else {
			return null;
		}
	}
	
	public String getServer() {
		if(mGlobalPreferences.getBoolean("custom_server_enabled", false)) {
			return mGlobalPreferences.getString("custom_server_url", getContext().getResources().getString(R.string.config_server_default));
		}
		
		return mGlobalPreferences.getString("server_list", getContext().getResources().getString(R.string.config_server_default));
	}
	
	public void setIplanetCookieDomain(String value){
		mSharedPreferences.savePreferenceString(Constants.IPLANET_COOKIE_DOMAIN, value);
	}
	public String getIplanetCookieDomain(){
		if(isCustomDomainEnabled()){
			return mGlobalPreferences.getString("custom_cookie_domain","");
		}else{
			return mSharedPreferences.getPreferenceString(Constants.IPLANET_COOKIE_DOMAIN, getContext().getResources().getString(R.string.config_iplanet_cookie_domain_default));
		}
	}
	
	public void setAmAuthCookieDomain(String value){
		mSharedPreferences.savePreferenceString(Constants.AMAUTH_COOKIE_DOMAIN, value);
	}
	public String getAmAuthCookieDomain(){
		if(isCustomDomainEnabled()){
			return mGlobalPreferences.getString("custom_cookie_domain","");
		}else{
			return mSharedPreferences.getPreferenceString(Constants.AMAUTH_COOKIE_DOMAIN, getContext().getResources().getString(R.string.config_amauth_cookie_domain_default));	
		}
	}
	
	public boolean isCustomDomainEnabled() {
		return mGlobalPreferences.getBoolean("custom_domain_name_enabled", false);
	}
	
	public boolean isMDTEnabled() {
		return mGlobalPreferences.getBoolean("tracking_checkbox", true);
	}
	
	public boolean isLRFEnabled() {
		return mGlobalPreferences.getBoolean("lrf_checkbox", false);
	}
	
	public boolean isPushNotificationsDisabled(){
		return mGlobalPreferences.getBoolean("disable_notifications_checkbox", false);
	}
	
	public boolean isDebugEnabled() {
		return mGlobalPreferences.getBoolean("debug_checkbox", false);
	}
	public boolean isSyncWifiOnlyEnabled(){
		return mGlobalPreferences.getBoolean("tracking_sync_over_wifi_only_checkbox", false);
	}
	
	public int getIncidentDataRate() {
		if(LowDataMode){
			return LowDataRate;
		}else{
			return Integer.parseInt(mGlobalPreferences.getString("incident_sync_frequency", "60"));
		}
	}
	
	public int getCollabroomDataRate() {
		if(LowDataMode){
			return LowDataRate;
		}else{
			return Integer.parseInt(mGlobalPreferences.getString("collabroom_sync_frequency", "30"));
		}
	}
	
	public int getMDTDataRate() {
	if(LowDataMode){
			return LowDataRate;
		}else{
			return Integer.parseInt(mGlobalPreferences.getString("mdt_sync_frequency", "60"));
		}
	}
	
	public int getWFSDataRate() {
	if(LowDataMode){
			return LowDataRate;
		}else{
			return Integer.parseInt(mGlobalPreferences.getString("wfs_sync_frequency", "90"));
		}
	}
	
	public int getCoordinateRepresentation() {
		return Integer.parseInt(mGlobalPreferences.getString("coordinate_representation", "0"));
	}
	
	public String getGeoServerURL() {
		
		if(mGlobalPreferences.getBoolean("custom_geo_server_enabled", false)) {
			return mGlobalPreferences.getString("custom_geo_server_url", getContext().getResources().getString(R.string.config_geo_server_default));
		}
		
		return mGlobalPreferences.getString("geo_server_list", getContext().getResources().getString(R.string.config_geo_server_default));
	}
	
	public String getAuthServerURL() {
	
		if(mGlobalPreferences.getBoolean("custom_auth_server_enabled", false)) {
			return mGlobalPreferences.getString("custom_auth_server_url", getContext().getResources().getString(R.string.config_auth_server_default));
		}
		
		return mGlobalPreferences.getString("auth_server_list", getContext().getResources().getString(R.string.config_auth_server_default));
		
	}
	
	public String getAuthToken() {
		return mSharedPreferences.getPreferenceString("auth_token", null);
	}

	public void setAuthToken(String tokenId) {
		if(tokenId == null) {
			mSharedPreferences.removePreference("auth_token");
		} else {
			mSharedPreferences.savePreferenceString("auth_token", tokenId);
		}
	}
	
	public String getSelectedLanguage(){
		String code = mGlobalPreferences.getString("language_select_list","Device Default");
		if(code.equals("Device Default")){
			code = Locale.getDefault().getISO3Language().substring(0,2);
		}
		return code;
	}

	public void setLowDataMode(boolean value){
		LowDataMode = value;
		
		//refresh rates
		requestFieldReportRepeating(getIncidentDataRate(),false);
		requestDamageReportRepeating(getIncidentDataRate(),false);
		requestSimpleReportRepeating(getIncidentDataRate(),false);
		requestResourceRequestRepeating(getIncidentDataRate(),false);
		requestWeatherReportRepeating(getIncidentDataRate(),false);
		requestMarkupRepeating(getCollabroomDataRate(),false);
		requestChatMessagesRepeating(getCollabroomDataRate(),false);
	}
	public boolean getLowDataMode(){
		return LowDataMode;
	}
	
	public void setCurrentNavigationView(String newView){
		currentNavigationView = newView;
	}
	public String getCurrentNavigationView(){
		return currentNavigationView;
	}
	public void setCurrentLocale(String newLanguage){
		mLocale = new Locale(newLanguage); 
	    Resources res = getContext().getResources(); 
	    DisplayMetrics dm = res.getDisplayMetrics(); 
	    Configuration conf = res.getConfiguration(); 
	    conf.locale = mLocale; 
	    res.updateConfiguration(conf, dm);
	}
	
	public void setSupportedLanguages(String[] languages){
		supportedLanguages = languages;
	}
	public String[] getSupportedLanguages(){
		return supportedLanguages;
	}
	public boolean getTabletLayoutOn(){
		if(isTabletLayout == null){
			isTabletLayout = mSharedPreferences.getPreferenceBoolean("tablet_layout", "false");
		}
		return isTabletLayout;
	}
	public void setTabletLayoutOn(boolean value){
		isTabletLayout = value;
		mSharedPreferences.savePreferenceBoolean("tablet_layout", value);
	}
	public boolean isNewGeneralMessageAvailable(){
		return newGeneralMessageAvailable;
	}
	public void setNewGeneralMessageAvailable(boolean available){
		newGeneralMessageAvailable = available;
	}
	public boolean isNewReportAvailable() {
		return newReportAvailable;
	}
	public void setNewReportAvailable(boolean newReportAvailable) {
		this.newReportAvailable = newReportAvailable;
	}
	public boolean isNewchatAvailable() {
		return newchatAvailable;
	}
	public void setNewchatAvailable(boolean newchatAvailable) {
		this.newchatAvailable = newchatAvailable;
	}
	public boolean isNewMapAvailable() {
		return newMapAvailable;
	}
	public void setNewMapAvailable(boolean newMapAvailable) {
		this.newMapAvailable = newMapAvailable;
	}
	private void initializeTrackingLayers(){
		
			TrackingLayers = new ArrayList<TrackingLayerPayload>();
			
			TrackingLayerPayload damagePayload = new TrackingLayerPayload();
			damagePayload.setDisplayname(mContext.getString(R.string.wfslayer_dcds_damage_report_title));
			damagePayload.setLayername("dcds_dmgrpt");
			damagePayload.setInternalurl(getGeoServerURL());
			
			TrackingLayerPayload generalPayload = new TrackingLayerPayload();
			generalPayload.setDisplayname(mContext.getString(R.string.wfslayer_dcds_simple_report_title));
			generalPayload.setLayername("dcds_sr");
			generalPayload.setInternalurl(getGeoServerURL());
			
			TrackingLayerPayload uxoPayload = new TrackingLayerPayload();
			uxoPayload.setDisplayname(mContext.getString(R.string.wfslayer_dcds_uxo_report_title));
			uxoPayload.setLayername("dcds_urrpt");	
			uxoPayload.setInternalurl(getGeoServerURL());
			
			TrackingLayers.add(generalPayload);
			TrackingLayers.add(damagePayload);
			TrackingLayers.add(uxoPayload);
	}
	public ArrayList<TrackingLayerPayload>  getTrackingLayers() {
		if(TrackingLayers == null){
			initializeTrackingLayers();
		}
		return TrackingLayers;
	}
	public void setTrackingLayers(ArrayList<TrackingLayerPayload>  trackingLayers) {
		initializeTrackingLayers();
		if(trackingLayers != null){
			TrackingLayers.addAll(trackingLayers);
		}
	}
	public boolean UpdateTrackingLayerData(TrackingLayerPayload updatedTrackingLayer){
	
		for(int i = 0; i < TrackingLayers.size();i++){
			TrackingLayerPayload layer = TrackingLayers.get(i);
			if(layer.getLayername().equals(updatedTrackingLayer.getDisplayname())){
				TrackingLayers.set(i, updatedTrackingLayer);
				return true;
			}
		}
		return false;
	}

	public long getLastSuccesfulServerCommsTimestamp() {
		return lastSuccesfulServerCommsTimestamp;
	}

	public void setLastSuccesfulServerCommsTimestamp( long lastSuccesfulServerCommsTimestamp) {
		this.lastSuccesfulServerCommsTimestamp = lastSuccesfulServerCommsTimestamp;
	}
    public void CheckLastTimeServerWasPinged(){
    	if(checkIfOnline()){
	    	if((System.currentTimeMillis() - lastSuccesfulServerCommsTimestamp) >= timeTillDisconnect){
	    		switchToOfflineMode();
	    	}
    	}else{
    		if(getUsername() != Constants.dcds_NO_RESULTS && getPassword() != Constants.dcds_NO_RESULTS){
				if(AuthManager.getInstance()!=null){
	    			if(AuthManager.getInstance().getClient()!= null){
	    				RestClient.attemptLogin(getUsername(),getPassword(),false);	
	    			}else{
	    				requestLogin(getUsername(),getPassword());
	    			}
				}else{
					if(getAutoLogin()){
						requestLogin(getUsername(),getPassword());
					}
				}
    		}
    	}
    }
}
