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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.client.HttpResponseException;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings.Secure;
import android.util.Log;
import android.util.SparseArray;

import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import edu.mit.ll.dcds.android.api.data.DamageReportData;
import edu.mit.ll.dcds.android.api.data.ReportSendStatus;
import edu.mit.ll.dcds.android.api.data.UxoReportData;
import edu.mit.ll.dcds.android.api.data.MarkupFeature;
import edu.mit.ll.dcds.android.api.data.SimpleReportData;
import edu.mit.ll.dcds.android.api.handlers.ChatResponseHandler;
import edu.mit.ll.dcds.android.api.handlers.DamageReportNoImageResponseHandler;
import edu.mit.ll.dcds.android.api.handlers.DamageReportResponseHandler;
import edu.mit.ll.dcds.android.api.handlers.FieldReportResponseHandler;
import edu.mit.ll.dcds.android.api.handlers.SimpleReportNoImageResponseHandler;
import edu.mit.ll.dcds.android.api.handlers.UxoReportNoImageResponseHandler;
import edu.mit.ll.dcds.android.api.handlers.WeatherReportResponseHandler;
import edu.mit.ll.dcds.android.api.handlers.MDTResponseHandler;
import edu.mit.ll.dcds.android.api.handlers.MarkupResponseHandler;
import edu.mit.ll.dcds.android.api.handlers.ResourceRequestResponseHandler;
import edu.mit.ll.dcds.android.api.handlers.SimpleReportResponseHandler;
import edu.mit.ll.dcds.android.api.handlers.UxoReportResponseHandler;
import edu.mit.ll.dcds.android.api.messages.ChatMessage;
import edu.mit.ll.dcds.android.api.messages.CollaborationRoomMessage;
import edu.mit.ll.dcds.android.api.messages.DamageReportMessage;
import edu.mit.ll.dcds.android.api.messages.FieldReportMessage;
import edu.mit.ll.dcds.android.api.messages.TrackingLayerMessage;
import edu.mit.ll.dcds.android.api.messages.WeatherReportMessage;
import edu.mit.ll.dcds.android.api.messages.UxoReportMessage;
import edu.mit.ll.dcds.android.api.messages.IncidentMessage;
import edu.mit.ll.dcds.android.api.messages.LoginMessage;
import edu.mit.ll.dcds.android.api.messages.MarkupMessage;
import edu.mit.ll.dcds.android.api.messages.OrganizationMessage;
import edu.mit.ll.dcds.android.api.messages.ResourceRequestMessage;
import edu.mit.ll.dcds.android.api.messages.SimpleReportMessage;
import edu.mit.ll.dcds.android.api.messages.UserMessage;
import edu.mit.ll.dcds.android.api.payload.ChatPayload;
import edu.mit.ll.dcds.android.api.payload.CollabroomPayload;
import edu.mit.ll.dcds.android.api.payload.LoginPayload;
import edu.mit.ll.dcds.android.api.payload.MarkupPayload;
import edu.mit.ll.dcds.android.api.payload.MobileDeviceTrackingPayload;
import edu.mit.ll.dcds.android.api.payload.TrackingLayerPayload;
import edu.mit.ll.dcds.android.api.payload.TrackingTokenPayload;
import edu.mit.ll.dcds.android.api.payload.WeatherPayload;
import edu.mit.ll.dcds.android.api.payload.forms.DamageReportPayload;
import edu.mit.ll.dcds.android.api.payload.forms.FieldReportPayload;
import edu.mit.ll.dcds.android.api.payload.forms.ResourceRequestPayload;
import edu.mit.ll.dcds.android.api.payload.forms.SimpleReportPayload;
import edu.mit.ll.dcds.android.api.payload.forms.WeatherReportPayload;
import edu.mit.ll.dcds.android.api.payload.forms.UxoReportPayload;
import edu.mit.ll.dcds.android.api.tasks.ParseChatMessagesTask;
import edu.mit.ll.dcds.android.api.tasks.ParseDamageReportsTask;
import edu.mit.ll.dcds.android.api.tasks.ParseFieldReportsTask;
import edu.mit.ll.dcds.android.api.tasks.ParseWeatherReportsTask;
import edu.mit.ll.dcds.android.api.tasks.ParseUxoReportsTask;
import edu.mit.ll.dcds.android.api.tasks.ParseMarkupFeaturesTask;
import edu.mit.ll.dcds.android.api.tasks.ParseResourceRequestsTask;
import edu.mit.ll.dcds.android.api.tasks.ParseSimpleReportsTask;
import edu.mit.ll.dcds.android.auth.AuthManager;
import edu.mit.ll.dcds.android.auth.providers.OpenAMAuthProvider;
import edu.mit.ll.dcds.android.utils.Constants;
import edu.mit.ll.dcds.android.utils.EncryptedPreferences;
import edu.mit.ll.dcds.android.utils.Intents;

@SuppressWarnings("unchecked")
public class RestClient {
    private static Context mContext = null;
    private static DataManager mDataManager = null;
    private static GsonBuilder mBuilder = new GsonBuilder();
    private static Header[] mAuthHeader = null;
    
    private static AsyncTask<ArrayList<ChatPayload>, Object, Integer> mParseChatMessagesTask;
    private static AsyncTask<ArrayList<SimpleReportPayload>, Object, Integer> mParseSimpleReportsTask;
    private static AsyncTask<ArrayList<FieldReportPayload>, Object, Integer> mParseFieldReportsTask;
    private static AsyncTask<ArrayList<DamageReportPayload>, Object, Integer> mParseDamageReportsTask;
    private static AsyncTask<ArrayList<ResourceRequestPayload>, Object, Integer> mParseResourceRequestTask;
    private static AsyncTask<ArrayList<WeatherReportPayload>, Object, Integer> mParseWeatherReportsTask;
    private static AsyncTask<ArrayList<UxoReportPayload>, Object, Integer> mParseUxoReportsTask;
    private static AsyncTask<MarkupPayload, Object, Integer> mParseMarkupFeaturesTask;
    
    private static SparseArray<SimpleReportResponseHandler> mSimpleReportResponseHandlers;
    private static SparseArray<DamageReportResponseHandler> mDamageReportResponseHandlers;
    private static SparseArray<UxoReportResponseHandler> mUxoReportResponseHandlers;

	private static String mDeviceId;

	public static boolean mIsAttemptingLogin = false;
	private static boolean mSendingSimpleReports;
	private static boolean mSendingFieldReports;
	private static boolean mSendingDamageReports;
	private static boolean mSendingResourceRequests;
	private static boolean mSendingWeatherReports;
	private static boolean mSendingUxoReports;
	private static boolean mSendingChatMessages;
	private static boolean mSendingMarkupFeatures;

	private static boolean mFetchingSimpleReports;
	private static boolean mFetchingFieldReports;
	private static boolean mFetchingDamageReports;
	private static boolean mFetchingResourceRequests;
	private static boolean mFetchingWeatherReports;
	private static boolean mFetchingUxoReports;
	private static boolean mFetchingChatMessages;
	private static boolean mFetchingMarkupFeatures;
	
	private static AuthManager mAuthManager;   
            
