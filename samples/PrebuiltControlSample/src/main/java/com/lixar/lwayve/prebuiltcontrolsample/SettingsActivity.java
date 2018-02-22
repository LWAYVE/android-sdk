package com.lixar.lwayve.prebuiltcontrolsample;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;

import com.lixar.lwayve.sdk.core.LwayveConnectionCallback;
import com.lixar.lwayve.sdk.core.LwayveSdk;
import com.lixar.lwayve.sdk.core.LwayveSdkConfiguration;
import com.lixar.lwayve.sdk.exceptions.InvalidSdkConfigurationException;
import com.lixar.lwayve.sdk.experience.ExperienceLanguage;
import com.lixar.lwayve.sdk.utils.UrlUtils;

import java.util.List;

import timber.log.Timber;

import static com.lixar.lwayve.prebuiltcontrolsample.SampleApplication.PREFS_KEY_AUTH_TOKEN;
import static com.lixar.lwayve.prebuiltcontrolsample.SampleApplication.PREFS_KEY_BASE_URL;
import static com.lixar.lwayve.prebuiltcontrolsample.SampleApplication.PREFS_KEY_LANGUAGE;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {

    private LwayveSdk lwayveSdk;

    private SharedPreferences prefs;

    public static void startActivity(Context context) {
        Intent intent = new Intent( context, SettingsActivity.class );
        intent.putExtra( PreferenceActivity.EXTRA_SHOW_FRAGMENT, GeneralPreferenceFragment.class.getName() );
        intent.putExtra( PreferenceActivity.EXTRA_NO_HEADERS, true );
        context.startActivity(intent);
    }

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String key = preference.getKey();
            String stringValue = value.toString();

            if (TextUtils.equals(key, PREFS_KEY_BASE_URL) && (TextUtils.isEmpty(stringValue) || !UrlUtils.isUrlValid(stringValue))) {
                return false;
            }

            if (TextUtils.equals(key, PREFS_KEY_AUTH_TOKEN) && TextUtils.isEmpty(stringValue)) {
                return false;
            }

            preference.setSummary(stringValue);

            return true;
        }
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        LwayveSdk.connect(new LwayveConnectionCallback() {
            @Override
            public void onConnected(@NonNull LwayveSdk sdk) {
                lwayveSdk = sdk;
            }
        });
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.settings);
        }
    }

    @Override
    public void onBackPressed() {
        if (isLwayveConfigUpdated()) {
            updateLwayveConfiguration();
        }
        super.onBackPressed();
    }

    private boolean isLwayveConfigUpdated() {
        boolean lwayveConfigUpdated = false;

        LwayveSdkConfiguration lwayveConfig = lwayveSdk.getConfiguration();

        if (isBaseUrlChanged(lwayveConfig)) {
            lwayveConfigUpdated = true;
        }

        if (isAuthTokenChanged(lwayveConfig)) {
            lwayveConfigUpdated = true;
        }

        if (isLanguageChanged(lwayveConfig)) {
            lwayveConfigUpdated = true;
        }

        return lwayveConfigUpdated;
    }

    private void updateLwayveConfiguration() {
        if (lwayveSdk == null) {
            Timber.e("updateLwayveConfiguration() LwayveSdk is not initialized!");
            return;
        }

        LwayveSdkConfiguration lwayveConfig = lwayveSdk.getConfiguration();
        LwayveSdkConfiguration.Builder newConfigBuilder = new LwayveSdkConfiguration.Builder().from(lwayveConfig);

        if (isBaseUrlChanged(lwayveConfig)) {
            newConfigBuilder.setBaseUrl(getSavedBaseUrl());
        }

        if (isAuthTokenChanged(lwayveConfig)) {
            newConfigBuilder.setAuthenticationToken(getSavedAuthToken());
        }

        if (isLanguageChanged(lwayveConfig)) {
            newConfigBuilder.setLanguage(ExperienceLanguage.getLanguageForCode(getSavedLanguage()));
        }

        try {
            lwayveSdk.updateConfiguration(newConfigBuilder.build());
        } catch (InvalidSdkConfigurationException ex) {
            Timber.e(ex.getMessage());
            Toast.makeText(this, "LWAYVE SDK Config invalid. LWAYVE was NOT reinitialized.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isBaseUrlChanged(LwayveSdkConfiguration lwayveConfig) {
        return !TextUtils.equals(getSavedBaseUrl(), lwayveConfig.getBaseUrl());
    }

    @NonNull
    private String getSavedBaseUrl() {
        return prefs.getString(PREFS_KEY_BASE_URL, "");
    }

    private boolean isAuthTokenChanged(LwayveSdkConfiguration lwayveConfig) {
        return !TextUtils.equals(getSavedAuthToken(), lwayveConfig.getAuthToken());
    }

    @NonNull
    private String getSavedAuthToken() {
        return prefs.getString(PREFS_KEY_AUTH_TOKEN, "");
    }

    private boolean isLanguageChanged(LwayveSdkConfiguration lwayveConfig) {
        return !TextUtils.equals(getSavedLanguage(), lwayveConfig.getLanguage().getLanguageCode());
    }

    @NonNull
    private String getSavedLanguage() {
        return prefs.getString(PREFS_KEY_LANGUAGE, "");
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!super.onMenuItemSelected(featureId, item)) {
                NavUtils.navigateUpFromSameTask(this);
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference(PREFS_KEY_BASE_URL));
            bindPreferenceSummaryToValue(findPreference(PREFS_KEY_AUTH_TOKEN));
            bindPreferenceSummaryToValue(findPreference(PREFS_KEY_LANGUAGE));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                ((SettingsActivity)getActivity()).onBackPressed();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

}
