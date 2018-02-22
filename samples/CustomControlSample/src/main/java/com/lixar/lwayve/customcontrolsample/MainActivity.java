package com.lixar.lwayve.customcontrolsample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.lixar.lwayve.customcontrolsample.databinding.ActivityMainBinding;
import com.lixar.lwayve.sdk.core.LwayveConnectionCallback;
import com.lixar.lwayve.sdk.core.LwayveSdk;
import com.lixar.lwayve.sdk.events.EventHelper;
import com.lixar.lwayve.sdk.events.PlaybackEvent;
import com.lixar.lwayve.sdk.experience.OuterBandAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.lixar.lwayve.sdk.experience.OuterBandAction.HOTEL;
import static com.lixar.lwayve.sdk.experience.OuterBandAction.MAP;
import static com.lixar.lwayve.sdk.experience.OuterBandAction.SHARE;
import static com.lixar.lwayve.sdk.experience.OuterBandAction.TICKET;
import static com.lixar.lwayve.sdk.experience.OuterBandAction.URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PERMISSIONS_REQUEST_CODE = 100;

    private LwayveSdk lwayveSdk;

    private ActivityMainBinding viewBinding;

    private PlaybackEventsReceiver playbackEventsReceiver;
    private MediaBrowserConnectionCallback mediaBrowserConnectionCallback;

    private boolean connectingToMediaBrowser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewBinding.setActivity(this);
        checkPermissions();
        initializeLwayveSdk();
    }

    private void initializeLwayveSdk() {
        LwayveSdk.connect(new LwayveConnectionCallback() {
            @Override
            public void onConnected(@NonNull LwayveSdk lwayveSdk) {
                MainActivity.this.lwayveSdk = lwayveSdk;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onLwayveSdkInitialized();
                    }
                });
            }
        });
    }

    private void onLwayveSdkInitialized() {
        if (connectingToMediaBrowser) {
            connectToMediaBrowser();
        }
    }

    private String getUnplayedItemsCount() {
        return "Number of unplayed items: " + lwayveSdk.getUnplayedItemsCount();
    }

    private String getClipDuration() {
        long duration = lwayveSdk.getCurrentQueueItemDuration();
        return "Current audio clip duration: " + duration + " ms";
    }

    @Override
    protected void onResume() {
        super.onResume();
        connectToMediaBrowser();
    }

    private void connectToMediaBrowser() {
        if (lwayveSdk == null) {
            connectingToMediaBrowser = true;
            return;
        }
        registerPlaybackEventsReceiver();
        mediaBrowserConnectionCallback = new MediaBrowserConnectionCallback();
        lwayveSdk.connectToMediaBrowser(this, mediaBrowserConnectionCallback);
    }

    private void registerPlaybackEventsReceiver() {
        IntentFilter filter = new IntentFilter(EventHelper.PLAYBACK_AUDIO_EVENT_ACTION);
        playbackEventsReceiver = new PlaybackEventsReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(playbackEventsReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        disconnectFromMediaBrowser();
    }

    private void disconnectFromMediaBrowser() {
        connectingToMediaBrowser = false;
        if (lwayveSdk == null) {
            return;
        }
        lwayveSdk.disconnectFromMediaBrowser();
        mediaBrowserConnectionCallback = null;
        unregisterPlaybackEventsReceiver();
    }

    private void unregisterPlaybackEventsReceiver() {
        if (playbackEventsReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(playbackEventsReceiver);
            playbackEventsReceiver = null;
        }
    }

    public void onPlayClicked() {
        lwayveSdk.play();
    }

    public void onPauseClicked() {
        lwayveSdk.pause();
    }

    public void onClearClicked() {
        lwayveSdk.clearPlayedItems();
        lwayveSdk.refreshPlayingQueue();
    }

    public void onPrevClicked() {
        lwayveSdk.prev();
    }

    public void onNextClicked() {
        lwayveSdk.next();
    }

    public void onRefreshClicked() {
        updateLocations();
        updateUserLikes();
        lwayveSdk.refreshPlayingQueue();
    }

    public void onUrlClicked() {
        lwayveSdk.executeOuterBandAction(URL);
    }

    public void onMapClicked() {
        lwayveSdk.executeOuterBandAction(MAP);
    }

    public void onTicketsClicked() {
        lwayveSdk.executeOuterBandAction(TICKET);
    }

    public void onShareClicked() {
        lwayveSdk.executeOuterBandAction(SHARE);
    }

    public void onHotelClicked() {
        lwayveSdk.executeOuterBandAction(HOTEL);
    }

    public void onRecordClicked() {
        lwayveSdk.startRecordActivity(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_settings) {
            SettingsActivity.startActivity(this);
            return true;
        }
        return false;
    }

    private void checkPermissions() {
        List<String> permissions = new ArrayList<>();
        if (shouldCheckForLocationPermission(this)) {
            permissions.add(ACCESS_FINE_LOCATION);
        }
        if (!permissions.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissions.toArray(new String[permissions.size()]), PERMISSIONS_REQUEST_CODE);
        }
    }

    public static boolean shouldCheckForLocationPermission(Context context) {
        return ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED;
    }

    private void updateLocations() {
        String input = viewBinding.locationsInput.getText().toString();
        if (!TextUtils.isEmpty(input)) {
            lwayveSdk.setUserLocations(extractTags(input));
        } else {
            lwayveSdk.setUserLocations(new HashSet<String>());
        }
    }

    private void updateUserLikes() {
        String input = viewBinding.userLikesInput.getText().toString();
        if (!TextUtils.isEmpty(input)) {
            lwayveSdk.setUserLikes(extractTags(input));
        } else {
            lwayveSdk.setUserLikes(new HashSet<String>());
        }
    }

    @NonNull
    private HashSet<String> extractTags(String input) {
        return new HashSet<>(new ArrayList<>(Arrays.asList(input.trim().split("\\s*,\\s*"))));
    }

    private class MediaBrowserConnectionCallback extends MediaBrowserCompat.ConnectionCallback {

        @Override
        public void onConnected() {
            if (lwayveSdk.getPlayingQueue() != null && !lwayveSdk.getPlayingQueue().isEmpty()) {
                int index = lwayveSdk.getCurrentQueueItemIndex();
                boolean isFirst = isFirstItem(index);
                boolean isLast = isLastItem(index);
                viewBinding.prevBtn.setEnabled(!isFirst);
                viewBinding.nextBtn.setEnabled(!isLast);
                viewBinding.playBtn.setEnabled(!isLast);
                viewBinding.pauseBtn.setEnabled(!isLast);
            }
        }

    }

    private class PlaybackEventsReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            PlaybackEvent event = (PlaybackEvent) intent.getExtras().get(EventHelper.PLAYBACK_AUDIO_EVENT_TYPE_KEY);
            Log.i(TAG, "got: " + event.name() + " event");

            int index = lwayveSdk.getCurrentQueueItemIndex();
            switch (event) {
                case PREPARED_EVENT:
                    viewBinding.playBtn.setEnabled(true);
                    viewBinding.pauseBtn.setEnabled(true);
                    viewBinding.prevBtn.setEnabled(!isFirstItem(index));
                    viewBinding.nextBtn.setEnabled(!isLastItem(index));
                    updateDebugInfo(true);
                    break;
                case NOT_PREPARED_EVENT:
                    viewBinding.playBtn.setEnabled(false);
                    viewBinding.pauseBtn.setEnabled(false);
                    viewBinding.prevBtn.setEnabled(false);
                    viewBinding.nextBtn.setEnabled(false);
                    updateDebugInfo(false);
                    break;
                case END_OF_PLAYLIST_EVENT:
                    viewBinding.prevBtn.setEnabled(true);
                    viewBinding.playBtn.setEnabled(false);
                    viewBinding.pauseBtn.setEnabled(false);
                    viewBinding.nextBtn.setEnabled(false);
                    updateDebugInfo(false);
                    break;
            }

            updateActionsForCurrentClip();
        }
    }

    private boolean isLastItem(int index) {
        return index >= lwayveSdk.getPlayingQueue().size() - 1;
    }

    private boolean isFirstItem(int index) {
        return index == 0;
    }

    private void updateDebugInfo(boolean prepared) {
        StringBuilder debugInfo = new StringBuilder();
        if (prepared) {
            debugInfo.append(getClipDuration())
                    .append("\n\n");
        }
        debugInfo.append(getUnplayedItemsCount())
                .append("\n\n")
                .append(lwayveSdk.generateDebugInfo())
                .append(lwayveSdk.generatePlaylistDebugInfo())
                .append(lwayveSdk.generateQueueDebugInfo());
        viewBinding.debugInfoTxt.setText(debugInfo);
    }

    private void updateActionsForCurrentClip() {
        viewBinding.urlBtn.setEnabled(false);
        viewBinding.mapBtn.setEnabled(false);
        viewBinding.ticketBtn.setEnabled(false);
        viewBinding.shareBtn.setEnabled(false);
        viewBinding.hotelBtn.setEnabled(false);

        for (OuterBandAction action : lwayveSdk.getOuterBandActions()) {
            switch (action) {
                case URL:
                    viewBinding.urlBtn.setEnabled(true);
                    break;
                case MAP:
                    viewBinding.mapBtn.setEnabled(true);
                    break;
                case TICKET:
                    viewBinding.ticketBtn.setEnabled(true);
                    break;
                case SHARE:
                    viewBinding.shareBtn.setEnabled(true);
                    break;
                case HOTEL:
                    viewBinding.hotelBtn.setEnabled(true);
                    break;
            }
        }
    }

}
