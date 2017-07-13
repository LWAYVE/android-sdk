# LWAYVE SDK for Android
The following document provides background information on the LWAYVE platform as well as provides setup and usage instructions for the LWAYVE SDK for Android. The content in this document is divided into the following sections:
 
  - [Section 1: Introducing LWAYVE and Contextual Audio](#section-1-introducing-lwayve-and-contextual-audio)
    * [Background](#background)
    * [Who are the Players?](#who-are-the-players)
    * [What are the Components of the LWAYVE Environment?](#what-are-the-components-of-the-lwayve-environment)
    * [How Does LWAYVE Work?](#how-does-lwayve-work)
  - [Section 2: Implementing the LWAYVE SDK in an Android Project](#section-2-implementing-the-lwayve-sdk-in-an-android-project)
    * [Prerequisites](#prerequisites)
    * [Add the LWAYVE and ProxSee SDKs as Dependencies](#add-the-lwayve-and-proxsee-sdks-as-dependencies)
    * [Initialize the LWAYVE and ProxSee SDKs](#initialize-the-lwayve-and-proxsee-sdks)
    * [Enable Communication Between the LWAYVE and ProxSee SDKs](#enable-communication-between-the-lwayve-and-proxsee-sdks)
    * [Handle Audio](#handle-audio)
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
Incorporating the LWAYVE and ProxSee SDKs in your Android project is a simple four-step process:
1. Add the LWAYVE and ProxSee SDKs as Dependencies
2. Initialize the LWAYVE and ProxSee SDKs
3. Enable Communication Between the LWAYVE and ProxSee SDKs
4. Handle Audio
 
### Prerequisites
The instructions have been provided below with the following assumptions:
- A Customer-specific environment has been provisioned
- An Authorization token has been provided
- A ProxSee API key has been provided
- An Experience Designer has already created a Contextual Audio Experience and uploaded it for your environment. Alternatively, a Sample Lixar Experience has been loaded into your environment.
- The corresponding audio files for the Contextual Audio Experience have been uploaded to the environment
- If ProxSee services are being used, Location tags needed for the LWAYVE Contextual Audio Experience have been defined within the ProxSee environment.
 
 
### Add the LWAYVE and ProxSee SDKs as Dependencies
The first step in setting up LWAYVE is to add both the LWAYVE SDK and the ProxSee SDK as dependencies in your project's build.gradle file. The LWAYVE SDK handles the time, location, and audio of the Contextual Audio Experience. The ProxSee SDK passes the location tags to the LWAYVE SDK so that the LWAYVE SDK can complete the location aspect of the Contextual Audio Experience.
 
#### Add the LWAYVE SDK as a Dependency
To include the LWAYVE SDK in your project, add the following line of code to the **dependencies** section of your **build.gradle**:
 
**Parameters**
 
- {sdkVersion} - The latest version of LWAYVE, which can be found at [http://search.maven.org/#search|ga|1|lwayve-sdk](http://search.maven.org/#search|ga|1|lwayve-sdk)
 
**Code**
 
```
 
compile 'com.lixar.lwayve:lwayve-sdk:{sdkVersion}'
 
```
 
#### Add the ProxSee SDK as a Dependency
To include the ProxSee SDK as a dependency in your project, add the following line of code to the **dependencies** section of your **build.gradle**.
 
**Parameters**
 
- {sdkVersion} - The latest version of ProxSee, which can be found at [http://search.maven.org/#search|ga|1|proxsee-sdk](http://search.maven.org/#search|ga|1|proxsee-sdk)
 
**Code**
 
```
 
compile 'io.proxsee.sdk:proxsee-sdk:{sdkVersion}'
 
```
 
### Initialize the LWAYVE and ProxSee SDKs
The next step is to initialize (launch) both the LWAYVE SDK and the ProxSee SDK. 
 
#### Initialize the LWAYVE SDK
Add the following code to your mobile application's initialization process (e.g., Application.onCreate()).
 
**Parameters**
 
- {lwayveAuthToken} - The authentication token for LWAYVE provided by Lixar. 
 
**Code**
 
```
LwayveSdkConfiguration configuration = new LwayveSdkConfiguration.Builder()
                .setAuthenticationToken({lwayveAuthToken})
                .build();
 
try {
    LwayveSdk.init(this, configuration);
} catch (InvalidSdkConfigurationException e) {
 
} catch (SdkNotInitializedException e) {
 
}
 
```
 
#### Initialize the ProxSee SDK
Add the following code to your mobile application's initialization process (e.g., Application.onCreate()).
 
**Parameters**
 
[proxseeApikey} - The API key provided by Lixar.
 
**Code**
 
```
 
ProxSeeSDKManager.initialize(app, {proxseeApiKey});
ProxSeeSDKManager.getInstance().start();
 
```
 
### Enable Communication Between the LWAYVE and ProxSee SDKs
Now that the LWAYVE SDK and ProxSee SDK have been added to your project and initialized, you need to ensure that they can communicate with each other. This is done by sending the LWAYVE Device ID to the ProxSee SDK as well as sending ProxSee locations to the LWAYVE SDK. Sending the LWAYVE Device ID to the ProxSee SDK is required to link the data captured by LWAYVE to the location tag data captured by the Proxsee SDK. Sending the ProxSee tag locations to the LWAYVE SDK (and creating a ProxSeeBroadcastReceiver) allows the LWAYVE SDK to listen for location tag changes.
 
#### Send LWAYVE Device ID to ProxSee SDK
Add the following code to your mobile application.
 
 
```
 
private void setProxSeeMetadata() {
    HashMap<String, Object> metadata = new HashMap<>();
    metadata.put("lwayve_deviceid", lwayveSdk.getDeviceUUID());
 
    ProxSeeSDKManager.getInstance().updateMetadata(metadata, new ProxSeeSDKManager.CompletionHandler() {
        @Override
        public void onUpdateCompleted(boolean success, Exception e) {
            if (!success) {
                Log.e(LOGTAG, "Error sending lwayve deviceid to proxsee");
            }
        }
    });
}
 
```
 
#### Send ProxSee Locations to LWAYVE SDK
Add the following code to your mobile application.
 
```
 
private final Set<String> currentTags = new HashSet<>();
private LwayveSdk lwayveSdk;
private TagReceiver tagReceiver = new TagReceiver();
 
@Override
protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    try {
        lwayveSdk = LwayveSdk.getInstance();
    } catch (SdkNotInitializedException e) {
 
    }
    registerReceiver(tagReceiver, new IntentFilter(ProxSeeBroadcaster.TAGS_CHANGED_ACTION));
}
 
@Override
public void onDestroy() {
    super.onDestroy();
    unregisterReceiver(tagReceiver);
}
 
public class TagReceiver extends ProxSeeBroadcastReceiver {
 
@Override
public void didChangeTagsSet(BeaconNotificationObject beaconNotificationObject) {
    Log.d(LOGTAG, "Tags Set Changed");
    setCurrentTags(beaconNotificationObject);
    findExpiredTags(beaconNotificationObject);
    findNewTags(beaconNotificationObject);
}
}
 
private void setCurrentTags(BeaconNotificationObject beaconNotificationObject) {
    currentTags.clear();
    currentTags.addAll(beaconNotificationObject.getCurrentTagsChangedSet().getTags());
}
 
private void findNewTags(BeaconNotificationObject bno) {
    Set<String> newTags = new HashSet<>();
    for (String tag : currentTags) {
        if (!bno.getPreviousTagsChangedSet().getTags().contains(tag)) {
            newTags.add(tag);
        }
    }
    onTagsFound(newTags);
}
 
private void onTagsFound(Set<String> tags) {
    lwayveSdk.addLocations(tags);
}
 
private void findExpiredTags(BeaconNotificationObject bno) {
    Set<String> expiredTags = new HashSet<>();
    for (String tag : bno.getPreviousTagsChangedSet().getTags()) {
        if (!currentTags.contains(tag)) {
            expiredTags.add(tag);
        }
    }
    onTagsLost(expiredTags);
}
 
private void onTagsLost(Set<String> tags) {
    lwayveSdk.removeLocations(tags);
}
 
```
 
### Handle Audio 
You must configure your mobile application to send playback commands (e.g., Play, Pause) to the LWAYVE SDK as well as receive playback commands from the LWAYVE SDK. 

There are two options for handling audio:
- Option 1: Prebuilt Implementation
- Option 2: Manual Implementation

Using the prebuilt control allows for quick and easy integration, however, note that using this option does not allow for much customization. While the manual implementation may be more difficult than the prebuilt implementation outlined in Option 1, the manual implementation allows for more freedom in the look and feel of the UI elements. 

#### Option 1: Prebuilt Implementation

Add the control to your Activity's xml layout file:
 
Example
 
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
Add the following code to your Activity:

Example
 
```
public class MainActivity extends Activity {
 
    private LwayveSdk lwayveSdk;
 
	@Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 
		try {
            lwayveSdk = LwayveSdk.getInstance();
        } catch (SdkNotInitializedException e) {
            throw new RuntimeException("Lwayve SDK not initialized aborting");
        }
        
        LwayvePlaybackControlView lwayvePlaybackControls = (LwayvePlaybackControlView) findViewById(R.id.lwayve_playback_controls);
        lwayvePlaybackControls.setOnPlaybackEventListener(new LwayvePlaybackControlView.OnPlaybackEventListener() {
            @Override
            public void onPlaybackEvent(PlaybackEventType eventType) {
                updateDebugInfo();
            }
        });
    }
 
}
 
```
#### Option 2: Manual Implementation

There are two main steps in the manual implementation:
- Send Playback Commands to the LWAYVE SDK
- Receive Playback Commands from the LWAYVE SDK
 
##### Send Playback Commands to the LWAYVE SDK
The following code is an example of how you can configure a mobile application to send playback commands to the LWAYVE SDK. Note that this is example code. Your actual code may differ. 
 
```
private LwayveSdk lwayveSdk;
private Button playBtn;
private Button nextBtn;
private Button prevBtn;
private boolean isPlaying;
 
@Override
protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
 
    try {
        lwayveSdk = LwayveSdk.getInstance();
    } catch (SdkNotInitializedException e) {
 
    }
 
    playBtn = (Button) findViewById(R.id.playBtn);
    nextBtn = (Button) findViewById(R.id.nextBtn);
    prevBtn = (Button) findViewById(R.id.prevBtn);
 
    playBtn.setEnabled(false);
    prevBtn.setEnabled(false);
    nextBtn.setEnabled(false);
 
    playBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!isPlaying) {
                lwayveSdk.play();
                isPlaying = true;
            } else {
                lwayveSdk.pause();
                isPlaying = false;
            }
            updatePlayBtnState();
        }
    });
 
    nextBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            lwayveSdk.next();
        }
    });
 
    prevBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            lwayveSdk.prev();
        }
    });
}
 
private void updatePlayBtnState() {
    int res = isPlaying ? R.drawable.ic_stop_selector : R.drawable.ic_play_selector;
    playBtn.setImageResource(res);
}
```
##### Receive Playback Commands from the LWAYVE SDK
 
Based on the example provided above on configuring a mobile application to send playback commands to the LWAYVE SDK, the following example is an example of companion code for receiving playback commands from the LWAYVE SDK. Note that this is example code. Your code may differ depending on how you implement the sending of playback commands. 
 
```
... (See above example)
private PlaybackEventsBroadcastReceiver playbackEventsReceiver;
 
@Override
protected void onCreate(@Nullable Bundle savedInstanceState) {
	... (See above example)
 
    playbackEventsReceiver = new PlaybackEventsBroadcastReceiver();
    IntentFilter filter = new IntentFilter(PLAYBACK_AUDIO_EVENT_ACTION);
 
    LocalBroadcastManager.getInstance(this).registerReceiver(playbackEventsReceiver, filter);
}
 
@Override
public void onDestroy() {
    super.onDestroy();
    LocalBroadcastManager.getInstance(this).unregisterReceiver(playbackEventsReceiver);
}
 
private class PlaybackEventsBroadcastReceiver extends BroadcastReceiver {
 
    private final String LOGTAG = PlaybackEventsBroadcastReceiver.class.getSimpleName();
 
    @Override
    public void onReceive(Context context, Intent intent) {
        PlaybackEventType eventType = (PlaybackEventType) intent.getSerializableExtra(BroadcastHelper.PLAYBACK_AUDIO_EVENT_TYPE_KEY);
 
        int index = lwayveSdk.getCurrentPlaylistItemIndex();
        switch (eventType) {
            case PREPARED_EVENT:
                playBtn.setEnabled(true);
                prevBtn.setEnabled(!isFirstItem(index));
                nextBtn.setEnabled(!isLastItem(index));
                break;
            case NOT_PREPARED_EVENT:
                playBtn.setEnabled(false);
                prevBtn.setEnabled(false);
                nextBtn.setEnabled(false);
                break;
            case END_OF_PLAYLIST_EVENT:
                isPlaying = false;
                prevBtn.setEnabled(true);
                playBtn.setEnabled(false);
                nextBtn.setEnabled(false);
                updatePlayBtnState();
                break;
            case STOP_EVENT:
            case PAUSE_EVENT:
                isPlaying = false;
                updatePlayBtnState();
                break;
            case PLAY_EVENT:
                isPlaying = true;
                updatePlayBtnState();
                break;
        }
    }
}
 
private boolean isLastItem(int index) {
    return index >= lwayveSdk.getPlayingQueue().size() - 1;
}
 
private boolean isFirstItem(int index) {
    return index == 0;
}
```
 
## Section 3: Reference Documentation
 
### API
You can test LWAYVE by using the API documented on Swagger. You can access Swagger through the following URL: [https://gateway.lwayve.com/swagger-ui/index.html](https://gateway.lwayve.com/swagger-ui/index.html)
 
### Classes 
For Javadoc documentation, refer to  [https://lwayve.github.io/android/docs/javadoc/reference/classes.html](#https://lwayve.github.io/android/docs/javadoc/reference/classes.html).

 
