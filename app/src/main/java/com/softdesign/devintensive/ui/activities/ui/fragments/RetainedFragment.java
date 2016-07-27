package com.softdesign.devintensive.ui.activities.ui.fragments;


import android.app.Fragment;
import android.os.Bundle;

import com.softdesign.devintensive.ui.activities.data.storage.models.User;

import java.util.List;


public class RetainedFragment extends Fragment {

    private List<User> mData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    public List<User> getData() {
        return mData;
    }

    public void setData(List<User> data) {
        mData = data;
    }
}