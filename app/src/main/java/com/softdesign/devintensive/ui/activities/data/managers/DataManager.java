package com.softdesign.devintensive.ui.activities.data.managers;

import android.content.Context;

import com.softdesign.devintensive.ui.activities.data.network.RestService;
import com.softdesign.devintensive.ui.activities.data.network.ServiceGenerator;
import com.softdesign.devintensive.ui.activities.data.network.req.UserLoginReq;
import com.softdesign.devintensive.ui.activities.data.network.res.UserListRes;
import com.softdesign.devintensive.ui.activities.data.network.res.UserModelRes;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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


    public Call<ResponseBody> uploadPhoto(File photoFile) {
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), photoFile);
        MultipartBody.Part bodyPart =
                MultipartBody.Part.createFormData("photo", photoFile.getName(), requestBody);
        return mRestService.uploadImage(bodyPart);
    }

    public Call<UserListRes> getUserList() {
        return mRestService.getUserList();
    }
    // endregion
}