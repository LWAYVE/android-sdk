package com.lixar.lwayve.customcontrolsample;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.lixar.lwayve.proxsee.ProxSeeSdkAdapterFactory;
import com.lixar.lwayve.sdk.core.LwayveSdk;
import com.lixar.lwayve.sdk.core.LwayveSdkConfiguration;
import com.lixar.lwayve.sdk.exceptions.InvalidSdkConfigurationException;
import com.lixar.lwayve.sdk.experience.ExperienceLanguage;
import com.lixar.lwayve.sdk.recorder.RecorderAdapterFactory;

import timber.log.Timber;

public class SampleApplication extends Application {

    public static final String PREFS_KEY_AUTH_TOKEN = "sdk_auth_token";
    public static final String PREFS_KEY_BASE_URL = "sdk_base_url";
    public static final String PREFS_KEY_LANGUAGE = "sdk_language_preference";

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

        String language = prefs.getString(PREFS_KEY_LANGUAGE, ExperienceLanguage.getDeviceDefaultLanguage().getLanguageCode());
        prefs.edit().putString(PREFS_KEY_LANGUAGE, language).apply();

        return new LwayveSdkConfiguration.Builder()
                .setAuthenticationToken(authToken)
                .setBaseUrl(baseUrl)
                .setLanguage(ExperienceLanguage.getLanguageForCode(language))
                .setProxSeeSdkAdapterFactory(new ProxSeeSdkAdapterFactory(this))
                .setRecorderAdapterFactory(new RecorderAdapterFactory())
                .build();
    }

}
