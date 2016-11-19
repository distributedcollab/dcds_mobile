#  DCDS Mobile (Android / iOS) 

## Synopsis

Contains the projects files for the Android and iOS version of DCDS Mobile

##Dependencies

###Android

DCDS Mobile is only compatible with Eclipse

When creating your Eclipse workspace, import all the packages in the "DCDS-mobile/android" directory

<ul>
  <li>DCDSMobile</li>
  <li>DCDSAndroidAPI</li>
  <li>android-async-http</li>
  <li>google-play-services_lib</li>
  <li>google-support-v7-appcompat</li>
  <li>gridlayout_v7</li>
  <li>nasa-worldwind-coordinate-converter</li>
  <li>nmea-handler_lib</li>
</ul>

The project dependencies should be preconfigured in each package's properties menu when imported to match the structure below. You can check these by right clicking each DCDS project and selecting Properties then Android.

DCDSMobile
<ul>
  <li>DCDSAndroidAPI</li>
  <li>nmea-handler_lib</li>
  <li>gridlayout_v7</li>
  <li>google-support-v7-appcompat</li>
</ul>

DCDSAndroidAPI  (Is Library)
<ul>
  <li>android-async-http</li>
  <li>google-play-services_lib</li>
  <li>gridlayout_v7</li>
  <li>nasa-worldwind-coordinate-converter</li>
</ul>

You can deploy the app by building the "DCDSMobile" project as an Android Application in Eclipse.

## Configuration

###Android

The configuration file can be found at this path: DCDS_mobile/android/DCDSAndroidAPI/res/values/config_strings.xml

This is where you will enter all of your DCDS web configuration information.

The App is setup to allow you to easily toggle between multiple DCDS instances from the settings menu within the app. If you only have one instance then you can disregard the second item in each string array or remove them. 

You will also need to enter your Google Maps API key here which you can register for on Googles developer console: https://developers.google.com/maps/documentation/android-api/signup

DCDS Mobile is setup to use Application Crash Reports for Android (ACRA-https://github.com/ACRA/acra) to auto send crash reports to a Gmail account of your choice and you can configure this at the bottom of the config_strings.xml


###iOS
DCDS iOS was built in Xcode Version 7.3.1. Project may not run as expected in higher versions.

DCDS Mobile uses Cocoa Pods which requires you to open it using the "DCDS Mobile.xcworkspace" file instead of the typical ".xcodeproj" file.

The configuration file can be found at this path: DCDS_mobile/ios/DCDS Mobile/Localized/Settings.bundle/Root.plist

The bottom half of the file is what needs to be configured. Starting at the "Select DCDS Server" field.

Your Google Maps API key needs to be entered in the AppDelegate.m file at this path DCDS_mobile/ios/DCDS Mobile/AppDelegate.m)
