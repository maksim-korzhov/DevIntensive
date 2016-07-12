package com.softdesign.devintensive.ui.activities.data.managers;

import android.content.Context;

import com.softdesign.devintensive.ui.activities.data.network.RestService;
import com.softdesign.devintensive.ui.activities.data.network.ServiceGenerator;
import com.softdesign.devintensive.ui.activities.data.network.req.UserLoginReq;
import com.softdesign.devintensive.ui.activities.data.network.res.UserModelRes;
import com.softdesign.devintensive.ui.activities.utils.DevintensiveApplication;

import retrofit2.Call;

public class DataManager {
    public static DataManager INSTANCE = null;

    private Context mContext;
    private PreferencesManager mPreferencesManager;
    private RestService mRestService;


    public DataManager() {
        //this.mContext = DevintensiveApplication.getContext();
        this.mPreferencesManager = new PreferencesManager();
        this.mRestService = ServiceGenerator.createService(RestService.class);
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

    // region ==================== Network ==================
    public Call<UserModelRes> loginUser(UserLoginReq userLoginReq) {
        return mRestService.loginUser(userLoginReq);
    }
    // endregion
}