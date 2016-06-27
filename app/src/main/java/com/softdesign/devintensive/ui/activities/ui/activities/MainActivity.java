package com.softdesign.devintensive.ui.activities.ui.activities;

import android.graphics.Color;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.ui.activities.utils.ConstantManager;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = ConstantManager.TAG_PREFIX + "Main Activity";
    private ImageView mCallImg;
    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");

        mCallImg = (ImageView) findViewById(R.id.call_img);
        mCallImg.setOnClickListener(this);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_container);

        if( savedInstanceState == null ) {
            // активити запущено впервые
            showSnackbar("Activity запускается впервые");
        } else {
            // активити уже запускалось
            showSnackbar("Activity уже запускалось");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.d(TAG, "onStart");
    }

    @Override
    public void onClick(View v) {
        switch( v.getId() ) {
            case R.id.call_img:
                /*showProgress();
                runWithDelay();*/
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

/*    private void runWithDelay() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO: выполнить с задержкой
                hideProgress();
            }
        }, 5000);
    }*/

    private void showSnackbar( String message ) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }
}
