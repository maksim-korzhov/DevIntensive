package com.softdesign.devintensive.ui.activities.data.network.interceptors;

import com.softdesign.devintensive.ui.activities.data.managers.DataManager;
import com.softdesign.devintensive.ui.activities.data.managers.PreferencesManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        PreferencesManager pm = DataManager.getInstance().getPreferencesManager();

        Request original = chain.request();

        Request.Builder requestBuilder = original.newBuilder()
                .header("X-Access-Token", pm.getAuthToken())
                .header("Request-User-Id", pm.getUserId())
                .header("User-Agent", "DevIntensiveApp");

        Request request = requestBuilder.build();

        return chain.proceed(request);
    }
}
