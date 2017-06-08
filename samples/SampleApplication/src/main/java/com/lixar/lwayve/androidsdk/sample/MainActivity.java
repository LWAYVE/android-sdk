package com.lixar.lwayve.androidsdk.sample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.lixar.lwayve.androidsdk.R;
import com.lixar.lwayve.sdk.core.LwayveSdk;
import com.lixar.lwayve.sdk.exceptions.SdkNotInitializedException;
import com.lixar.lwayve.sdk.services.audio.PlaybackEventType;
import com.lixar.lwayve.sdk.utils.BroadcastHelper;

import java.util.ArrayList;
import java.util.LinkedHashSet;
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

    private boolean needsPreparation = false;

    @BindView(R.id.status)
    TextView statusTextView;

    @BindView(R.id.unplayed)
    TextView unplayedTextView;

    private BroadcastReceiver playbackEventsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            PlaybackEventType event = (PlaybackEventType) intent.getExtras().get(BroadcastHelper.PLAYBACK_AUDIO_EVENT_TYPE_KEY);
            Log.i(TAG, "got: " + event.name() + " event");
            if (lwayveSdk != null && event.name().equals(PlaybackEventType.PLAY_EVENT.name())) {
                long duration = lwayveSdk.getCurrentPlaylistItemDuration();
                String text = "current audio clip duration: " + duration + " ms";
                Log.i(TAG, text);
                statusTextView.setText(text);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        checkPermissions();

        try {
            lwayveSdk = LwayveSdk.getInstance();
            lwayveSdk.connectToMediaBrowser(this, null);

            populateUnplayedItemsCountTextView();
        } catch (SdkNotInitializedException e) {
            Log.e(TAG, e.getMessage());
            lwayveSdk = null;
        }
    }

    private void populateUnplayedItemsCountTextView() {
        unplayedTextView.setText("Number of unplayed items: " + lwayveSdk.getUnplayedPlaylistItemsCount());
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(BroadcastHelper.PLAYBACK_AUDIO_EVENT_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(playbackEventsReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(playbackEventsReceiver);
    }

    @OnClick(R.id.start)
    public void start() {
        if (lwayveSdk != null) {
            if (needsPreparation) {
                lwayveSdk.connectToMediaBrowser(this, null);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lwayveSdk.play();
                        needsPreparation = false;
                    }
                }, 1000);
            } else {
                lwayveSdk.play();
            }
        }
    }

    @OnClick(R.id.pause)
    public void pause() {
        if (lwayveSdk != null) {
            lwayveSdk.pause();
        }
    }

    @OnClick(R.id.stop)
    public void stop() {
        if (lwayveSdk != null) {
            lwayveSdk.stop();
            needsPreparation = true;
        }
    }

    @OnClick(R.id.tags)
    public void tags() {
        if (lwayveSdk != null) {
            Set<String> tags = new LinkedHashSet<>();
            tags.add("#Drinks");
            tags.add("#Danica");
            tags.add("#Johnson-Jimmie");
            tags.add("#Car43");
            tags.add("#Car45");
            tags.add("#Car50");
            lwayveSdk.addTags(tags);
        }
    }

    @OnClick(R.id.locations)
    public void locations() {
        if (lwayveSdk != null) {
            Set<String> locations = new LinkedHashSet<>();
            locations.add("#bar1");
            locations.add("#bar2");
            lwayveSdk.addLocations(locations);
        }
    }

    @OnClick(R.id.skip)
    public void skip() {
        if (lwayveSdk != null) {
            lwayveSdk.next();
        }
    }

    @OnClick(R.id.rewind)
    public void rewind() {
        if (lwayveSdk != null) {
            lwayveSdk.prev();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (lwayveSdk != null) {
            lwayveSdk.deinit();
            lwayveSdk = null;
        }
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
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            settingsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(settingsIntent);
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

}