	 public static void attemptLogin(final String username,final String password, final boolean getActiveAssignment) {
	    	
	    	new Thread(new Runnable() {
				@Override
				public void run() {
					Looper.prepare();
	    	
			    	try {
			    		mIsAttemptingLogin = true;
			    		
						mDataManager = DataManager.getInstance(mContext);
			    					
						mDeviceId = Build.SERIAL;
						if(mDeviceId == null) {
							mDeviceId = Secure.getString(mContext.getContentResolver(), Secure.ANDROID_ID);
						}
			
						mSimpleReportResponseHandlers = new SparseArray<SimpleReportResponseHandler>();
						mDamageReportResponseHandlers = new SparseArray<DamageReportResponseHandler>();
						mUxoReportResponseHandlers = new SparseArray<UxoReportResponseHandler>();
						
				    	LoginPayload p = new LoginPayload(username);
			
				    	p.setWorkspaceId(mDataManager.getWorkspaceId());
				    	
						StringEntity entity = new StringEntity(p.toJsonString());
						
						mDataManager.setUsername(username);

						Log.d("RestClient", "RestClient posting login");
														
						mAuthManager.getClient().post("login", entity, new AsyncHttpResponseHandler() {
				
							@Override
							public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
								mDataManager.setLastSuccesfulServerCommsTimestamp(System.currentTimeMillis());
								String content = (responseBody != null) ? new String(responseBody) : "error";
								
								//if you don't properly logout then the api doesn't handle the usersession properly and sends back "error" next time you try to log in.
								//Until the APi sends back the response that the user is already logged in. I am currently catching the error and logging out, then logging back in.
								if(content.equals("error")){
									logout(username, true, getActiveAssignment);
									return;
								}
								
								mDataManager.setPassword(password);
						        mDataManager.setLoggedIn(true);
								Log.i("dcdsRest", "Successfully logged in as: " + username + " status code: " + statusCode );
								
								mAuthHeader = headers;
								
								LoginMessage message = mBuilder.create().fromJson(content, LoginMessage.class);
								LoginPayload payload = message.getLoginPayload().get(0);
								
								mDataManager.setLoginData(payload);
								
						        Intent intent = new Intent();
						        intent.setAction(Intents.dcds_SUCCESSFUL_LOGIN);
						        intent.putExtra("payload", message.toJsonString());
						        mContext.sendBroadcast (intent);
						        
						        getUserOrgs(payload.getUserId());
								getAllIncidents(payload.getUserId());
								getUserData(payload.getUserId());
								mDataManager.requestOrgCapabilitiesUpdate();
								
								if(mDataManager.getActiveIncidentId() != -1){
									getCollabRooms(mDataManager.getActiveIncidentId());
								}
												
								mDataManager.addPersonalHistory("User " + username + " logged in successfully. ");
								mDataManager.switchToOnlineMode();
								mDataManager.sendAllLocalContent();
								mIsAttemptingLogin = false;
							}
			
							@Override
							public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
								String content = (responseBody != null) ? new String(responseBody) : "error";
								
						        mDataManager.setLoggedIn(false);
						        mDataManager.stopPollingAlarms();
						        mIsAttemptingLogin = false;
						        
								boolean broadcast = true;
								
						        Intent intent = new Intent(); 
						        intent.setAction(Intents.dcds_FAILED_LOGIN);
			
								if(error.getClass() == HttpResponseException.class) {
									HttpResponseException exception = (HttpResponseException)error;
									
									if(exception.getStatusCode() == 412) {
										broadcast = false;
										LoginMessage message = mBuilder.create().fromJson(content, LoginMessage.class);
										Log.w("dcdsRest", message.getMessage());
										logout(username, true, getActiveAssignment);
									} else if(exception.getStatusCode() == 401) {
								        intent.putExtra("message", "Invalid username or password");
									} else {
								        intent.putExtra("message", exception.getMessage());
									}
								} else {
									
									if(error.getMessage()!= null){
										Log.e("dcdsRest", error.getMessage());
									}else{
										Log.e("dcdsRest", "null error on failed login attempt");
									}
									intent.putExtra("offlineMode", true);
									
									if(error.getClass() == UnknownHostException.class) {
										 intent.putExtra("message", "Failed to connect to server. Please check your network connection.");	
									} else {
										if(error.getMessage()!= null){
											intent.putExtra("message", error.getMessage());
										}else{
											Log.e("dcdsRest", "null error on failed login attempt");
										}
									}
									error.printStackTrace();
								}
								
								if(broadcast) {
							        mContext.sendBroadcast (intent);
							        
							        if(intent.getExtras() != null) {
							        	mDataManager.addPersonalHistory("User " + username + " login failed: " + intent.getExtras().get("message"));
							        } else {
							        	mDataManager.addPersonalHistory("User " + username + " login failed.");
							        }
								}
							}
						});
		
				    	
			    	} catch (UnsupportedEncodingException e) {
			    		Log.e("dcdsRest", e.getLocalizedMessage());
			    		
			    		Intent intent = new Intent();
			    	    intent.setAction(Intents.dcds_FAILED_LOGIN);
			    	    
			    		intent.putExtra("offlineMode", true);
			    	    intent.putExtra("message", "Failed to connect to server: " + e.getLocalizedMessage() + " - Please check your network connection.");
			    	    mContext.sendBroadcast (intent);
			    	    mIsAttemptingLogin = false;
			    	}
					
					Looper.loop();
		    	}
			}).start();  	
	    }
    
	public static void login(final Context context,String username, String password, final boolean getActiveAssignment) {
		if(mIsAttemptingLogin){
			return;
		}
		
		mContext = context;
		
		mAuthManager = AuthManager.getInstance(username, password);
		if(mAuthManager.getClient() == null){
			mAuthManager.registerAuthType(new OpenAMAuthProvider(mContext));		//enable for openAM
		}else{
			if(mAuthManager.getClient().mIsAuthenticating){
				return;
			}
		}
	}

	public static void logout(final String username, final boolean retryLogin, final boolean getActiveAssignment) {
		
		mDataManager.setLoggedIn(false);
		mDataManager.setAutoLogin(false);
		mDataManager.setPassword(null);
		
		if(mAuthManager != null) {
			
			final String tempPasswordForRetry= mDataManager.getPassword(); 
			
			mAuthManager.getClient().delete("login/" + username, new AsyncHttpResponseHandler() {
			
				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
					mDataManager.setLastSuccesfulServerCommsTimestamp(System.currentTimeMillis());
					
					Log.i("dcdsRest", "Successfully logged out: " + username);
					mIsAttemptingLogin = false;
					
					mFetchingChatMessages = false;
					mFetchingFieldReports = false;
					mFetchingDamageReports = false;
					mFetchingMarkupFeatures = false;
					mFetchingResourceRequests = false;
					mFetchingSimpleReports = false;
					mFetchingUxoReports = false;
					
					mSendingChatMessages = false;
					mSendingFieldReports = false;
					mSendingDamageReports = false;
					mSendingMarkupFeatures = false;
					mSendingResourceRequests = false;
					mSendingSimpleReports = false;
					mSendingUxoReports = false;
					
					clearParseChatMessagesTask();
					clearParseFieldReportTask();
					clearParseDamageReportTask();
					clearParseMarkupFeaturesTask();
					clearParseResourceRequestTask();
					clearParseSimpleReportTask();
					clearParseUxoReportTask();
					
					
					mDataManager.stopPollingAlarms();
					AuthManager.clearAuthTypes();
					
					if(retryLogin) {
						login(mContext, username, tempPasswordForRetry, getActiveAssignment);
					} else {
						mDataManager.addPersonalHistory("User " + username + " logged out successfully. ");
					}
				}
	
				@Override
				public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
					mAuthManager.getClient().clearAuthData();
					mIsAttemptingLogin = false;
					Log.e("dcdsRest", "Failed to log out: " + username);
				}
			});
		}
	}

	public static void deleteMarkup(final String featureId) {
		if(!mDataManager.isLoggedIn()){
			return;
		}
		
		mAuthManager.getClient().delete("mapmarkups/" + mDataManager.getWorkspaceId() + "/" + featureId, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				mDataManager.setLastSuccesfulServerCommsTimestamp(System.currentTimeMillis());
				Log.i("dcdsRest", "Successfully deleted feature: " + featureId);
				mDataManager.addPersonalHistory("Successfully deleted feature: " + featureId);
				mDataManager.deleteMarkupHistoryForCollabroomByFeatureId(mDataManager.getSelectedCollabRoom().getCollabRoomId(), featureId);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

				Log.e("dcdsRest", "Failed to delete out: " + featureId);
				mDataManager.addPersonalHistory("Failed to delete out: " + featureId);
			}
		});
	}
	
	public static void getUserData(long userId) {
		if(!mDataManager.isLoggedIn()){
			return;
		}
		mAuthManager.getClient().get("users/" + mDataManager.getWorkspaceId() + "/" + userId, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				mDataManager.setLastSuccesfulServerCommsTimestamp(System.currentTimeMillis());
				String content = (responseBody != null) ? new String(responseBody) : "error";
				
				UserMessage message = mBuilder.create().fromJson(content, UserMessage.class);
				if(message.getCount() > 0) {
					mDataManager.setUserData(message.getUsers().get(0));
					Log.i("dcdsRest", "Successfully received user information.");
					mDataManager.addPersonalHistory("Successfully received user information.");
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				String content = (responseBody != null) ? new String(responseBody) : "error";
				
				Log.e("dcdsRest", content);
				
				Intent intent = new Intent();
			    intent.setAction(Intents.dcds_FAILED_LOGIN);
			    
				intent.putExtra("offlineMode", true);
		        intent.putExtra("message", "Failed to connect to server: " + content + " - Please check your network connection.");
		        mDataManager.addPersonalHistory("Failed to receive user information.");
		        mContext.sendBroadcast (intent);
			}
		});
	}
	
	public static void getUserOrgs(long userId) {
		if(!mDataManager.isLoggedIn()){
			return;
		}
		mAuthManager.getClient().get("orgs/" + mDataManager.getWorkspaceId() + "?userId=" + userId, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				mDataManager.setLastSuccesfulServerCommsTimestamp(System.currentTimeMillis());
				String content = (responseBody != null) ? new String(responseBody) : "error";
				
				OrganizationMessage message = mBuilder.create().fromJson(content, OrganizationMessage.class);
				if(message.getCount() > 0) {
					mDataManager.setOrganizations(message);
					
					EncryptedPreferences preferences;
					preferences = new EncryptedPreferences(mContext.getSharedPreferences(Constants.dcds_USER_PREFERENCES, 0));
					preferences.savePreferenceString("savedOrgs", message.toJsonString());
					
			        Intent intent = new Intent();
			        intent.setAction(Intents.dcds_SUCCESSFUL_GET_USER_ORGANIZATION_INFO);
			        intent.putExtra("payload", message.toJsonString());
			        mContext.sendBroadcast (intent);
			        
					Log.i("dcdsRest", "Successfully received user organization information.");
					mDataManager.addPersonalHistory("Successfully received user organization information.");
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				
				if(error.getClass().equals(HttpResponseException.class)) {
					HttpResponseException exception = (HttpResponseException)error;
					if(exception.getStatusCode() == 404) {
						Intent intent = new Intent();
				        intent.setAction(Intents.dcds_SUCCESSFUL_GET_USER_ORGANIZATION_INFO);
				        mContext.sendBroadcast (intent);
					}
				}
				Log.i("dcdsRest", "Failed to receive user organization information.");
			}
		});
	}

	public static void getAllIncidents(long userId) {
		if(!mDataManager.isLoggedIn()){
			return;
		}
		mAuthManager.getClient().get("incidents/"  + mDataManager.getWorkspaceId() + "?accessibleByUserId=" + userId, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				mDataManager.setLastSuccesfulServerCommsTimestamp(System.currentTimeMillis());
				String content = (responseBody != null) ? new String(responseBody) : "error";
				
				IncidentMessage message = mBuilder.create().fromJson(content, IncidentMessage.class);
				mDataManager.setIncidents(message);
								
		        Intent intent = new Intent();
		        intent.setAction(Intents.dcds_SUCCESSFUL_GET_ALL_INCIDENT_INFO);
		        intent.putExtra("payload", message.toJsonString());
				mContext.sendBroadcast (intent);
		        
		        Log.e("dcdsRest", "Successfully received incident information.");
		        mDataManager.addPersonalHistory("Successfully received incident information.");
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				
		        Intent intent = new Intent();
		        intent.setAction(Intents.dcds_FAILED_GET_INCIDENTS);
		        mContext.sendBroadcast (intent);
		        
		        mDataManager.addPersonalHistory("Failed to receive incident information.");
			}
		});
	}

	public static void getCollabRooms(final long incidentId) {
		if(!mDataManager.isLoggedIn()){
			return;
		}
		 Log.e("dcdsRest", "requesting collabrooms for " + incidentId);
		Intent intent = new Intent();
	    intent.setAction(Intents.dcds_POLLING_COLLABROOMS);
	    mContext.sendBroadcast (intent);
		mAuthManager.getClient().get("collabroom/" + incidentId + "?userId=" + mDataManager.getUserId(), new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				mDataManager.setLastSuccesfulServerCommsTimestamp(System.currentTimeMillis());
				String content = (responseBody != null) ? new String(responseBody) : "error";

				 Log.e("dcdsRest", "successfully pulled rooms for " + incidentId + " with code " + statusCode);
				
				CollaborationRoomMessage message = mBuilder.create().fromJson(content, CollaborationRoomMessage.class);
				
				HashMap<String, CollabroomPayload> rooms = new HashMap<String, CollabroomPayload>();
				for(CollabroomPayload room : message.getresults()) {
					
					room.setName(room.getName().replace(mDataManager.getActiveIncidentName() + "-", ""));
					rooms.put(room.getName(), room);
				}
				
				mDataManager.setCollabRoomsForIncident(incidentId, message);

				Intent intent = new Intent();
			    intent.setAction(Intents.dcds_SUCCESSFULLY_GET_COLLABROOMS);
			    mContext.sendBroadcast (intent);
				
			}
			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				 Log.e("dcdsRest", "failed to pull collabrooms.");
				Intent intent = new Intent();
			    intent.setAction(Intents.dcds_FAILED_GET_COLLABROOMS);
			    mContext.sendBroadcast (intent);
			
			}
		});
	}
	
	public static void getSimpleReports(int offset, int limit, final long incidentId) {
		if(!mDataManager.isLoggedIn()){
			return;
		}
		if(!mFetchingSimpleReports && mParseSimpleReportsTask == null && incidentId != -1) {
			String url = "reports/"  + mDataManager.getActiveIncidentId() + "/SR?sortOrder=desc&fromDate=" + (mDataManager.getLastSimpleReportTimestamp() + 1);
						
			mFetchingSimpleReports = true;
			
			mAuthManager.getClient().get(url, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
					mDataManager.setLastSuccesfulServerCommsTimestamp(System.currentTimeMillis());
					String content = (responseBody != null) ? new String(responseBody) : "error";
					SimpleReportMessage message = mBuilder.create().fromJson(content, SimpleReportMessage.class);
					
					if(message != null) {
						ArrayList<SimpleReportPayload> srPayloads = message.getReports();
						
						if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
							mParseSimpleReportsTask = new ParseSimpleReportsTask(mContext).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, srPayloads);
						} else {
							mParseSimpleReportsTask = new ParseSimpleReportsTask(mContext).execute(srPayloads);
						}
						Log.i("dcdsRest", "Successfully received simple report information.");
					}
					mFetchingSimpleReports = false;
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
					mFetchingSimpleReports = false;
				}
			});
		}
	}
	
	public static void getFieldReports(int offset, int limit, final long incidentId) {
		if(!mDataManager.isLoggedIn()){
			return;
		}
		if(!mFetchingFieldReports && mParseFieldReportsTask == null && incidentId != -1) {
			String url = "reports/" + mDataManager.getActiveIncidentId() + "/FR?sortOrder=desc&fromDate=" + (mDataManager.getLastFieldReportTimestamp() + 1);
			
			if(incidentId != -1) {
				url += "&incidentId=" + incidentId;
			}
			
			mFetchingFieldReports = true;
			
			mAuthManager.getClient().get(url, new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
					mDataManager.setLastSuccesfulServerCommsTimestamp(System.currentTimeMillis());
					String content = (responseBody != null) ? new String(responseBody) : "error";
					
					FieldReportMessage message = mBuilder.create().fromJson(content, FieldReportMessage.class);
					
					if(message != null) {
						ArrayList<FieldReportPayload> frPayloads = message.getReports();

						if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
							mParseFieldReportsTask = new ParseFieldReportsTask(mContext).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, frPayloads);
						} else {
							mParseFieldReportsTask = new ParseFieldReportsTask(mContext).execute(frPayloads);
						}
						Log.i("dcdsRest", "Successfully received field report information.");
					}
					mFetchingFieldReports = false;
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
					mFetchingFieldReports = false;
				}
			});
		}
	}
	
	public static void getDamageReports(int offset, int limit, final long incidentId) {
		if(!mDataManager.isLoggedIn()){
			return;
		}
		if(!mFetchingDamageReports && mParseDamageReportsTask == null && incidentId != -1) {
			String url = "reports/" + mDataManager.getActiveIncidentId() + "/DMGRPT?sortOrder=desc&fromDate=" + (mDataManager.getLastDamageReportTimestamp() + 1);
			
			if(incidentId != -1) {
				url += "&incidentId=" + incidentId;
			}
			
			mFetchingDamageReports = true;
			
			mAuthManager.getClient().get(url, new AsyncHttpResponseHandler() {
				
				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
					mDataManager.setLastSuccesfulServerCommsTimestamp(System.currentTimeMillis());
					String content = (responseBody != null) ? new String(responseBody) : "error";
					
					DamageReportMessage message = mBuilder.create().fromJson(content, DamageReportMessage.class);
					
					if(message != null) {
						ArrayList<DamageReportPayload> drPayloads = message.getReports();

						if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
							mParseDamageReportsTask = new ParseDamageReportsTask(mContext).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, drPayloads);
						} else {
							mParseDamageReportsTask = new ParseDamageReportsTask(mContext).execute(drPayloads);
						}
						Log.i("dcdsRest", "Successfully received damage report information.");
					}
					mFetchingDamageReports = false;
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
					mFetchingDamageReports = false;
				}
			});
		}
	}	
	
	public static void getResourceRequests(int offset, int limit, final long incidentId) {
		if(!mDataManager.isLoggedIn()){
			return;
		}
		if(!mFetchingResourceRequests && mParseResourceRequestTask == null && incidentId != -1) {
			String url = "reports/" + mDataManager.getActiveIncidentId() + "/RESREQ?sortOrder=desc&fromDate=" + (mDataManager.getLastResourceRequestTimestamp() + 1);
			
			if(incidentId != -1) {
				url += "&incidentId=" + incidentId;
			}
			
			mFetchingResourceRequests = true;
			
			mAuthManager.getClient().get(url , new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
					mDataManager.setLastSuccesfulServerCommsTimestamp(System.currentTimeMillis());
					String content = (responseBody != null) ? new String(responseBody) : "error";
					
					ResourceRequestMessage message = mBuilder.create().fromJson(content, ResourceRequestMessage.class);
					
					if(message != null) {
						ArrayList<ResourceRequestPayload> resourceRequestPayloads = message.getReports();

						if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
							mParseResourceRequestTask = new ParseResourceRequestsTask(mContext).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, resourceRequestPayloads);
						} else {
							mParseResourceRequestTask = new ParseResourceRequestsTask(mContext).execute(resourceRequestPayloads);
						}
						Log.i("dcdsRest", "Successfully received resource request information.");
					}
					mFetchingResourceRequests = false;
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
					String content = (responseBody != null) ? new String(responseBody) : "error";

					mFetchingResourceRequests = false;
					Log.i("dcdsRest", "Failed to received resource request information for: " + content);
				}
			});
		}
	}
	
	public static void getWeatherReports(int offset, int limit, final long incidentId) {
		if(!mDataManager.isLoggedIn()){
			return;
		}
		if(!mFetchingWeatherReports && mParseWeatherReportsTask == null && incidentId != -1) {
			String url = "reports/" + mDataManager.getActiveIncidentId() + "/WR?sortOrder=desc&fromDate=" + (mDataManager.getLastWeatherReportTimestamp() + 1);
			
			if(incidentId != -1) {
				url += "&incidentId=" + incidentId;
			}
			
			mFetchingWeatherReports = true;
			
			mAuthManager.getClient().get(url , new AsyncHttpResponseHandler() {
				
				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
					mDataManager.setLastSuccesfulServerCommsTimestamp(System.currentTimeMillis());
					String content = (responseBody != null) ? new String(responseBody) : "error";
					WeatherReportMessage message = mBuilder.create().fromJson(content, WeatherReportMessage.class);
					
					if(message != null) {
						ArrayList<WeatherReportPayload> wrPayloads = message.getReports();

						if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
							mParseWeatherReportsTask = new ParseWeatherReportsTask(mContext).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, wrPayloads);
						} else {
							mParseWeatherReportsTask = new ParseWeatherReportsTask(mContext).execute(wrPayloads);
						}
						Log.i("dcdsRest", "Successfully received weather report information.");
					}
					mFetchingWeatherReports = false;
				}
				
				@Override
				public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
					String content = (responseBody != null) ? new String(responseBody) : "error";
					mFetchingWeatherReports = false;
					Log.i("dcdsRest", "Failed to received weather report information for: " + content);
				}
			});
		}
	}

	public static void getUxoReports(int offset, int limit, final long incidentId) {
		if(!mDataManager.isLoggedIn()){
			return;
		}
		if(!mFetchingUxoReports && mParseUxoReportsTask == null && incidentId != -1) {
			String url = "reports/" + mDataManager.getActiveIncidentId() + "/UXO?sortOrder=desc&fromDate=" + (mDataManager.getLastUxoReportTimestamp() + 1);
						
			if(incidentId != -1) {
				url += "&incidentId=" + incidentId;
			}
			
			mFetchingUxoReports = true;
			
			mAuthManager.getClient().get(url, new AsyncHttpResponseHandler() {
				
				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
					mDataManager.setLastSuccesfulServerCommsTimestamp(System.currentTimeMillis());
					String content = (responseBody != null) ? new String(responseBody) : "error";
					
					UxoReportMessage message = mBuilder.create().fromJson(content, UxoReportMessage.class);
					
					if(message != null) {
						ArrayList<UxoReportPayload> uxoPayloads = message.getReports();

						if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
							mParseUxoReportsTask = new ParseUxoReportsTask(mContext).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uxoPayloads);
						} else {
							mParseUxoReportsTask = new ParseUxoReportsTask(mContext).execute(uxoPayloads);
						}
						Log.i("dcdsRest", "Successfully received uxo report information.");
					}
					mFetchingUxoReports = false;
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
					mFetchingUxoReports = false;
				}
			});
		}
	}	
	
	public static void getChatHistory(final long incidentId, final long collabRoomId) {
		if(!mDataManager.isLoggedIn()){
			return;
		}
		if(!mFetchingChatMessages && mParseChatMessagesTask == null && incidentId != -1 && collabRoomId != -1) {

				mFetchingChatMessages = true;
				mAuthManager.getClient().get("chatmsgs/" + collabRoomId + "?sortOrder=desc&fromDate=" + (mDataManager.getLastChatTimestamp(incidentId, collabRoomId)+1) + "&dateColumn=created" , new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
						mDataManager.setLastSuccesfulServerCommsTimestamp(System.currentTimeMillis());
						String content = (responseBody != null) ? new String(responseBody) : "error";
						ChatMessage message = mBuilder.create().fromJson(content, ChatMessage.class);
								
						try {
							JSONObject jObject = new JSONObject(content);
							ArrayList<ChatPayload> chatMessages = message.getChatMsgs();
							
							for(int i = 0; i < message.getCount(); i++){
								chatMessages.get(i).setIncidentId(incidentId);								
								chatMessages.get(i).setuserId(jObject.getJSONArray("chats").getJSONObject(i).getJSONObject("userorg").getJSONObject("user").getLong("userId"));
								chatMessages.get(i).setUserOrgName(jObject.getJSONArray("chats").getJSONObject(i).getJSONObject("userorg").getJSONObject("org").getString("name"));
								chatMessages.get(i).setNickname(jObject.getJSONArray("chats").getJSONObject(i).getJSONObject("userorg").getJSONObject("user").getString("username"));
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

						if(message != null) {
							if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
								mParseChatMessagesTask = new ParseChatMessagesTask(mContext).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,  message.getChatMsgs());
							} else {
								mParseChatMessagesTask = new ParseChatMessagesTask(mContext).execute(message.getChatMsgs());
							}
							Log.i("dcdsRest", "Successfully received chat information for: " + incidentId + "-" + collabRoomId);
						}
						mFetchingChatMessages = false;
					}

					@Override
					public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
						String content = (responseBody != null) ? new String(responseBody) : "error";
						
						Log.e("dcdsRest", "Failed to receive chat information for: " + incidentId + "-" + collabRoomId);
						Log.e("dcdsRest", content + " " + error.getLocalizedMessage());
						mFetchingChatMessages = false;
					}
				});
			}
		}
	
	public static void getMarkupHistory(final long collabRoomId) {
		if(!mDataManager.isLoggedIn()){
			return;
		}
		if(collabRoomId != -1){
			if(!mFetchingMarkupFeatures && mParseMarkupFeaturesTask == null && collabRoomId != -1) {
				mFetchingMarkupFeatures = true;
				mAuthManager.getClient().get("features/collabroom/" + collabRoomId + "?geoType=4326&userId=" + mDataManager.getUserId() + "&fromDate=" + (mDataManager.getLastMarkupTimestamp(collabRoomId) + 1)+"&dateColumn=seqtime", new AsyncHttpResponseHandler() {
					
					@Override
					public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
						mDataManager.setLastSuccesfulServerCommsTimestamp(System.currentTimeMillis());
						String content = (responseBody != null) ? new String(responseBody) : "error";
						MarkupMessage message = mBuilder.create().fromJson(content, MarkupMessage.class);
	
						for(int i = 0; i < message.getFeatures().size();i++){
							message.getFeatures().get(i).buildVector2Point(true);
						}
						
						MarkupPayload payload = new MarkupPayload();
						payload.setFeatures(message.getFeatures());
						payload.setDeletedFeatures(message.getDeletedFeature());
						payload.setCollabRoomId(collabRoomId);
						
						if(message != null) {
							if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
								mParseMarkupFeaturesTask = new ParseMarkupFeaturesTask(mContext).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, payload);
							} else {
								mParseMarkupFeaturesTask = new ParseMarkupFeaturesTask(mContext).execute(payload);
							}
							Log.i("dcdsRest", "Successfully received markup information.");
						}
						mFetchingMarkupFeatures = false;
					}
					
					@Override
					public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
						String content = (responseBody != null) ? new String(responseBody) : "error";
						
						if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
							mParseMarkupFeaturesTask = new ParseMarkupFeaturesTask(mContext).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (MarkupPayload[])null);
						} else {
							mParseMarkupFeaturesTask = new ParseMarkupFeaturesTask(mContext).execute((MarkupPayload[])null);
						}
						Log.i("dcdsRest", "Failed to receive markup history: " + content);
						mFetchingMarkupFeatures = false;
				}
			});
					
			}
	 	}
	}

	public static void getWeatherUpdate(final double latitude, final double longitude) {
		if(!mDataManager.isLoggedIn()){
			return;
		}
		if(!Double.isNaN(latitude) && !Double.isNaN(longitude) ) {
			mAuthManager.getClient().get("http://forecast.weather.gov/MapClick.php" + "?lat=" + latitude + "&lon=" + longitude + "&FcstType=json&lg=english", new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
					mDataManager.setLastSuccesfulServerCommsTimestamp(System.currentTimeMillis());
					String content = (responseBody != null) ? new String(responseBody) : "error";
					
					try {
						WeatherPayload payload = mBuilder.create().fromJson(content, WeatherPayload.class);
	
						if(payload != null) {
							mDataManager.setWeather(payload);
							
							Log.i("dcdsRest", "Successfully received weather information for: " + latitude + "," + longitude);
					        Intent intent = new Intent();
					        intent.setAction(Intents.dcds_NEW_WEATHER_REPORT_RECEIVED);
					        intent.putExtra("payload", payload.toJsonString());
					        mContext.sendBroadcast (intent);
						}
					} catch(Exception e) {
						Log.e("dcdsRest", "Failed to load weather information for: " + latitude + "/" + longitude);
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
					Log.e("dcdsRest", "Failed to load weather information for: " + latitude + "/" + longitude);
					
				}
			});
		}
	}
	
	public static void getWFSLayers(){
		if(!mDataManager.isLoggedIn()){
			return;
		}
			mAuthManager.getClient().get("datalayer/"+ mDataManager.getWorkspaceId() +"/tracking", new AsyncHttpResponseHandler() {
				
				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
					mDataManager.setLastSuccesfulServerCommsTimestamp(System.currentTimeMillis());
					String content = (responseBody != null) ? new String(responseBody) : "error";
					TrackingLayerMessage message = mBuilder.create().fromJson(content, TrackingLayerMessage.class);
					Log.i("dcdsRest", "Succesfully received Tracking Layers: " + message.getCount());

					mDataManager.setTrackingLayers(message.getLayers());
				}
				
				@Override
				public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
					String content = (responseBody != null) ? new String(responseBody) : "error";
					Log.i("dcdsRest", "Failed to receive Tracking Layers: " + content);
					
			}
		});	
	}
	
	public static void getWFSData(final TrackingLayerPayload layer, int numResults, String mLastFeatureTimestamp, AsyncHttpResponseHandler responseHandler) {
		
		if(layer.getDatasourceid() == null ){	// no token needed, pull layer
			
			if(layer.shouldExpectJson())
			{
				Log.d("dcds REST", layer.getInternalurl()+"?service=WFS&outputFormat=json&version=1.1.0&request=GetFeature&srsName=EPSG:4326&typeName=" + layer.getLayername() + "&maxFeatures=" + numResults);
				mAuthManager.getClient().get(layer.getInternalurl()+"?service=WFS&outputFormat=json&version=1.1.0&request=GetFeature&srsName=EPSG:4326&typeName=" + layer.getLayername() + "&maxFeatures=" + numResults, responseHandler);
				return;
			}else{
				Log.d("dcds REST", layer.getInternalurl()+"?service=WFS&version=1.1.0&request=GetFeature&srsName=EPSG:4326&typeName=" + layer.getLayername() + "&maxFeatures=" + numResults);
				mAuthManager.getClient().get(layer.getInternalurl()+"?service=WFS&version=1.1.0&request=GetFeature&srsName=EPSG:4326&typeName=" + layer.getLayername() + "&maxFeatures=" + numResults, responseHandler);
				return;
			}
		}else{
			if(layer.getAuthtoken() == null){	//get token for layer
				RestClient.getWFSDataToken(layer, numResults, mLastFeatureTimestamp, responseHandler);
			}else{	//already have token so pull layer
				if(layer.getAuthtoken().getExpires() <= System.currentTimeMillis()){	//token is expired so pull a new one
					RestClient.getWFSDataToken(layer, numResults, mLastFeatureTimestamp, responseHandler);
				}else{
					if(layer.getAuthtoken().getToken() != null){
						
						if(layer.shouldExpectJson())
						{
							Log.d("dcds REST", layer.getInternalurl()+"?service=WFS&outputFormat=json&version=1.1.0&request=GetFeature&srsName=EPSG:4326&typeName=" + layer.getLayername() + "&maxFeatures=" + numResults + "&token=" + layer.getAuthtoken().getToken());
							mAuthManager.getClient().get(layer.getInternalurl()+"?service=WFS&outputFormat=json&version=1.1.0&request=GetFeature&srsName=EPSG:4326&typeName=" + layer.getLayername() + "&maxFeatures=" + numResults + "&token=" + layer.getAuthtoken().getToken(), responseHandler);
						}else{
							Log.d("dcds REST", layer.getInternalurl()+"?service=WFS&version=1.1.0&request=GetFeature&srsName=EPSG:4326&typeName=" + layer.getLayername() + "&maxFeatures=" + numResults + "&token=" + layer.getAuthtoken().getToken());
							mAuthManager.getClient().get(layer.getInternalurl()+"?service=WFS&version=1.1.0&request=GetFeature&srsName=EPSG:4326&typeName=" + layer.getLayername() + "&maxFeatures=" + numResults + "&token=" + layer.getAuthtoken().getToken(), responseHandler);
						}
					}	
				}
			}
		}
	}
	
	public static void getWFSDataToken(final TrackingLayerPayload layer,final int numResults,final String mLastFeatureTimestamp,final AsyncHttpResponseHandler responseHandler) {
		if(!mDataManager.isLoggedIn()){
			return;
		}
		
		Log.d("dcds REST","WFS Data Token: " + mDataManager.getServer() + "datalayer/1/token/" + layer.getDatasourceid());
		mAuthManager.getClient().get(mDataManager.getServer() + "datalayer/" + mDataManager.getWorkspaceId() + "/token/" + layer.getDatasourceid(),  new AsyncHttpResponseHandler() {
			@SuppressWarnings("unchecked")

			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				mDataManager.setLastSuccesfulServerCommsTimestamp(System.currentTimeMillis());
				String content = (responseBody != null) ? new String(responseBody) : "error";
				try {
					Log.d("dcds REST","Successfully received WFS Data Token: " + content);
					TrackingTokenPayload token = mBuilder.create().fromJson(content, TrackingTokenPayload.class);
					
					if(token.getToken() == null){
						token.setExpires(System.currentTimeMillis() + 120000);	//set not authorized token to expire in 2 minutes so rest client tries to pull it again later
					}else{
						RestClient.getWFSData(layer, numResults, mLastFeatureTimestamp, responseHandler);
					}
					
					layer.setAuthtoken(token);
					mDataManager.UpdateTrackingLayerData(layer);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				String content = (responseBody != null) ? new String(responseBody) : "error";
				Log.e("dcdsRest", "Failed to authenticate WFS Layer: " + content);
			}
		});	
	}
	
	public static Header[] getAuthData() {
		return mAuthHeader;
	}

	public static void clearParseSimpleReportTask() {
		if(mParseSimpleReportsTask != null) {
			mParseSimpleReportsTask.cancel(true);
			mParseSimpleReportsTask = null;
		}
	}
	
	public static void clearParseFieldReportTask() {
		if(mParseFieldReportsTask != null) {
			mParseFieldReportsTask.cancel(true);
			mParseFieldReportsTask = null;
		}
	}
	
	public static void clearParseDamageReportTask() {
		if(mParseDamageReportsTask != null) {
			mParseDamageReportsTask.cancel(true);
			mParseDamageReportsTask = null;
		}
	}
	
	public static void clearParseResourceRequestTask() {
		if(mParseResourceRequestTask != null) {
			mParseResourceRequestTask.cancel(true);
			mParseResourceRequestTask = null;
		}
	}
	
	public static void clearParseWeatherReportTask() {
		if(mParseWeatherReportsTask != null) {
			mParseWeatherReportsTask.cancel(true);
			mParseWeatherReportsTask = null;
		}
	}
	
	public static void clearParseUxoReportTask() {
		if(mParseUxoReportsTask != null) {
			mParseUxoReportsTask.cancel(true);
			mParseUxoReportsTask = null;
		}
	}
		
	public static void clearParseMarkupFeaturesTask() {
		if(mParseMarkupFeaturesTask != null) {
			mParseMarkupFeaturesTask.cancel(true);
			mParseMarkupFeaturesTask = null;
		}
	}
	
	public static boolean isParsingMarkup() {
		if(mParseMarkupFeaturesTask != null) {
			return true;
		}
		return false;
	}
	
	public static boolean isFetchingMarkup() {
		return mFetchingMarkupFeatures;
	}
	
	public static void clearParseChatMessagesTask() {
		if(mParseChatMessagesTask != null) {
			mParseChatMessagesTask.cancel(true);
			mParseChatMessagesTask = null;
		}
	}
	
	public static void postSimpleReports() {
		if(!mDataManager.isLoggedIn()){
			return;
		}
		ArrayList<SimpleReportPayload> simpleReports = mDataManager.getAllSimpleReportStoreAndForwardReadyToSendForUser(mDataManager.getUsername());
		
		if(mSimpleReportResponseHandlers == null) {
			mSimpleReportResponseHandlers = new SparseArray<SimpleReportResponseHandler>();
		}
		
		for (SimpleReportPayload report : simpleReports) {
        	if(!report.isDraft() && mSimpleReportResponseHandlers != null && mSimpleReportResponseHandlers.indexOfKey((int)report.getId()) < 0 && !mSendingSimpleReports) {
        		Log.w("dcds_POST", "Adding simple report " + report.getId() + " to send queue.");
        		SimpleReportData data = report.getMessageData();
        		
        		try {
        			if(data.getFullpath() != null && data.getFullpath() != ""){
        				
    					SimpleReportResponseHandler handler =  new SimpleReportResponseHandler(mContext, mDataManager, report.getId());
                		mSimpleReportResponseHandlers.put((int)report.getId(), handler);

		        		RequestParams params = new RequestParams();
		        		params.put("deviceId", mDeviceId);
		        		params.put("incidentId", String.valueOf(report.getIncidentId()));
		        		params.put("userId", String.valueOf(mDataManager.getUserId()));
		        		params.put("usersessionid", String.valueOf(mDataManager.getUserSessionId()));
		        		params.put("latitude", String.valueOf(data.getLatitude()));
		        		params.put("longitude", String.valueOf(data.getLongitude()));
		        		params.put("altitude", "0.0");
		        		params.put("track", "0.0");
		        		params.put("speed","0.0");
		        		params.put("accuracy", "0.0");
		        		params.put("description", data.getDescription());
		        		params.put("category", data.getCategory());
		        		params.put("seqtime", String.valueOf(report.getSeqTime()));
		        		params.put("image", new File(data.getFullpath()));     		
        		
                		if(mAuthManager != null && mAuthManager.getClient() != null) {                			
    	            		mAuthManager.getClient().post("reports/"  + mDataManager.getActiveIncidentId() + "/SR", params, handler);
    	        			mSendingSimpleReports = true;
                		}
		        		
        			}else{	//no image
        				report.setUserSessionId(mDataManager.getUserSessionId());
		        		StringEntity entity = new StringEntity(report.toJsonString());
		    			mAuthManager.getClient().post("reports/"  + mDataManager.getActiveIncidentId()  + "/SR", entity, new SimpleReportNoImageResponseHandler(mContext, mDataManager, report.getId()));
		    			mSendingSimpleReports = true;
        			}
        		} catch(FileNotFoundException e) {
        			Log.e("dcdsRest", "Deleting: " + report.getId() + " success: " + mDataManager.deleteSimpleReportStoreAndForward(report.getId()) + " due to invalid file.");
        			mDataManager.addPersonalHistory("Deleting simple report: " + report.getId() + " success: " + mDataManager.deleteSimpleReportStoreAndForward(report.getId()) + " due to invalid/missing image file.");
        			mSendingSimpleReports = false;
        		} catch (IOException e) {
					e.printStackTrace();
				}
        	}
		}
	}
		
	public static void removeSimpleReportHandler(long reportId) {
		Log.w("dcds_POST", "Removing simple report " + reportId + " from send queue.");
		mSimpleReportResponseHandlers.remove((int)reportId);
	}
	
	public static void removeDamageReportHandler(long reportId) {
		Log.w("dcds_POST", "Removing damage report " + reportId + " from send queue.");
		mDamageReportResponseHandlers.remove((int)reportId);
	}
	
	public static void removeUxoReportHandler(long reportId) {
		Log.w("dcds_POST", "Removing uxo report " + reportId + " from send queue.");
		mUxoReportResponseHandlers.remove((int)reportId);
	}
	
	public static void postFieldReports() {
		if(!mDataManager.isLoggedIn()){
			return;
		}
		if(!mSendingFieldReports) {
			ArrayList<FieldReportPayload> fieldReports = mDataManager.getAllFieldReportStoreAndForwardReadyToSendForUser(mDataManager.getUsername());
			
			for (FieldReportPayload report : fieldReports) {
				report.setUserSessionId(mDataManager.getUserSessionId());
				try {
		        	if(!report.isDraft()) {
		        		report.setUserSessionId(mDataManager.getUserSessionId());
		    			StringEntity entity = new StringEntity(report.toJsonString());
		    			
		    			mAuthManager.getClient().post("reports/"  + mDataManager.getActiveIncidentId() + "/FR", entity, new FieldReportResponseHandler(mContext, mDataManager, report.getId()));
		    			mSendingFieldReports = true;
		        	}
				} catch(UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void postDamageReports() {
		if(!mDataManager.isLoggedIn()){
			return;
		}
		ArrayList<DamageReportPayload> damageReports = mDataManager.getAllDamageReportStoreAndForwardReadyToSendForUser(mDataManager.getUsername());
		
		if(mDamageReportResponseHandlers == null) {
			mDamageReportResponseHandlers = new SparseArray<DamageReportResponseHandler>();
		}
		
		for (DamageReportPayload report : damageReports) {
        	if(!report.isDraft() && mDamageReportResponseHandlers != null && mDamageReportResponseHandlers.indexOfKey((int)report.getId()) < 0 && !mSendingDamageReports) {
        		Log.w("dcds_POST", "Adding damage report " + report.getId() + " to send queue.");
        		DamageReportData data = report.getMessageData();
        		
        		try {
        		
	        		if(data.getFullpath() != null && data.getFullpath() != ""){
		        		
		        		RequestParams params = new RequestParams();
		        		params.put("msg", data.toJsonString());
		        		params.put("deviceId", mDeviceId);
		        		params.put("incidentId", String.valueOf(report.getIncidentId()));
		        		params.put("usersessionid", String.valueOf(mDataManager.getUserSessionId()));
		        		params.put("seqtime", String.valueOf(report.getSeqTime()));
	        			params.put("image", new File(data.getFullpath()));
	        			
	        			DamageReportResponseHandler handler =  new DamageReportResponseHandler(mContext, mDataManager, report.getId());
	            		mDamageReportResponseHandlers.put((int)report.getId(), handler);
	            		
	            		mAuthManager.getClient().post("reports/"  + mDataManager.getActiveIncidentId() + "/DMGRPT", params, handler);
	        		}else{	//no image
	        			report.setUserSessionId(mDataManager.getUserSessionId());
		        		StringEntity entity = new StringEntity(report.toJsonString());
		    			mAuthManager.getClient().post("reports/"  + mDataManager.getActiveIncidentId()  + "/DMGRPT", entity, new DamageReportNoImageResponseHandler(mContext, mDataManager, report.getId()));
		    			mSendingDamageReports = true;
	        		}
            		
            		
        		} catch(FileNotFoundException e) {
        			Log.e("dcdsRest", "Deleting: " + report.getId() + " success: " + mDataManager.deleteDamageReportStoreAndForward(report.getId()) + " due to invalid file.");
        			mSendingDamageReports = false;
        		} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					mSendingDamageReports = false;
        		}
        		
    			mSendingDamageReports = true;
        	}
		}
	}
	
	public static void postResourceRequests() {
		if(!mDataManager.isLoggedIn()){
			return;
		}
		if(!mSendingResourceRequests) {
			ArrayList<ResourceRequestPayload> resourceRequests = mDataManager.getAllResourceRequestStoreAndForwardReadyToSendForUser(mDataManager.getUsername());
			
			for (ResourceRequestPayload request : resourceRequests) {
				request.setUserSessionId(mDataManager.getUserSessionId());
				try {
		        	if(!request.isDraft()) {
		        		request.setUserSessionId(mDataManager.getUserSessionId());
		    			StringEntity entity = new StringEntity(request.toJsonString());
		    			
		    			mAuthManager.getClient().post("reports/"  + mDataManager.getActiveIncidentId() + "/RESREQ", entity, new ResourceRequestResponseHandler(mContext, mDataManager, request.getId()));
		    			mSendingResourceRequests = true;
		        	}
				} catch(UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void postWeatherReports() {
		if(!mDataManager.isLoggedIn()){
			return;
		}
		if(!mSendingWeatherReports) {
			ArrayList<WeatherReportPayload> weatherReports = mDataManager.getAllWeatherReportStoreAndForwardReadyToSendForUser(mDataManager.getUsername());
			
			for (WeatherReportPayload report : weatherReports) {
				report.setUserSessionId(mDataManager.getUserSessionId());
				try {
		        	if(!report.isDraft()) {
		        		report.setUserSessionId(mDataManager.getUserSessionId());
		        		StringEntity entity = new StringEntity(report.toJsonString());
		    			mAuthManager.getClient().post("reports/"  + mDataManager.getActiveIncidentId()  + "/WR", entity, new WeatherReportResponseHandler(mContext, mDataManager, report.getId()));
		    			mSendingWeatherReports = true;
		        	}
				} catch(UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void postUxoReports() {
		if(!mDataManager.isLoggedIn()){
			return;
		}
		ArrayList<UxoReportPayload> uxoReports = mDataManager.getAllUxoReportStoreAndForwardReadyToSendForUser(mDataManager.getUsername());
		
		if(mUxoReportResponseHandlers == null) {
			mUxoReportResponseHandlers = new SparseArray<UxoReportResponseHandler>();
		}
		
		for (UxoReportPayload report : uxoReports) {
        	if(!report.isDraft() && report.getSendStatus() != ReportSendStatus.SENT && mUxoReportResponseHandlers != null && mUxoReportResponseHandlers.indexOfKey((int)report.getId()) < 0 && !mSendingUxoReports) {
        		Log.w("dcds_POST", "Adding uxo report " + report.getId() + " to send queue.");
        		UxoReportData data = report.getMessageData();
        		
        		try {
        		
	        		if(data.getFullPath() != null && data.getFullPath() != ""){
	        		
		        		RequestParams params = new RequestParams();
		        		params.put("msg", data.toJsonString());
		        		params.put("deviceId", mDeviceId);
		        		params.put("incidentId", String.valueOf(report.getIncidentId()));
		//        		params.put("userId", String.valueOf(report.getSenderUserId()));
		        		params.put("usersessionid", String.valueOf(mDataManager.getUserSessionId()));
		        		params.put("seqtime", String.valueOf(report.getSeqTime()));
	        		
	        			params.put("ur-image", new File(data.getFullPath()));
	        			UxoReportResponseHandler handler =  new UxoReportResponseHandler(mContext, mDataManager, report.getId());
	            		mUxoReportResponseHandlers.put((int)report.getId(), handler);
	            		
	            		mAuthManager.getClient().post("reports/"  + mDataManager.getActiveIncidentId() + "/UXO", params, handler);
	        		}else{	//no image
	        			report.setUserSessionId(mDataManager.getUserSessionId());
		        		StringEntity entity = new StringEntity(report.toJsonString());
		    			mAuthManager.getClient().post("reports/"  + mDataManager.getActiveIncidentId()  + "/UXO", entity, new UxoReportNoImageResponseHandler(mContext, mDataManager, report.getId()));
		    			mSendingUxoReports = true;
	    			}
            		
        		} catch(FileNotFoundException e) {
//        			mAuthManager.getClient().post("reports/"  + mDataManager.getActiveIncidentId()  + "/UXO", entity, new UxoReportResponseHandler(mContext, mDataManager, report.getId()));
        			Log.e("dcdsRest", "Deleting: " + report.getId() + " success: " + mDataManager.deleteUxoReportStoreAndForward(report.getId()) + " due to invalid file.");
        			mSendingUxoReports = false;
        		} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					mSendingUxoReports = false;
				}
        		
    			mSendingUxoReports = true;
        	}
		}
	}
		
	public static void postChatMessages() {
		if(!mDataManager.isLoggedIn()){
			return;
		}
		if(!mSendingChatMessages) {
			ArrayList<ChatPayload> chatMessages = mDataManager.getAllChatStoreAndForward();
			
			for (ChatPayload payload : chatMessages) {
				
				try {
					payload.setchatid(-1);
					StringEntity entity = new StringEntity(payload.toJsonString());
	    			mAuthManager.getClient().post("chatmsgs/" + payload.getcollabroomid(), entity, new ChatResponseHandler(mDataManager, chatMessages));
	    			
	    			mSendingChatMessages = true;
	        	} catch(UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			if(chatMessages.size()>0){
				mDataManager.requestChatHistory(mDataManager.getActiveIncidentId(), mDataManager.getActiveCollabroomId());
				mSendingChatMessages = true;
			}
		}
	}
	
	public static void postMDTs() {
		if(!mDataManager.isLoggedIn()){
			return;
		}
		
		ArrayList<MobileDeviceTrackingPayload> mdtMessages = mDataManager.getAllMDTStoreAndForward();
		MobileDeviceTrackingPayload message = mdtMessages.get(mdtMessages.size()-1);
		
		try {
			if(message.getDeviceId() != null && !message.getDeviceId().isEmpty()) {
				StringEntity entity = new StringEntity(message.toJsonString());
				
				mAuthManager.getClient().post("mdtracks", entity, new MDTResponseHandler(mDataManager, mdtMessages));
			} else {
				// invalid mdt due to lack of device id, so delete it
				mDataManager.deleteMDTStoreAndForward(message.getId());
			}
    	} catch(UnsupportedEncodingException e) {
    		e.printStackTrace();
		}
	}
	
	public static void postMarkupFeatures() {
		if(!mDataManager.isLoggedIn()){
			return;
		}
		
		if(!mSendingMarkupFeatures) {
			ArrayList<MarkupFeature> features = mDataManager.getAllMarkupFeaturesStoreAndForwardReadyToSend();
			
			if(features.size() > 0) {					
		    	try {
		    		features.get(0).setUsersessionId(mDataManager.getUserSessionId());
					StringEntity entity = new StringEntity(features.get(0).toJsonStringWithWebLonLat());
					
					mAuthManager.getClient().post("features/collabroom/" + features.get(0).getCollabRoomId() + "?geoType=4326", entity, new MarkupResponseHandler(mContext, mDataManager, features.get(0)));
					mSendingMarkupFeatures = true;
				} catch(UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void getGoogleMapsLegalInfo(AsyncHttpResponseHandler responseHandler){
		AsyncHttpClient mClient = new AsyncHttpClient();
		mClient.setTimeout(60 * 1000);
		mClient.setURLEncodingEnabled(false);
		mClient.setMaxRetriesAndTimeout(2, 1000);
		mClient.get("http://www.google.com/mobile/legalnotices/", responseHandler);
	}
	
	public static String getDeviceId() {
		return mDeviceId;
	}

	public static void setSendingSimpleReports(boolean isSending) {
		mSendingSimpleReports = isSending;
		if(mDataManager.isLoggedIn()) {
			postSimpleReports();
		}
	}
	
	public static void setSendingFieldReports(boolean isSending) {
		mSendingFieldReports = isSending;
	}

	public static void setSendingDamageReports(boolean isSending) {
		mSendingDamageReports = isSending;
		if(mDataManager.isLoggedIn()) {
			postDamageReports();
		}
	}
	
	public static void setSendingResourceRequests(boolean isSending) {
		mSendingResourceRequests = isSending;
	}
	
	public static void setSendingWeatherReports(boolean isSending) {
		mSendingWeatherReports = isSending;
	}
	
	public static void setSendingUxoReports(boolean isSending) {
		mSendingUxoReports = isSending;
		if(mDataManager.isLoggedIn()) {
			postUxoReports();
		}
	}
	
	public static void setSendingChatMessages(boolean isSending) {
		mSendingChatMessages = isSending;
	}
	
	public static void setSendingMarkupFeatures(boolean isSending) {
		mSendingMarkupFeatures = isSending;
	}

	public static void setDataManager(DataManager mInstance) {
		mDataManager = mInstance;
	}
}
