# LWAYVE SDK for Android
The following document provides background information on the LWAYVE platform as well as provides setup and usage instructions for the LWAYVE SDK for Android. The content in this document is divided into the following sections:
 
  - [Section 1: Introducing LWAYVE and Contextual Audio](#section-1-introducing-lwayve-and-contextual-audio)
    * [Background](#background)
    * [Who are the Players?](#who-are-the-players)
    * [What are the Components of the LWAYVE Environment?](#what-are-the-components-of-the-lwayve-environment)
    * [How Does LWAYVE Work?](#how-does-lwayve-work)
  - [Section 2: Implementing the LWAYVE SDK in an Android Project](#section-2-implementing-the-lwayve-sdk-in-an-android-project)
    * [Prerequisites](#prerequisites)
    * [Add the LWAYVE SDK (and optionally the ProxSee SDK) as Dependencies](#add-the-lwayve-sdk-and-optionally-the-proxsee-sdk-as-dependencies)
    * [Initialize the LWAYVE SDK](#initialize-the-lwayve-sdk)
    * [Add the LWAYVE Playback control to your app's UI](#add-the-lwayve-playback-control-to-your-apps-ui)
  - [Section 3: Reference Documentation](#section-3-reference-documentation)
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
Incorporating the LWAYVE and ProxSee SDKs in your Android project is a simple three-step process:
1. Add the LWAYVE SDK and ProxSee SDK add-on module as Dependencies
2. Initialize the LWAYVE SDK
3. Add the LWAYVE Playback control to your app's UI
 
### Prerequisites
The instructions have been provided below with the following assumptions:
- A Customer-specific environment has been provisioned
- An Authorization token has been provided
- An Experience Designer has already created a Contextual Audio Experience and uploaded it for your environment. Alternatively, a Sample Lixar Experience has been loaded into your environment.
- The corresponding audio files for the Contextual Audio Experience have been uploaded to the environment
- If ProxSee services are being used, Location tags needed for the LWAYVE Contextual Audio Experience have been defined within the ProxSee environment.
 
 
### Add the LWAYVE SDK (and optionally the ProxSee SDK) as Dependencies
The first step in setting up LWAYVE is to add the LWAYVE SDK (and the ProxSee SDK add-on module if desired) as dependencies in your project's build.gradle file. The LWAYVE SDK handles the time, location, and audio of the Contextual Audio Experience. The ProxSee SDK passes the location tags to the LWAYVE SDK so that the LWAYVE SDK can complete the location aspect of the Contextual Audio Experience.
 
#### Add the LWAYVE SDK as a Dependency
To include the LWAYVE SDK in your project, add the following line of code to the **dependencies** section of your **build.gradle**:
 
**Parameters**
 
- {sdkVersion} - The latest version of LWAYVE, which can be found at [http://search.maven.org/#search|ga|1|lwayve-sdk](http://search.maven.org/#search|ga|1|lwayve-sdk)
 
**Code**
 
```
compile 'com.lixar.lwayve:lwayve-sdk:{sdkVersion}'
```
 
#### Add the ProxSee SDK as a Dependency (optional)
To include the optional ProxSee SDK add-on module as a dependency in your project, add the following line of code to the **dependencies** section of your **build.gradle**.
 
**Parameters**
 
- {sdkVersion} - The version used for the ProxSee SDK module should match the version of LWAYVE used in the step above.
 
**Code**
 
```
compile 'com.lixar.lwayve:lwayve-proxsee:{sdkVersion}'
```
 
### Initialize the LWAYVE SDK
Add the following code to your Application class' initialization process (e.g., Application.onCreate()):
 
**Parameters**
 
- {authToken} - The authentication token for LWAYVE provided by Lixar. 
 
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

#### Configuration Options
There are several configuration parameters that can be set when initializing the LWAYVE SDK. These options are passed to the SDK during initialization through the LwayveSdkConfiguration object. An LwayveSdkConfiguration instance can be constructed with the LwayveSdkConfiguration.Builder class. The Builder supports the following methods:

- setAuthenticationToken(String authToken) (required) - Sets the JWT auth token to use to authenticate with the LWAYVE backend.
- setBaseUrl(String baseUrl) (optional) - Configures the url used to communicate with the LWAYVE backend.
- setLanguage(LwayveSdkLanguage language) (optional) - Sets the preferred language to use for audio clips.
- setNotificationLargeIconRes(int res) (optional) - Sets the drawable resource to use for the icon in the media player notification.
- setNotificationSmallIconRes(int res) (optional) - Sets the drawable resource to use as the status bar icon for the media player notification.
- setProxSeeSdkAdapterFactory(ProxSeeSdkAdapter.Factory factory) - Sets the ProxSeeSdkAdapter instance to use when initializing the LWAYVE SDK.

See the [LwayveSdkConfiguration.Builder](https://lwayve.github.io/android/docs/javadoc/reference/com/lixar/lwayve/sdk/core/LwayveSdkConfiguration.Builder.html)
 section of the Javadoc for more details.
 
#### Connect to the LWAYVE SDK
Once the SDK has been initialized you can call the connect() method to obtain the SDK instance.

**Code**

```
LwayveSdk.connect(new LwayveConnectionCallback() {
    public void onConnected(LwayveSdk instance) {
        lwayveSdk = instance;
    }
});
```
 
#### Initializing the ProxSee SDK add-on module
To enable the ProxSee SDK integration support in LWAYVE, ensure the optional ProxSee add-on dependency has been added to the application's build.gradle file. Next create and pass an instance of ProxSeeSdkAdapterFactory to LwayveSdkConfiguration.Builder when initializing the SDK:
  
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
  
### Add the LWAYVE Playback control to your app's UI
In order to interact with the LWAYVE SDK you will need to add the playback control to your applications UI. If you wish to listen for playback events broadcast by the SDK, you may optionally set an OnPlaybackEventListener. First, add the control to your Activity's xml layout file:
 
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
Next, add the following code to the Activity hosting the playback control:
 
```
public class MainActivity extends Activity {
 
    private LwayvePlaybackControlView lwayvePlaybackControlView;
 
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         
        lwayvePlaybackControls = (LwayvePlaybackControlView) findViewById(R.id.lwayve_playback_controls);
        
        // Add a PlabackEventListener to listen for PlaybackEvents broadcast by the SDK (optional)
        lwayvePlaybackControls.setOnPlaybackEventListener(new LwayvePlaybackControlView.OnPlaybackEventListener() {
            @Override
            public void onPlaybackEvent(PlaybackEventType eventType) {
                // Playback events can be received here to add custom handling in your app's UI
            }
        });
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
 
## Section 3: Reference Documentation
 
### API
You can test LWAYVE by using the API documented on Swagger. You can access Swagger through the following URL: [https://gateway.lwayve.com/swagger-ui/index.html](https://gateway.lwayve.com/swagger-ui/index.html)
 
### Classes 
For Javadoc documentation, refer to  [https://lwayve.github.io/android/docs/javadoc/reference/classes.html](#https://lwayve.github.io/android/docs/javadoc/reference/classes.html).

 
