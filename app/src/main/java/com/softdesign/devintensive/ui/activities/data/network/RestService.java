package com.softdesign.devintensive.ui.activities.data.network;

import com.softdesign.devintensive.ui.activities.data.network.req.UserLoginReq;
import com.softdesign.devintensive.ui.activities.data.network.res.UserListRes;
import com.softdesign.devintensive.ui.activities.data.network.res.UserModelRes;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RestService {

    @POST("login")
    Call<UserModelRes> loginUser(@Body UserLoginReq req);

    @Multipart
    @POST("user/edit")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part file);

    //@Multipart
    //@POST("user/{userId}/publicValues/profilePhoto")
    //Call<UploadPhotoRes> uploadPhoto( @Path("userId") String userId,
    // @Part MultipartBody.Part file);

    @GET("user/list?orderBy=rating")
    Call<UserListRes> getUserList();
}
