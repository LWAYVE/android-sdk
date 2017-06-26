package com.lixar.lwayve.sampleA;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lixar.lwayve.sampleA.R;
import com.lixar.lwayve.sdk.core.LwayveSdk;
import com.lixar.lwayve.sdk.exceptions.SdkNotInitializedException;
import com.lixar.lwayve.sdk.services.audio.PlaybackEventType;
import com.lixar.lwayve.sdk.utils.BroadcastHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PERMISSIONS_REQUEST_CODE = 100;

    private LwayveSdk lwayveSdk;

    private final Set<String> locations = new HashSet<>();
    private final Set<String> userLikes = new HashSet<>();

    @BindView(R.id.txt_debug_info)
    TextView debugInfoTxt;

    @BindView(R.id.txt_locations)
    EditText locationsInput;

    @BindView(R.id.txt_likes)
    EditText userLikesInput;

    @BindView(R.id.btn_play)
    Button playBtn;

    @BindView(R.id.btn_pause)
    Button pauseBtn;

    @BindView(R.id.btn_stop)
    Button stopBtn;

    @BindView(R.id.btn_prev)
    Button prevBtn;

    @BindView(R.id.btn_next)
    Button nextBtn;

    private BroadcastReceiver playbackEventsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            PlaybackEventType event = (PlaybackEventType) intent.getExtras().get(BroadcastHelper.PLAYBACK_AUDIO_EVENT_TYPE_KEY);
            Log.i(TAG, "got: " + event.name() + " event");

            int index = lwayveSdk.getCurrentPlaylistItemIndex();
            switch (event) {
                case PREPARED_EVENT:
                    playBtn.setEnabled(true);
                    pauseBtn.setEnabled(true);
                    stopBtn.setEnabled(true);
                    prevBtn.setEnabled(!isFirstItem(index));
                    nextBtn.setEnabled(!isLastItem(index));
                    updateDebugInfo(true);
                    break;
                case NOT_PREPARED_EVENT:
                    playBtn.setEnabled(false);
                    pauseBtn.setEnabled(false);
                    stopBtn.setEnabled(false);
                    prevBtn.setEnabled(false);
                    nextBtn.setEnabled(false);
                    updateDebugInfo(false);
                    break;
                case END_OF_PLAYLIST_EVENT:
                    prevBtn.setEnabled(true);
                    playBtn.setEnabled(false);
                    pauseBtn.setEnabled(false);
                    stopBtn.setEnabled(false);
                    nextBtn.setEnabled(false);
                    updateDebugInfo(false);
                    break;
            }

        }
    };

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
                .append(lwayveSdk.generateDebugInfo());
        debugInfoTxt.setText(debugInfo);
    }

    private MediaBrowserCompat.ConnectionCallback mediaBrowserConnectionCallback = new MediaBrowserCompat.ConnectionCallback() {

        @Override
        public void onConnected() {
            if (lwayveSdk.getPlayingQueue() != null && !lwayveSdk.getPlayingQueue().isEmpty()) {
                int index = lwayveSdk.getCurrentPlaylistItemIndex();
                boolean isFirst = isFirstItem(index);
                boolean isLast = isLastItem(index);
                prevBtn.setEnabled(!isFirst);
                nextBtn.setEnabled(!isLast);
                playBtn.setEnabled(!isLast);
                pauseBtn.setEnabled(!isLast);
                stopBtn.setEnabled(!isLast);
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        checkPermissions();
        initializeLwayveSdk();
    }

    private void initializeLwayveSdk() {
        try {
            lwayveSdk = LwayveSdk.getInstance();
        } catch (SdkNotInitializedException e) {
            throw new RuntimeException("Lwayve SDK not initialized aborting");
        }
    }

    private String getUnplayedItemsCount() {
        return "Number of unplayed items: " + lwayveSdk.getUnplayedPlaylistItemsCount();
    }

    private String getClipDuration() {
        long duration = lwayveSdk.getCurrentPlaylistItemDuration();
        return "Current audio clip duration: " + duration + " ms";
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(BroadcastHelper.PLAYBACK_AUDIO_EVENT_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(playbackEventsReceiver, filter);
        lwayveSdk.connectToMediaBrowser(this, mediaBrowserConnectionCallback);
    }

    @Override
    protected void onPause() {
        super.onPause();
        lwayveSdk.disconnectFromMediaBrowser();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(playbackEventsReceiver);
    }

    @OnClick(R.id.btn_play)
    public void onStartClicked() {
        lwayveSdk.play();
    }

    @OnClick(R.id.btn_pause)
    public void onPauseClicked() {
        lwayveSdk.pause();
    }

    @OnClick(R.id.btn_stop)
    public void onStopClicked() {
        lwayveSdk.stop();
    }

    @OnClick(R.id.btn_prev)
    public void onPrevClicked() {
        lwayveSdk.prev();
    }

    @OnClick(R.id.btn_next)
    public void onNextClicked() {
        lwayveSdk.next();
    }

    @OnClick(R.id.btn_refresh)
    public void onRefreshClicked() {
        updateLocations();
        updateUserLikes();
        lwayveSdk.refreshPlaylist();
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
        String input = locationsInput.getText().toString();
        if (!TextUtils.isEmpty(input)) {
            Set<String> newLocations = extractTags(input);

            Set<String> toRemove = new HashSet<>(locations);
            toRemove.removeAll(newLocations);

            Set<String> toAdd = new HashSet<>(newLocations);
            toAdd.removeAll(locations);

            lwayveSdk.removeLocations(toRemove);
            lwayveSdk.addLocations(toAdd);

            locations.removeAll(toRemove);
            locations.addAll(toAdd);
        } else {
            lwayveSdk.removeLocations(locations);
            locations.clear();
        }
    }

    private void updateUserLikes() {
        String input = userLikesInput.getText().toString();
        if (!TextUtils.isEmpty(input)) {
            Set<String> newLikes = extractTags(input);

            Set<String> toRemove = new HashSet<>(userLikes);
            toRemove.removeAll(newLikes);

            Set<String> toAdd = new HashSet<>(newLikes);
            toAdd.removeAll(userLikes);

            lwayveSdk.removeUserLikes(toRemove);
            lwayveSdk.addUserLikes(toAdd);

            userLikes.removeAll(toRemove);
            userLikes.addAll(toAdd);
        } else {
            lwayveSdk.removeUserLikes(userLikes);
            userLikes.clear();
        }
    }

    @NonNull
    private HashSet<String> extractTags(String input) {
        return new HashSet<>(new ArrayList<>(Arrays.asList(input.trim().split("\\s*,\\s*"))));
    }

}