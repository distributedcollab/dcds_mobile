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
package edu.mit.ll.dcds.android.utils;

public class Intents {

	public static final String dcds_SUCCESSFUL_LOGIN = "dcds_SUCCESSFUL_LOGIN";
	public static final String dcds_FAILED_LOGIN = "dcds_FAILED_LOGIN";
	
	public static final String dcds_SUCCESSFUL_GET_INCIDENT_INFO = "dcds_SUCCESSFUL_GET_INCIDENT_INFO";
	public static final String dcds_SUCCESSFUL_GET_ALL_INCIDENT_INFO = "dcds_SUCCESSFUL_GET_ALL_INCIDENT_INFO";

	public static final String dcds_COLLABROOM_SWITCHED = "dcds_COLLABROOM_SWITCHED";
	public static final String dcds_INCIDENT_SWITCHED = "dcds_INCIDENT_SWITCHED";
	
	public static final String dcds_BT_CONNECT = "dcds_BT_CONNECT";
	public static final String dcds_BT_DISCONNECT = "dcds_BT_DISCONNECT";

	public static final String dcds_NEW_CHAT_RECEIVED = "dcds_NEW_CHAT_RECEIVED";
	public static final String dcds_LAST_CHAT_RECEIVED = "dcds_LAST_CHAT_RECEIVED";
	public static final String dcds_NEW_PERSONAL_HISTORY_RECEIVED = "dcds_NEW_PERSONAL_HISTORY_RECEIVED";
	public static final String dcds_NEW_FIELD_REPORT_RECEIVED = "dcds_NEW_FIELD_REPORT_RECEIVED";
	public static final String dcds_NEW_DAMAGE_REPORT_RECEIVED = "dcds_NEW_DAMAGE_REPORT_RECEIVED";
	public static final String dcds_NEW_MARKUP_RECEIVED = "dcds_NEW_MARKUP_RECEIVED";
	public static final String dcds_NEW_RESOURCE_REQUEST_RECEIVED = "dcds_NEW_RESOURCE_REQUEST_RECEIVED";
	public static final String dcds_NEW_SIMPLE_REPORT_RECEIVED = "dcds_NEW_SIMPLE_REPORT_RECEIVED";
	public static final String dcds_NEW_WEATHER_REPORT_RECEIVED = "dcds_NEW_WEATHER_REPORT_RECEIVED";
	public static final String dcds_NEW_UXO_REPORT_RECEIVED = "dcds_NEW_UXO_REPORT_RECEIVED";
		
	public static final String dcds_SENT_SIMPLE_REPORTS_CLEARED = "dcds_SENT_SIMPLE_REPORTS_CLEARED";
	public static final String dcds_SENT_DAMAGE_REPORTS_CLEARED = "dcds_SENT_DAMAGE_REPORTS_CLEARED";
	public static final String dcds_SENT_FIELD_REPORTS_CLEARED = "dcds_SENT_FIELD_REPORTS_CLEARED";
	public static final String dcds_SENT_RESOURCE_REQUESTS_CLEARED = "dcds_SENT_RESOURCE_REQUESTS_CLEARED";
	public static final String dcds_SENT_WEATHER_REPORTS_CLEARED = "dcds_SENT_WEATHER_REPORTS_CLEARED";
	public static final String dcds_SENT_UXO_REPORTS_CLEARED = "dcds_SENT_UXO_REPORTS_CLEARED";
	
	public static final String dcds_VIEW_OVERVIEW = "dcds_VIEW_OVERVIEW";
	public static final String dcds_VIEW_SIMPLE_REPORTS_LIST = "dcds_VIEW_SIMPLE_REPORTS_LIST";
	public static final String dcds_VIEW_FIELD_REPORTS_LIST = "dcds_VIEW_FIELD_REPORTS_LIST";
	public static final String dcds_VIEW_DAMAGE_REPORTS_LIST = "dcds_VIEW_DAMAGE_REPORTS_LIST";
	public static final String dcds_VIEW_RESOURCE_REQUESTS_LIST = "dcds_VIEW_RESOURCE_REQUESTS_LIST";
	public static final String dcds_VIEW_WEATHER_REPORTS_LIST = "dcds_VIEW_WEATHER_REPORTS_LIST";
	public static final String dcds_VIEW_UXO_REPORTS_LIST = "dcds_VIEW_UXO_REPORTS_LIST";
	
