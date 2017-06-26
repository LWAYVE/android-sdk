package com.lixar.lwayve.sampleB;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;

import com.lixar.lwayve.sampleB.R;
import com.lixar.lwayve.sdk.core.LwayveSdk;
import com.lixar.lwayve.sdk.core.LwayveSdkConfiguration;
import com.lixar.lwayve.sdk.exceptions.InvalidSdkConfigurationException;
import com.lixar.lwayve.sdk.exceptions.SdkNotInitializedException;
import com.lixar.lwayve.sdk.utils.LanguageManager;

import java.util.List;

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

    public final static String TAG = SettingsActivity.class.getSimpleName();
    private static boolean lwayveSdkConfigurationChanged = false;
    private static LwayveSdkConfiguration newConfiguration;

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

            preference.setSummary(stringValue);

            try {
                if (LwayveSdk.getInstance() != null) {
                    try {

                        LwayveSdkConfiguration originalConfiguration = LwayveSdk.getInstance().getConfiguration();

                        LwayveSdkConfiguration.Builder newConfigurationBuilder;
                        if (newConfiguration == null) {
                            newConfigurationBuilder = new LwayveSdkConfiguration.Builder().from(originalConfiguration);
                        } else {
                            newConfigurationBuilder = new LwayveSdkConfiguration.Builder().from(newConfiguration);
                        }

                        if (key.equals("sdk_base_url")) {
                            newConfigurationBuilder.setBaseUrl(stringValue);
                        } else if (key.equals("sdk_auth_key")) {
                            newConfigurationBuilder.setAuthenticationToken(stringValue);
                        } else if (key.equals("language_preference")) {
                            newConfigurationBuilder.setLanguage(LanguageManager.getLanguageForString(stringValue));
                        } else if (key.equals("sdk_cache_max_age")) {
                            newConfigurationBuilder.setMaxCacheAge(Integer.parseInt(stringValue));
                        } else if (key.equals("sdk_cache_size")) {
                            newConfigurationBuilder.setMaxCacheSize(Integer.parseInt(stringValue));
                        }

                        newConfiguration = newConfigurationBuilder.build();

                        lwayveSdkConfigurationChanged = true;
                    } catch (IllegalStateException ex) {
                        Log.e(TAG, ex.getMessage());
                    }
                }
            } catch (SdkNotInitializedException ex) {
                Log.e(TAG, ex.getMessage());
                return false;
            }
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
    }

    @Override
    protected void onDestroy() {
        if (lwayveSdkConfigurationChanged) {
            try {
                LwayveSdkConfiguration updatedConfig = new LwayveSdkConfiguration.Builder().from(newConfiguration).build();
                LwayveSdk.getInstance().updateConfiguration(getApplicationContext(), updatedConfig);
            } catch (SdkNotInitializedException ex) {
                Log.e(TAG, ex.getMessage());
            } catch (InvalidSdkConfigurationException ex) {
                Log.e(TAG, ex.getMessage());
            } finally {
                lwayveSdkConfigurationChanged = false;
            }
        }
        super.onDestroy();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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
            bindPreferenceSummaryToValue(findPreference("sdk_base_url"));
            bindPreferenceSummaryToValue(findPreference("sdk_auth_key"));
            bindPreferenceSummaryToValue(findPreference("language_preference"));
            bindPreferenceSummaryToValue(findPreference("sdk_cache_max_age"));
            bindPreferenceSummaryToValue(findPreference("sdk_cache_size"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }
}
