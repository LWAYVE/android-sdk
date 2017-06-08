package com.lixar.lwayve.androidsdk.sample;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.lixar.lwayve.androidsdk.BuildConfig;
import com.lixar.lwayve.sdk.core.LwayveSdk;
import com.lixar.lwayve.sdk.core.LwayveSdkConfiguration;
import com.lixar.lwayve.sdk.exceptions.InvalidSdkConfigurationException;
import com.lixar.lwayve.sdk.utils.LanguageManager;

import timber.log.Timber;


public class SampleApplication extends Application {

    private final String TAG = SampleApplication.class.getSimpleName();

    private String authToken = "AUTH_TOKEN";
    private String baseUrl = "BASE_URL";
    private String preferredLanguage;
    private long maxCacheSize = 0;
    private int maxCacheAge = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());

        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (defaultSharedPreferences != null) {
            String prefValue = defaultSharedPreferences.getString("sdk_auth_key", null);
            if (prefValue != null && !prefValue.isEmpty()) {
                authToken = prefValue;
            } else {
                authToken = BuildConfig.LWAYVE_AUTH_KEY;
                defaultSharedPreferences.edit().putString("sdk_auth_key", authToken).apply();
            }
            prefValue = defaultSharedPreferences.getString("sdk_base_url", null);
            if (prefValue != null && !prefValue.isEmpty()) {
                baseUrl = prefValue;
            } else {
                baseUrl = BuildConfig.LWAYVE_BASE_URL;
                defaultSharedPreferences.edit().putString("sdk_base_url", baseUrl).apply();
            }
            prefValue = defaultSharedPreferences.getString("language_preference", null);
            if (!TextUtils.isEmpty(prefValue)) {
                preferredLanguage = prefValue;
            } else {
                preferredLanguage = LanguageManager.getDeviceDefaultLanguage().getLanguage();
            }
            prefValue = defaultSharedPreferences.getString("sdk_cache_max_age", null);
            if (!TextUtils.isEmpty(prefValue)) {
                try {
                    maxCacheAge = Integer.valueOf(prefValue);
                } catch (NumberFormatException ex) {
                    maxCacheAge = 0;
                }
            }
            prefValue = defaultSharedPreferences.getString("sdk_cache_size", null);
            if (!TextUtils.isEmpty(prefValue)) {
                try {
                    maxCacheSize = Long.valueOf(prefValue);
                    maxCacheSize = maxCacheSize * 1000000;
                } catch (NumberFormatException ex) {
                    maxCacheSize = 0;
                }
            }

            LwayveSdkConfiguration configuration = new LwayveSdkConfiguration.Builder()
                    .setAuthenticationToken(authToken)
                    .setBaseUrl(baseUrl)
                    .setLanguage(LanguageManager.getLanguageForString(preferredLanguage))
                    .setMaxCacheSize(maxCacheSize)
                    .setMaxCacheAge(maxCacheAge)
                    .build();

            try {
                LwayveSdk.init(this, configuration);
            } catch (InvalidSdkConfigurationException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }
}