	public static final String dcds_MARKING_ALL_REPORTS_READ_FINISHED = "dcds_MARKING_ALL_REPORTS_READ_FINISHED";
	
	public static final String dcds_POLLING_TASK = "dcds_POLLING_TASK";
	public static final String dcds_POLLING_TASK_DAMAGE_REPORT = "dcds_POLLING_TASK_DAMAGE_REPORT";
	public static final String dcds_POLLING_TASK_FIELD_REPORT = "dcds_POLLING_TASK_FIELD_REPORT";
	public static final String dcds_POLLING_TASK_SIMPLE_REPORT = "dcds_POLLING_TASK_SIMPLE_REPORT";
	public static final String dcds_POLLING_TASK_RESOURCE_REQUEST = "dcds_POLLING_TASK_RESOURCE_REQUEST";
	public static final String dcds_POLLING_TASK_WEATHER_REPORT = "dcds_POLLING_TASK_WEATHER_REPORT";
	public static final String dcds_POLLING_TASK_UXO_REPORT = "dcds_POLLING_TASK_UXO_REPORT";
	public static final String dcds_POLLING_TASK_CHAT_MESSAGES = "dcds_POLLING_TASK_CHAT_MESSAGES";
	public static final String dcds_POLLING_WEATHER = "dcds_POLLING_WEATHER";
	public static final String dcds_POLLING_MARKUP_REQUEST = "dcds_POLLING_MARKUP_REQUEST";
	public static final String dcds_POLLING_WFS_LAYER = "dcds_POLLING_WFS_LAYER_";
	public static final String dcds_POLLING_COLLABROOMS = "dcds_POLLING_COLLABROOMS";
	public static final String dcds_POLLING_INCIDENTS = "dcds_POLLING_INCIDENTS";
	
	public static final String dcds_SUCCESSFULLY_GET_COLLABROOMS = "dcds_SUCCESSFULLY_GET_COLLABROOMS";
	public static final String dcds_SUCCESSFULLY_GET_INCIDENTS = "dcds_SUCCESSFULLY_GET_INCIDENTS";
	public static final String dcds_FAILED_GET_COLLABROOMS = "dcds_FAILED_GET_COLLABROOMS";
	public static final String dcds_FAILED_GET_INCIDENTS = "dcds_FAILED_GET_INCIDENTS";
	public static final String dcds_FAILED_TO_POST_MARKUP = "dcds_FAILED_TO_POST_MARKUP";
	
	public static final String dcds_SHOW_INCIDENT_SELECT = "dcds_SHOW_INCIDENT_SELECT";
	public static final String dcds_SIMPLE_REPORT_PROGRESS = "dcds_SIMPLE_REPORT_PROGRESS";
	public static final String dcds_DAMAGE_REPORT_PROGRESS = "dcds_DAMAGE_REPORT_PROGRESS";
	public static final String dcds_UXO_REPORT_PROGRESS = "dcds_UXO_REPORT_PROGRESS";
	public static final String dcds_WEATHER_REPORT_PROGRESS = "dcds_WEATHER_REPORT_PROGRESS";
	public static final String dcds_FIELD_REPORT_PROGRESS = "dcds_FIELD_REPORT_PROGRESS";
	public static final String dcds_RESOURCE_REQUEST_PROGRESS = "dcds_RESOURCE_REQUEST_PROGRESS";
	public static final String dcds_SUCCESSFUL_GET_USER_ORGANIZATION_INFO = "dcds_SUCCESSFUL_GET_USER_ORGANIZATION_INFO";
	public static final String dcds_OPENAM_AUTH_TIMEOUT = "dcds_OPENAM_AUTH_TIMEOUT";
	
	public static final String dcds_LOCAL_MAP_FEATURES_CLEARED = "dcds_LOCAL_MAP_FEATURES_CLEARED";
	public static final String dcds_LOCAL_CHAT_CLEARED = "dcds_LOCAL_CHAT_CLEARED";
	public static final String dcds_LOCAL_REPORTS_CLEARED = "dcds_LOCAL_REPORTS_CLEARED";
	
	public static final String dcds_SWITCH_TO_OFFLINE_MODE = "dcds_SWITCH_TO_OFFLINE_MODE";
	public static final String dcds_SWITCH_TO_ONLINE_MODE = "dcds_SWITCH_TO_ONLINE_MODE";
}
