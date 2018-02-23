# LWAYVE SDK for Android
The following document provides background information on the LWAYVE platform as well as provides setup and usage instructions for the LWAYVE SDK for Android. The content in this document is divided into the following sections:
 
  - [Section 1: Introducing LWAYVE and Contextual Audio](#section-1-introducing-lwayve-and-contextual-audio)
    * [Background](#background)
    * [Who are the Players?](#who-are-the-players)
    * [What are the Components of the LWAYVE Environment?](#what-are-the-components-of-the-lwayve-environment)
    * [How Does LWAYVE Work?](#how-does-lwayve-work)
  - [Section 2: Implementing the LWAYVE SDK in an Android Project](#section-2-implementing-the-lwayve-sdk-in-an-android-project)
    * [Prerequisites](#prerequisites)
    * [1. Adding the LWAYVE SDK (and optional add-on modules) as Dependencies](#1-adding-the-lwayve-sdk-and-optional-add-on-modules-as-dependencies)
    * [2. Initializing the LWAYVE SDK](#2-initializing-the-lwayve-sdk)
    * [3. Integrating LWAYVE into an Application](#3-integrating-lwayve-into-an-application)
  - [Section 3: Customization and Configuration](#section-3-customization-and-configuration)
    * [SDK Initialization Options](#sdk-initialization-options)
    * [Recorder Module Initialization Options](#recorder-module-initialization-options)
    * [Supplementary Actions](#supplementary-actions)
    * [Colours](#colours)    
  - [Section 4: Reference Documentation](#section-4-reference-documentation)
    * [API](#api)
    * [Classes](#classes)
 
 
## Section 1: Introducing LWAYVE and Contextual Audio	
 
### Background
LWAYVE is an audio platform that provides event attendees with Contextual Audio Experiences and can be embedded to augment any mobile application. A Contextual Audio Experience serves as a personalized audio guide making any event as engaging, easy, and enjoyable as possible. 
 
Contextual Audio delivers optimal audio to a Listener based on their current context:
 
- Current time
- Current location
- Personal preferences
- Breaking information about and around the event
 
LWAYVE Contextual Audio Experiences are:
 
- **Contextual**. The audio in a Contextual Audio Experience will be different for each user at the event based on their user likes, location, time, and situation.
- **Choreographed.** Contextual Audio Experiences are carefully choreographed and curated to ensure the highest quality audio experience for the event.
- **Integrated**. The LWAYVE platform can be integrated into any mobile application on the most popular mobile devices and requires minimal interaction on the part of the Listener.
- **Measured**. The usage data collected with LWAYVE allows Customers to measure the impact of the audio advertising on a Listener's actions and then optimize the Contextual Audio Experience accordingly.
 
 
 
### Who are the Players?
 
There are six main players who have roles in the LWAYVE platform.  
 
- **Developers**: Integrate the LWAYVE platform into the mobile application. By integrating LWAYVE, the app will contain a Play button which Listeners can tap to get a Contextual Audio Experience based on time, location, and user likes. 
- **Experience Designer**: Choreographs the Contextual Audio Experience and sources the content. An Experience Designer essentially builds a Conextual Audio Experience based on an event (e.g., music festival). The Contextual Audio Experience contains audio that will be passed to the Listener based on their location, user likes, and time. For example, an Experience Designer for a music festival may choose to have Listeners hear traffic information on their way to the event and stage schedules while at the event.  
- **Listener**: The end-user that enjoys a high-value, personalized Contextual Audio Experience based on their location, user likes, and time. The Listener hears their Contextual Audio Experience by tapping a Play button on the mobile application.
- **Experience Conductor**: An Experience Conductor is the eyes and ears on the ground at an event. Experience Conductors control the situational audio in a Contextual Audio Experience and make adjustments based on real-time information. For example, at a music festival, if the concert that was originally scheduled for 9:00pm has been moved to 10:00pm, the Experience Conductor can adjust the Contextual Audio Experience accordingly. 
- **Customer**: The company or individual that has purchased LWAYVE to implement Contextual Audio Experiences. LWAYVE is integrated into the Customer's mobile application in order to provide Listeners with Contextual Audio Experiences.
- **Administrator**: The overall LWAYVE platform is managed by Lixar; however, Customers manage their own Contextual Audio Experiences and user roles. 
 
### What are the Components of the LWAYVE Environment?
The components to LWAYVE can be divided into the following main categories: Mobile SDKs, Customer Mobile Application, LWAYVE Service, and optionally, ProxSee Service.
 
#### Mobile SDKs
As part of their role, Developers integrate the following two SDKS into the mobile application:
- **LWAYVE SDK**: The LWAYVE SDK handles location (with the help of the ProxSee SDK), time, and contextual audio parameters. 
- **ProxSee SDK**: The ProxSee SDK passes location tags to the LWAYVE SDK. For full information on the ProxSee SDK, refer to https://github.com/proxsee.
 
#### Customer Mobile Application
- **Customer Mobile Application**: This is the mobile application in which the LWAYVE and ProxSee SDKs will be integrated. By integrating the SDKs, a simple LWAYVE Play button will appear in the mobile application. All Listeners need to do is tap the Play button to start their Contextual Audio Experience.  
 
#### Experience Service and Contextual Audio Experience 
- **Experience Service**: This is the backend platform on which the Experience Designer creates a Contextual Audio Experience. 
- **Contextual Audio Experience**: This is the content created on the Experience Service.
  - **Curated Audio**: This is the audio that has been gathered by the Experience Designer during the initial planning and design stages. This audio is planned around a Listener's user likes, time, and location. For example, for a music festival, the Experience Designer might create curated audio based on the performance schedule.   
  - **Situational Audio**: This is audio that is gathered by the Experience Conductor "on the ground" at the event. For example, at a music festival, sudden unpredicted weather changes may cause the Experience Conductor to create situational audio for safety precautions due to inclement weather (e.g., advising Listeners to take shelter).  
  - **User Likes**: These are settings set by the Listener in the Customer mobile application. 
  - **Time**: The Contextual Audio Experience will provide the Listener with different content depending on the time. For example, at a music festival, at 4:00pm when the gates open, the audio may be related to entrance lineups and at 8:00pm the audio may be related to the concert schedule.
  - **Location**: The Contextual Audio Experience will differ depending on the location of the Listener. For example, if the Listener is standing next to the food tent, the audio may be related to menu items and meal specials.
 
### How Does LWAYVE Work?
The following image depicts the high-level LWAYVE Contextual Audio Experience workflow:
 
![Communication Diagram](https://drive.google.com/uc?export=download&id=0B6FLg-DILCSrUm1ocFVGVnlMc0k)
 
 
## Section 2: Implementing the LWAYVE SDK in an Android Project
Incorporating the LWAYVE and ProxSee SDKs in your Android project is typically a three-step process:
1. Add the LWAYVE SDK and any add-on modules as Dependencies
2. Initialize the LWAYVE SDK
3. Add the LWAYVE Playback Control to your app's UI
 
### Prerequisites
The instructions have been provided below with the following assumptions:
- A Customer-specific environment has been provisioned
- An Authorization token has been provided
- An Experience Designer has already created a Contextual Audio Experience and uploaded it for your environment. Alternatively, a Sample Lixar Experience has been loaded into your environment.
- The corresponding audio files for the Contextual Audio Experience have been uploaded to the environment
- If ProxSee services are being used, Location tags needed for the LWAYVE Contextual Audio Experience have been defined within the ProxSee environment.
- The application into which LWAYVE will be integrated is using the [Gradle](https://gradle.org/) build system.

#### Supported Android Versions
The LWAYVE SDK is compatible with Android API 16 and later. The ProxSee add-on module requires API 18 or later for BLE support (it can be loaded on 16+ but Bluetooth scanning will be unavailable).

#### Android Plugin for Gradle
The LWAYVE SDK requires version 3.0.0 or later of the Android Plugin for Gradle. Earlier versions may show an error message if you choose to include the ProxSee add-on module in your project.

#### Android Studio Instant Run
The instant run feature of Android Studio should be disabled when building an app using the LWAYVE SDK as it can lead to unusual behaviour when running the app. This feature can be turned off in Android Studio by going to Preferences -> Build, Execution, Deployment -> Instant Run and disabling the option "Enable Instant Run to hot swap code/resource changes on deploy (default enabled)"
 
### 1. Adding the LWAYVE SDK (and optional add-on modules) as Dependencies
The first step in setting up LWAYVE is to add the LWAYVE SDK (and the ProxSee SDK add-on module if desired) as dependencies in your project's build.gradle. The LWAYVE SDK handles the time, location, and audio of the Contextual Audio Experience. The ProxSee SDK passes the location tags to the LWAYVE SDK so that the LWAYVE SDK can complete the location aspect of the Contextual Audio Experience.
 
#### LWAYVE SDK
To include the LWAYVE SDK in your project, add the following line of code to the **dependencies** section of your **build.gradle**:
 
**Parameters**
 
- {sdkVersion} - The latest version of LWAYVE, which can be found at [http://search.maven.org/#search|ga|1|lwayve-sdk](http://search.maven.org/#search|ga|1|lwayve-sdk)
 
**Code**
 
```
compile 'com.lixar.lwayve:lwayve-sdk:{sdkVersion}'
```
 
#### ProxSee SDK Module (optional)
To include the optional ProxSee SDK add-on module as a dependency in your project, add the lwayve-proxsee module to the **dependencies** section of the app's **build.gradle**.

**Code**
 
```
dependencies {
    compile 'com.lixar.lwayve:lwayve-proxsee:{sdkVersion}'
}
```

#### Recorder Module (optional)
To include the optional audio recorder add-on module as a dependency in your project, add the lwayve-recorder module to the **dependencies** section of your **build.gradle**. 

**Code**
 
```
dependencies {
    compile 'com.lixar.lwayve:lwayve-recorder:{sdkVersion}'
}
```

The recorder module has dependencies on some modules which are not available through Maven Central. The Jitpack.io repository will need to be added to your app's **build.gradle** if it is not already present.

**Code**
 
```
repositories {
    maven { url "https://jitpack.io" }
}
```
 
### 2. Initializing the LWAYVE SDK
Add the following code to your Application class' initialization process (e.g., Application.onCreate()):
 
**Parameters**
 
- {authToken} - The LWAYVE authentication token provided by Lixar. 
 
**Code**
 
```
LwayveSdkConfiguration configuration = new LwayveSdkConfiguration.Builder()
                .setAuthenticationToken({authToken})
                .build();
 
try {
    LwayveSdk.init(this, configuration);
} catch (InvalidSdkConfigurationException e) {
    // Error handling
} 
```

#### Connecting to the LWAYVE SDK
Once the SDK has been initialized you can call the connect() method to obtain the SDK instance.

**Code**

```
LwayveSdk.connect(new LwayveConnectionCallback() {
    public void onConnected(LwayveSdk instance) {
        lwayveSdk = instance;
    }
});
```
 
#### Initializing the ProxSee SDK Add-on Module
To enable the ProxSee SDK integration support in LWAYVE, ensure the optional ProxSee add-on dependency has been added to the application's build.gradle. Next create and pass an instance of ProxSeeSdkAdapterFactory to LwayveSdkConfiguration.Builder when initializing the SDK:
  
**Code**
 
```
LwayveSdkConfiguration configuration = new LwayveSdkConfiguration.Builder()
                .setAuthenticationToken({authToken})
                .setProxSeeSdkAdapterFactory(new ProxSeeSdkAdapterFactory(context))
                .build();
 
try {
    LwayveSdk.init(this, configuration);
} catch (InvalidSdkConfigurationException e) {
    // Error handling
} 
```

#### Initializing the Audio Recorder Add-on Module
To enable the audio recording integration support in LWAYVE, ensure the optional recorder add-on dependency has been added to the application's build.gradle. Next create and pass an instance of RecorderAdapterFactory to LwayveSdkConfiguration.Builder when initializing the SDK:
  
**Code**
 
```
LwayveSdkConfiguration configuration = new LwayveSdkConfiguration.Builder()
                .setAuthenticationToken({authToken})
                .setRecorderdapterFactory(new RecorderAdapterFactory())
                .build();
 
try {
    LwayveSdk.init(this, configuration);
} catch (InvalidSdkConfigurationException e) {
    // Error handling
} 
```
  
### 3. Integrating LWAYVE into an Application

#### Standard Implementation (Using LWAYVE Playback Control)
In order to interact with the LWAYVE SDK a prebuilt playback control is provided which you may integrate into an application's UI as a custom view.

A complete sample app using the prebuilt control can be found here: [Prebuilt Control Sample](https://github.com/LWAYVE/android-sdk/blob/master/samples/PrebuiltControlSample/src/main/java/com/lixar/lwayve/prebuiltcontrolsample)

First, add the control to your Activity's xml layout file:
 
```
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
             android:orientation="vertical"
             android:layout_width="match_parent"
             android:layout_height="match_parent">
 
	<com.lixar.lwayve.sdk.view.LwayvePlaybackControlView
			android:id="@+id/lwayve_playback_controls"
			android:layout_width="wrap_content"
			android:layout_height="wrap_content"
			android:layout_gravity="center_horizontal"/>
        
</FrameLayout>
```
Next, add the following code to your Activity to initialize the control:
 
```
public class MainActivity extends Activity {
 
    private LwayvePlaybackControlView lwayvePlaybackControlView;
 
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ...
         
        lwayvePlaybackControls = (LwayvePlaybackControlView) findViewById(R.id.lwayve_playback_controls);
        
        ...
    }
    
    @Override
    protected void onResume() {
    	lwayvePlaybackControlView.connectToMediaBrowser();
    }

    @Override
    protected void onPause() {
        super.onPause();
        lwayvePlaybackControlView.disconnectFromMediaBrowser();
    }
}
```
Playback events emitted by the SDK can be received by adding an OnPlaybackEventListener (optional):

```
@Override
protected void onCreate(@Nullable Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	...
    	
	lwayvePlaybackControls.setOnPlaybackEventListener(new LwayvePlaybackControlView.OnPlaybackEventListener() {
		@Override
		public void onPlaybackEvent(PlaybackEvent event) {
			// Playback events can be received here to add custom handling in your app's UI
		}
	});

	...
}

```

#### Custom Implementation
It is also possible for an app to implement it's own custom controls for interacting with the LWAYVE SDK if the prebuilt control is not satisfactory.

A complete sample app demonstrating how to interact with the SDK without the prebuilt control can be found here: [Custom Control Sample](https://github.com/LWAYVE/android-sdk/blob/master/samples/CustomControlSample/src/main/java/com/lixar/lwayve/customcontrolsample)

When opting not to use the prebuilt control playback events from the SDK can be received by registering a broadcast receiver:

```
private void registerPlaybackEventsReceiver() {
    IntentFilter filter = new IntentFilter(EventHelper.PLAYBACK_AUDIO_EVENT_ACTION);
    playbackEventsReceiver = new PlaybackEventsReceiver();
    LocalBroadcastManager.getInstance(this).registerReceiver(playbackEventsReceiver, filter);
}

private class PlaybackEventsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        PlaybackEvent event = (PlaybackEvent) intent.getExtras().get(EventHelper.PLAYBACK_AUDIO_EVENT_TYPE_KEY);
        Log.i(TAG, "got: " + event.name() + " event");
    }
}
```

Connecting to the media browser through the lwayveSdk instance:

```
public void onResume() {
	lwayveSdk.connectToMediaBrowser(activity, new MediaBrowserConnectionCallback() {
        public void onConnected() {
            // Once connected, playback commands such as lwayveSdk.play() or lwayveSdk.skipNext() 
            // can be sent to the SDK.
        }
	});
}
```

## Section 3: Customization and Configuration

### SDK Initialization Options

There are several configuration parameters that can be set when initializing the LWAYVE SDK. These options are passed to the SDK during initialization through the LwayveSdkConfiguration object. An LwayveSdkConfiguration instance can be constructed with the LwayveSdkConfiguration.Builder class. The Builder supports the following methods:

- setAuthenticationToken(String authToken) (required) - Sets the JWT auth token to use to authenticate with the LWAYVE backend.
- setBaseUrl(String baseUrl) (optional) - Configures the url used to communicate with the LWAYVE backend.
- setEventName(String name) (optional) - When set this will replace "LWAYVE" with the name of your choice for certain string resources used in the SDK. 
- setLanguage(ExperienceLanguage language) (optional) - Sets the preferred language to use for audio clips.
- setNotificationLargeIconRes(int res) (optional) - Sets the drawable resource to use for the icon in the media player notification.
- setNotificationSmallIconRes(int res) (optional) - Sets the drawable resource to use as the status bar icon for the media player notification.
- setProxSeeSdkAdapterFactory(ProxSeeSdkAdapter.Factory factory) - Sets the ProxSeeSdkAdapter instance to use when initializing the LWAYVE SDK.
- setRecorderAdapterFactory(RecorderAdapter.Factory factory) - Sets the RecorderAdapter instance to use when initializing the LWAYVE SDK.

See the [LwayveSdkConfiguration.Builder](https://lwayve.github.io/android/docs/javadoc/reference/com/lixar/lwayve/sdk/core/LwayveSdkConfiguration.Builder.html) section of the Javadoc for more details.

### Recorder Module Initialization Options

By default the LWAYVE SDK recorder module is configured to allow recording of up to sixty seconds of audio. You can reduce this limit to thirty seconds by passing the following option when creating the factory instance:
```
.setRecorderAdapterFactory(new RecorderAdapterFactory(RecordingDuration.THIRTY_SECONDS));
```
 
### Supplementary Actions

LWAYVE provides a set of customizable supplementary actions which can be enabled for any audio clip in an experience. There are five customizable actions provided: Tickets, Map, Link, Share and Hotel. Actions are enabled for a given audio clip based on whether a value has been defined for that action in the experience retrieved from the backend. The Tickets, Map, Link and Hotel actions all are url based actions meaning you may provide a valid url which will opened typically by the device's web browser. The Share action accepts a String and will open the device sharing dialog allowing the user to share the message of your choosing on their socail media accounts. 

#### Customizing the appearance of supplementary actions in the LWAYVE Playback Control

The supplementary actions for the current audio clip can be accessed from the Outer Band in the LWAYVE Playback Control. The Outer Band can be opened by long pressing or by swiping down on the play button. The default icons and text resources used in the Outer Band can be overridden if desired. The overridden values will apply for all audio clips in the experience. See the [API documentation](https://lwayve.github.io/android/docs/javadoc/reference/com/lixar/lwayve/sdk/core/LwayveSdk.html) for further information.

#### Record Action

A sixth action will appear in the Outer Band when recording functionality is enabled in the SDK. The function performed by this action cannot be customized and will always open the prebuilt recording UI. When recording is enabled the action will be enabled for all audio clips in the experience. If you wish to use the recording feature but would prefer to not include the record action in the Outer Band, you may disable this action from being added using the [LwayvePlaybackControlView.setShowRecordInOuterBand()](https://lwayve.github.io/android/docs/javadoc/reference/com/lixar/lwayve/sdk/view/LwayvePlaybackControlView.html#setShowRecordInOuterBand(boolean)) method.

#### Executing actions independent of the LWAYVE Playback Control

Supplementary actions for the current audio clip can be executed independent of the LWAYVE Playback Control by calling [LwayveSdk.getOuterBandActions()](https://lwayve.github.io/android/docs/javadoc/reference/com/lixar/lwayve/sdk/core/LwayveSdk.html#getOuterBandActions()) and [LwayveSdk.executeOuterBandAction(OuterBandAction)](https://lwayve.github.io/android/docs/javadoc/reference/com/lixar/lwayve/sdk/core/LwayveSdk.html#executeOuterBandAction(OuterBandAction)) methods. (Check out the [CustomControlSample](https://github.com/LWAYVE/android-sdk/blob/master/samples/CustomControlSample/src/main/java/com/lixar/lwayve/customcontrolsample/MainActivity.java) project for a complete example of implementing custom playback controls for LWAYVE.)

### Colours

#### Default Highlight Colour
The SDK supports customizing various colours used by the LWAYVE Playback Control and Audio Recording UI. In addition to allowing the colours to be configured independently, you can also override the default highlight colour which will change the default orange highlight colour used throughout the SDK to the colour of your choosing. To override the default highlight colour, override the colour resource default_highlight_color in your application's colors.xml.

```
<color tools:override="true" name="default_highlight_color">{colorValue}</color>
```

#### LWAYVE Playback Control
The LWAYVE Playback Control supports overriding the colours used for the play button, Outer Band icons and text, recording UI as well as the opacity of the play control itself. These options can be set either programmatically or on the LwayvePlaybackControlView in an xml layout. See the [API documentation](https://lwayve.github.io/android/docs/javadoc/reference/com/lixar/lwayve/sdk/view/LwayvePlaybackControlView.html) for the complete list of features which can be customized.

#### Audio Recording Window
The colours for the audio recording window can be configured through the [LwayvePlaybackControlView](https://lwayve.github.io/android/docs/javadoc/reference/com/lixar/lwayve/sdk/view/LwayvePlaybackControlView.html) if using the LWAYVE Playback Control.

The audio recording window can be launched independent of the LWAYVE Playback Control (provided the recording module is properly initialized.) This can be done by calling [LwayveSdk.startRecordActivity(Context, Bundle)](https://lwayve.github.io/android/docs/javadoc/reference/com/lixar/lwayve/sdk/core/LwayveSdk.html#startRecordActivity(Context,%20Bundle)). Colours can also be customized through the Bundle object passed to this method. See the [API documentation](https://lwayve.github.io/android/docs/javadoc/reference/com/lixar/lwayve/sdk/core/LwayveSdk.html#startRecordActivity(Context,%20Bundle)) for further information.
 
## Section 4: Reference Documentation
 
### API
You can test LWAYVE by using the API documented on Swagger. You can access Swagger through the following URL: [https://gateway.lwayve.com/swagger-ui/index.html](https://gateway.lwayve.com/swagger-ui/index.html)
 
### Classes 
For Javadoc documentation, refer to  [https://lwayve.github.io/android/docs/javadoc/reference/classes.html](#https://lwayve.github.io/android/docs/javadoc/reference/classes.html).

 
