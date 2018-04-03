package com.lixar.lwayve.prebuiltcontrolsample;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lixar.lwayve.sdk.core.LwayveConnectionCallback;
import com.lixar.lwayve.sdk.core.LwayveSdk;
import com.lixar.lwayve.sdk.events.OnPlaybackEventListener;
import com.lixar.lwayve.sdk.events.PlaybackEvent;
import com.lixar.lwayve.sdk.view.LwayvePlaybackControlView;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_CODE = 100;

    private TextView txtDebugInfo;
    private LwayveSdk lwayveSdk;
    private LwayvePlaybackControlView lwayvePlaybackControls;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissions();
        initializeLwayveSdk();
        lwayvePlaybackControls = findViewById(R.id.lwayve_playback_controls);
        lwayvePlaybackControls.setOnPlaybackEventListener(new OnPlaybackEventListener() {
            @Override
            public void onPlaybackEvent(PlaybackEvent eventType) {
                updateDebugInfo();
            }
        });
        Button refreshBtn = findViewById(R.id.btn_refresh);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });
        txtDebugInfo = findViewById(R.id.txt_debug_info);
    }

    private void updateDebugInfo() {
        String info = lwayveSdk.generateDebugInfo()
                + lwayveSdk.generatePlaylistDebugInfo()
                + lwayveSdk.generateQueueDebugInfo();
        txtDebugInfo.setText(info);
    }

    private void initializeLwayveSdk() {
        LwayveSdk.connect(new LwayveConnectionCallback() {
            @Override
            public void onConnected(@NonNull LwayveSdk sdk) {
                lwayveSdk = sdk;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        lwayvePlaybackControls.connectToMediaBrowser();
    }

    @Override
    protected void onPause() {
        super.onPause();
        lwayvePlaybackControls.disconnectFromMediaBrowser();
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

    public void refresh() {
        lwayveSdk.refreshPlayingQueue();
    }

}
