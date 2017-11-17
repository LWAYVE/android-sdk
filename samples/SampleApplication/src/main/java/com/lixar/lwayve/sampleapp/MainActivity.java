package com.lixar.lwayve.sampleapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lixar.lwayve.sdk.core.LwayveConnectionCallback;
import com.lixar.lwayve.sdk.core.LwayveSdk;
import com.lixar.lwayve.sdk.events.OnPlaybackEventListener;
import com.lixar.lwayve.sdk.exceptions.SdkNotInitializedException;
import com.lixar.lwayve.sdk.events.PlaybackEvent;
import com.lixar.lwayve.sdk.view.LwayvePlaybackControlView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_CODE = 100;

    private TextView txtDebugInfo;
    private LwayveSdk lwayveSdk;
    private EditText locationsInput;
    private EditText userLikesInput;
    private final Set<String> locations = new HashSet<>();
    private final Set<String> userLikes = new HashSet<>();
    private LwayvePlaybackControlView lwayvePlaybackControls;

    private static void startActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

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
        Button newActivityBtn = findViewById(R.id.btn_new_activity);
        newActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.startActivity(MainActivity.this);
            }
        });
        txtDebugInfo = findViewById(R.id.txt_debug_info);
        locationsInput = findViewById(R.id.txt_locations);
        userLikesInput = findViewById(R.id.txt_likes);
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
        inflater.inflate(R.menu.control_view, menu);
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

    public void refresh() {
        updateLocations();
        updateUserLikes();
    }

    private void updateLocations() {
        String input = locationsInput.getText().toString();
        if (!TextUtils.isEmpty(input)) {
            Set<String> newLocations = extractTags(input);

            Set<String> toRemove = new HashSet<>(locations);
            toRemove.removeAll(newLocations);

            Set<String> toAdd = new HashSet<>(newLocations);
            toAdd.removeAll(locations);

            lwayveSdk.removeUserLocations(toRemove);
            lwayveSdk.addUserLocations(toAdd);

            locations.removeAll(toRemove);
            locations.addAll(toAdd);
        } else {
            lwayveSdk.removeUserLocations(locations);
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
