package com.lixar.lwayve.customcontrolsample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.lixar.lwayve.customcontrolsample.databinding.ActivityMainBinding;
import com.lixar.lwayve.sdk.core.LwayveConnectionCallback;
import com.lixar.lwayve.sdk.core.LwayveSdk;
import com.lixar.lwayve.sdk.events.EventHelper;
import com.lixar.lwayve.sdk.events.PlaybackEvent;
import com.lixar.lwayve.sdk.experience.ClipAction;

import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private static final int PROGRESS_UPDATE_DELAY_MS = 33; // ~30fps

    private LwayveSdk lwayveSdk;

    private ActivityMainBinding viewBinding;

    private PlaybackEventsReceiver playbackEventsReceiver;
    private MediaBrowserConnectionCallback mediaBrowserConnectionCallback;

    private boolean connectingToMediaBrowser;

    private ActionPopup actionPopup;

    private Handler handler;
    private TimerRunnable timerRunnable;
    private long timerPrevTimestamp;
    private int currentProgress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        initView();
        checkPermissions();
        initializeLwayveSdk();
    }

    private void initView() {
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewBinding.setActivity(this);
        actionPopup = new ActionPopup(this, viewBinding.actions, new ActionPopup.ActionClickListener() {
            @Override
            public void onActionClicked(ClipAction action) {
                lwayveSdk.executeClipAction(action);
            }
        });
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
        if (lwayveSdk.isPlaying()) {
            lwayveSdk.pause();
        } else {
            lwayveSdk.play();
        }
    }

    public void onPrevClicked() {
        lwayveSdk.prev();
    }

    public void onNextClicked() {
        lwayveSdk.next();
    }

    public void onRefreshClicked() {
        lwayveSdk.refreshPlayingQueue();
    }

    public void showActionsMenu() {
        actionPopup.show(lwayveSdk.getClipActions());
    }

    public void startRecordActivity() {
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
        switch (item.getItemId()) {
            case R.id.menu_settings:
                SettingsActivity.startActivity(this);
                return true;
            case R.id.menu_tags:
                new TagDialogFragment().show(getFragmentManager(), TagDialogFragment.class.getSimpleName());
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

    private void handlePlaybackEvent(PlaybackEvent event) {
        switch (event) {
            case PREPARED_EVENT:
                viewBinding.playBtn.setEnabled(true);
                viewBinding.prevBtn.setEnabled(!lwayveSdk.isStartOfQueue());
                viewBinding.nextBtn.setEnabled(!lwayveSdk.isEndOfQueue());
                break;
            case NOT_PREPARED_EVENT:
                viewBinding.playBtn.setEnabled(false);
                viewBinding.prevBtn.setEnabled(false);
                viewBinding.nextBtn.setEnabled(false);
                break;
            case END_OF_PLAYLIST_EVENT:
                viewBinding.prevBtn.setEnabled(true);
                viewBinding.playBtn.setEnabled(false);
                viewBinding.nextBtn.setEnabled(false);
            case SKIP_NEXT_EVENT:
            case SKIP_PREV_EVENT:
            case STOP_EVENT:
            case COMPLETION_EVENT:
                resetSeekBar();
                break;
            case PAUSE_EVENT:
                cancelTimer();
                break;
            case PLAY_EVENT:
                viewBinding.prevBtn.setEnabled(true);
                initTimer();
                break;
            case REWIND_EVENT:
                resetSeekBar();
                if (lwayveSdk.isPlaying()) {
                    initTimer();
                }
                break;
        }

        updateDebugInfo();
        updateNewContentBanner();
        updateTicker();
        updatePlayBtnState();
        updateActionsMenuEnabled();
        updateCurrentTrackIndex();
    }

    private void updateDebugInfo() {
        StringBuilder debugInfo = new StringBuilder()
                .append(lwayveSdk.generateDebugInfo())
                .append(lwayveSdk.generatePlaylistDebugInfo())
                .append(lwayveSdk.generateQueueDebugInfo());
        viewBinding.debugInfoTxt.setText(debugInfo);
    }

    private void updateNewContentBanner() {
        boolean newContent = lwayveSdk.isNewContentAvailable() && currentProgress == 0;
        boolean situationalUpdate = lwayveSdk.isSituationalUpdatePending();

        if (situationalUpdate) {
            viewBinding.newContentTxt.setText(R.string.new_situational);
        } else if (newContent) {
            viewBinding.newContentTxt.setText(R.string.new_content);
        } else {
            viewBinding.newContentTxt.setText("");
        }
    }

    private void updateTicker() {
        String tickerText = lwayveSdk.getTickerText();
        if (!lwayveSdk.isSituationalUpdatePending() && lwayveSdk.getCurrentQueueItemDuration() > -1) {
            tickerText += " (" + getClipLength() + ")";
        }
        viewBinding.ticker.setText(tickerText);
    }

    @NonNull
    private String getClipLength() {
        long clipLength = lwayveSdk.getCurrentQueueItemDuration();
        return LocalTime.MIN.plus(clipLength, ChronoUnit.MILLIS).format(DateTimeFormatter.ofPattern("mm:ss"));
    }

    private void updatePlayBtnState() {
        int res = lwayveSdk.isPlaying() ? R.drawable.ic_stop_selector : R.drawable.ic_play_selector;
        viewBinding.playBtn.setImageResource(res);
    }

    private void updateActionsMenuEnabled() {
        viewBinding.actions.setEnabled(!lwayveSdk.getClipActions().isEmpty());
    }

    private void updateCurrentTrackIndex() {
        String text = "0/0";
        List<MediaSessionCompat.QueueItem> queue = lwayveSdk.getPlayingQueue();
        if (queue != null && !queue.isEmpty()) {
            int idx = lwayveSdk.getCurrentQueueItemIndex() + 1;
            int size = queue.size();
            if (idx > size) {
                text = "End";
            } else if (idx >= 0 && size > 0) {
                text = idx + "/" + size;
            }
        }
        viewBinding.trackIndex.setText(text);
    }

    private void initTimer() {
        cancelTimer();
        timerPrevTimestamp = System.currentTimeMillis();
        timerRunnable = new TimerRunnable();
        updateTimer();
    }

    private void updateTimer() {
        handler.postDelayed(timerRunnable, PROGRESS_UPDATE_DELAY_MS);
    }

    private void cancelTimer() {
        if (timerRunnable != null) {
            handler.removeCallbacks(timerRunnable);
            timerRunnable = null;
        }
    }

    private void resetSeekBar() {
        currentProgress = 0;
        viewBinding.seekBar.setProgress(0);
        viewBinding.seekBar.setMax(0);
    }

    public void updateProgress(int position, int max) {
        viewBinding.seekBar.setProgress(position);
        viewBinding.seekBar.setMax(max);
    }

    private class TimerRunnable implements Runnable {

        @Override
        public void run() {
            currentProgress += System.currentTimeMillis() - timerPrevTimestamp;
            timerPrevTimestamp = System.currentTimeMillis();

            int progressMaxVal = (int) lwayveSdk.getCurrentQueueItemDuration();

            updateProgress(currentProgress, progressMaxVal);

            if (lwayveSdk.isPlaying() && currentProgress < progressMaxVal) {
                updateTimer();
            } else {
                cancelTimer();
            }
        }

    }

    private class MediaBrowserConnectionCallback extends MediaBrowserCompat.ConnectionCallback {

        @Override
        public void onConnected() {
            handlePlaybackEvent(lwayveSdk.getLastPlaybackEvent());
        }

    }

    private class PlaybackEventsReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            PlaybackEvent event = (PlaybackEvent) intent.getExtras().get(EventHelper.PLAYBACK_AUDIO_EVENT_TYPE_KEY);
            Log.i(TAG, "got: " + event.name() + " event");
            handlePlaybackEvent(event);
        }
    }
    
}
