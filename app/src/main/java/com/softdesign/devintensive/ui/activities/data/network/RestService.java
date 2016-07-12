package com.softdesign.devintensive.ui.activities.data.network;

import com.softdesign.devintensive.ui.activities.data.network.req.UserLoginReq;
import com.softdesign.devintensive.ui.activities.data.network.res.UserModelRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RestService {

    @POST("login")
    Call<UserModelRes> loginUser(@Body UserLoginReq req);
}
