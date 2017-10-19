package com.lixar.lwayve.sampleapp;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.lixar.lwayve.sdk.core.LwayveSdk;
import com.lixar.lwayve.sdk.core.LwayveSdkConfiguration;
import com.lixar.lwayve.sdk.exceptions.InvalidSdkConfigurationException;
import com.lixar.lwayve.sdk.exceptions.SdkNotInitializedException;
import com.lixar.lwayve.sdk.utils.LanguageManager;

import timber.log.Timber;

public class SampleApplication extends Application {

    public static final String PREFS_KEY_AUTH_TOKEN = "sdk_auth_token";
    public static final String PREFS_KEY_BASE_URL = "sdk_base_url";
    public static final String PREFS_KEY_LANGUAGE = "sdk_language_preference";
    public static final String PREFS_KEY_MAX_CACHE_AGE = "sdk_cache_max_age";
    public static final String PREFS_KEY_MAX_CACHE_SIZE = "sdk_cache_size";

    private static final long DEFAULT_MAX_CACHE_SIZE = 0;
    private static final int DEFAULT_MAX_CACHE_AGE = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        initLogging();
        initLwayveSdk();
    }

    private void initLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    private void initLwayveSdk() {
        try {
            LwayveSdk.init(this, loadLwayveConfig());
        } catch (InvalidSdkConfigurationException e) {
            Timber.e(e);
        }
    }

    private LwayveSdkConfiguration loadLwayveConfig() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs == null) {
            return null;
        }

        String authToken = prefs.getString(PREFS_KEY_AUTH_TOKEN, BuildConfig.LWAYVE_AUTH_TOKEN);
        prefs.edit().putString(PREFS_KEY_AUTH_TOKEN, authToken).apply();

        String baseUrl = prefs.getString(PREFS_KEY_BASE_URL, BuildConfig.LWAYVE_BASE_URL);
        prefs.edit().putString(PREFS_KEY_BASE_URL, baseUrl).apply();

        String language = prefs.getString(PREFS_KEY_LANGUAGE, LanguageManager.getDeviceDefaultLanguage().getLanguage());
        prefs.edit().putString(PREFS_KEY_LANGUAGE, language).apply();

        String maxCacheAge = prefs.getString(PREFS_KEY_MAX_CACHE_AGE, String.valueOf(DEFAULT_MAX_CACHE_AGE));
        prefs.edit().putString(PREFS_KEY_MAX_CACHE_AGE, maxCacheAge).apply();

        String maxCacheSize = prefs.getString(PREFS_KEY_MAX_CACHE_SIZE, String.valueOf(DEFAULT_MAX_CACHE_SIZE));
        prefs.edit().putString(PREFS_KEY_MAX_CACHE_SIZE, maxCacheSize).apply();

        return new LwayveSdkConfiguration.Builder()
                .setAuthenticationToken(authToken)
                .setBaseUrl(baseUrl)
                .setLanguage(LanguageManager.getLanguageForString(language))
                .setMaxCacheAge(Integer.parseInt(maxCacheAge))
                .setMaxCacheSize(Integer.parseInt(maxCacheSize))
                .build();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        try {
            LwayveSdk.getInstance().deinit();
        } catch (SdkNotInitializedException e) {}
    }

}
