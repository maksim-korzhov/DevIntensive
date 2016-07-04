package com.softdesign.devintensive.ui.activities.data.managers;

public class DataManager {
    public static DataManager INSTANCE = null;

    private PreferencesManager mPreferencesManager;

    public DataManager() {
        this.mPreferencesManager = new PreferencesManager();
    }

    public static DataManager getInstance() {
        if( INSTANCE == null ) {
            INSTANCE = new DataManager();
        }

        return INSTANCE;
    }

    public PreferencesManager getPreferencesManager() {
        return mPreferencesManager;
    }
}