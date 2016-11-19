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

import android.content.Context;
import android.content.res.Resources;

public class Constants {

	public static final String dcds_UTF8 = "UTF-8";
	public static final String dcds_ISO_8859_1 = "ISO-8859-1";
	public static final String dcds_LRF_DEVICE_NAME = "TP360B";
	public static final String dcds_BT_SERIALPORT_SERVICEID = "00001101-0000-1000-8000-00805F9B34FB";
	public static final String dcds_USER_PREFERENCES = "dcds_USER_PREFERENCES";
	public static final String dcds_REMEMBER_USER = "dcds_REMEMBER_USER";
	public static final String dcds_AUTO_LOGIN = "dcds_AUTO_LOGIN";
	public static final String dcds_USER_KEY = "VAYHxGeIOY4lQ7J55mYoJw==:IUtPpNtl2yRmqGLqbE4QVBo6VID00J+7lc42oWSJdMY=:BxbKRpeWgehf3fK1yfbZcLbsZWw6Ec/7TKnetMOb5Bs=";
	public static final String dcds_FIRST_LOGIN = "dcds_FIRST_LOGIN";
	
	public static final String PREFERENCES_NAME = "dcds.pref";
	public static final int PREFERENCES_MODE = 0;
	public static final String USER_DATA = "user_data";
	public static final String USER_ID = "user_id";
	public static final String USER_ORG_ID = "user_org_id";
	public static final String CURRENT_USER_ORG = "current_user_org";
	public static final String USER_SESSION_ID = "user_session_id";
	public static final String USER_NAME = "user_name";
	public static final String PASSWORD = "password";
	
	public static final String INCIDENT_ID = "incident_id";
	public static final String INCIDENT_NAME = "incident_name";
	public static final String INCIDENT_LATITUDE = "incident_latitude";
	public static final String INCIDENT_LONGITUDE = "incident_longitude";
	
	public static final String COLLABROOM_ID = "collabroom_id";
	public static final String COLLABROOM_NAME = "collabroom_name";
	public static final String WORKSPACE_ID = "workspace_id";
	public static final String dcds_DEBUG_ANDROID_TAG = "dcds";
	public static final String dcds_LRF_DEBUG_ANDROID_TAG = "dcds_LRF";
	public static final String dcds_DATABASE_NAME = "dcds.db";
	public static final int dcds_DATABASE_VERSION = 6;

	public static final int LOCATION_UTM = 3;
	public static final int LOCATION_MGRS = 4;
	
	public static final String LAST_LATITUDE = "last_latitude";
	public static final String LAST_LONGITUDE = "last_longitude";
	public static final String LAST_ALTITUDE = "last_altitude";
	public static final String LAST_ACCURACY = "last_accuracy";
	public static final String LAST_COURSE = "last_course";
	public static final String LAST_MDT_TIME = "last_mdt_time";
	public static final String LAST_HR = "last_hr";
	public static final String LAST_HSI = "last_hsi";
	
	public static final String WEATHER_PAYLOAD = "weather_payload";
	
	public static final String PREVIOUS_INCIDENT_ID = "previous_incident_id";
	public static final String PREVIOUS_INCIDENT_NAME = "previous_incident_name";

	public static final String PREVIOUS_COLLABROOM = "previous_collabroom";	
	public static final String SELECTED_COLLABROOM = "selected_collabroom";

	public static final String SAVED_INCIDENTS = "saved_incidents";
	public static final String SAVED_COLLABROOMS = "saved_collabrooms";
	public static final String SAVED_ORGANIZATIONS = "saved_organizations";
	
	public static final String dcds_MAP_MARKUP_STATE = "dcds_MAP_MARKUP_STATE";
	public static final String dcds_MAP_MARKUP_COORDINATES = "dcds_MAP_MARKUP_COORDINATES";
	public static final String dcds_MAP_PREVIOUS_CAMERA = "dcds_MAP_PREVIOUS_CAMERA";
	public static final String dcds_MAP_TRAFFIC_ENABLED = "dcds_MAP_TRAFFIC_ENABLED";
	public static final String dcds_MAP_INDOOR_ENABLED = "dcds_MAP_INDOOR_ENABLED";
	public static final String dcds_MAP_TYPE = "dcds_MAP_TYPE";
	public static final String dcds_MAP_CURRENT_SHAPE_TYPE = "dcds_MAP_CURRENT_SHAPE_TYPE";
	public static final String dcds_MAP_COORDINATES_COLOR_RED = "dcds_MAP_COORDINATES_RED";
	public static final String dcds_MAP_COORDINATES_COLOR_GREEN = "dcds_MAP_COORDINATES_GREEN";
	public static final String dcds_MAP_COORDINATES_COLOR_BLUE = "dcds_MAP_COORDINATES_BLUE";
	public static final String dcds_MAP_CURRENT_SYMBOL_RESOURCE_ID = "dcds_MAP_CURRENT_SYMBOL_RESOURCE_ID";
	public static final String dcds_MAP_ACTIVE_WFS_LAYERS = "dcds_MAP_ACTIVE_WFS_LAYERS";
	
	public static final String IPLANET_COOKIE_DOMAIN = "IPLANET_COOKIE_DOMAIN";
	public static final String AMAUTH_COOKIE_DOMAIN = "AMAUTH_COOKIE_DOMAIN";
	public static final String CUSTOM_COOKIE_DOMAIN = "CUSTOM_COOKIE_DOMAIN";
	
	public static final String dcds_TIME_FORMAT = "MM/dd kk:mm:ss";
	public static final String dcds_NO_RESULTS = "dcds_NO_RESULTS";
	
	public enum NavigationOptions {
		SELECTINCIDENT(0),
		OVERVIEW(1),
		GENERALMESSAGE(2),
		FIELDREPORT(3),
		RESOURCEREQUEST(4),
		WEATHERREPORT(5),
		MAPCOLLABORATION(6),
		CHATLOG(7),
		USERINFO(8),
		LOGOUT(9),
		DAMAGESURVEY(10),
		UXOREPORT(11),
		UXOFILTER(12);
		
		private final int value;
		
	    private NavigationOptions(int value) {
	        this.value = value;
	    }

	    public int getValue() {
	        return value;
	    }
	    public String getLabel(Context context) {
	        Resources res = context.getResources();
	        int resId = res.getIdentifier(this.name(), "string", context.getPackageName());
	        if (0 != resId) {
	            return (res.getString(resId));
	        }
	        return (name());
	    }

	}
}
