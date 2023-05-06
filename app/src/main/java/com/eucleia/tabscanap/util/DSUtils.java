package com.eucleia.tabscanap.util;

import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;

import com.blankj.utilcode.util.Utils;
import com.eucleia.tabscanap.constant.CharVar;
import com.eucleia.pdicheck.bean.constant.Constant;

import io.reactivex.Flowable;
import io.reactivex.Single;


public class DSUtils {

    public static DSUtils get() {
        return InClass.instance;
    }


    private static class InClass {
        private static DSUtils instance = new DSUtils();
    }


    private RxDataStore<Preferences> dataStores;

    private DSUtils() {
        dataStores = new RxPreferenceDataStoreBuilder(Utils.getApp().getApplicationContext(), Constant.APP_TAG).build();
    }


    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean b) {
        Preferences.Key<Boolean> booleanKey = PreferencesKeys.booleanKey(key);
        Flowable<Boolean> booleanFlowable = dataStores.data().map(preferences ->
                preferences.get(booleanKey) == null ? b : preferences.get(booleanKey)
        );
        return booleanFlowable.blockingFirst();
    }

    public void putBoolean(String key, boolean bValue) {
        Preferences.Key<Boolean> booleanKey = PreferencesKeys.booleanKey(key);
        dataStores.updateDataAsync(preferences -> {
            MutablePreferences mutablePreferences = preferences.toMutablePreferences();
            mutablePreferences.set(booleanKey, bValue);
            return Single.just(mutablePreferences.toPreferences());
        });
    }

    public int getInt(String key) {
        Preferences.Key<Integer> integerKey = PreferencesKeys.intKey(key);
        Flowable<Integer> integerFlowable = dataStores.data().map(preferences ->
                preferences.get(integerKey) == null ? 0 : preferences.get(integerKey)
        );
        return integerFlowable.blockingFirst();
    }

    public void putInt(String key, int bValue) {
        Preferences.Key<Integer> integerKey = PreferencesKeys.intKey(key);
        dataStores.updateDataAsync(preferences -> {
            MutablePreferences mutablePreferences = preferences.toMutablePreferences();
            mutablePreferences.set(integerKey, bValue);
            return Single.just(mutablePreferences.toPreferences());
        });
    }

    public long getLong(String key) {
        Preferences.Key<Long> longKey = PreferencesKeys.longKey(key);
        Flowable<Long> longFlowable = dataStores.data().map(preferences ->
                preferences.get(longKey) == null ? 0L : preferences.get(longKey)
        );
        return longFlowable.blockingFirst();
    }

    public void putLong(String key, long bValue) {
        Preferences.Key<Long> longKey = PreferencesKeys.longKey(key);
        dataStores.updateDataAsync(preferences -> {
            MutablePreferences mutablePreferences = preferences.toMutablePreferences();
            mutablePreferences.set(longKey, bValue);
            return Single.just(mutablePreferences.toPreferences());
        });
    }

    public String getString(String key) {
        Preferences.Key<String> stringKey = PreferencesKeys.stringKey(key);
        Flowable<String> stringFlowable = dataStores.data().map(preferences ->
                preferences.get(stringKey) == null ? CharVar.EMPTY : preferences.get(stringKey)
        );
        return stringFlowable.blockingFirst();
    }

    public void putString(String key, String bValue) {
        Preferences.Key<String> stringKey = PreferencesKeys.stringKey(key);
        dataStores.updateDataAsync(preferences -> {
            MutablePreferences mutablePreferences = preferences.toMutablePreferences();
            mutablePreferences.set(stringKey, bValue);
            return Single.just(mutablePreferences.toPreferences());
        });
    }

}
