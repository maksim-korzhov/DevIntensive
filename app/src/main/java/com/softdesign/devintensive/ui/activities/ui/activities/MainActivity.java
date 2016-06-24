package com.softdesign.devintensive.ui.activities.ui.activities;

import android.graphics.Color;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.ui.activities.utils.ConstantManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    protected EditText mEditText;
    protected Button mRedButton, mGreenButton;
    protected int mColorMode;

    public static final String TAG = ConstantManager.TAG_PREFIX + "Main Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRedButton = (Button) findViewById(R.id.red_btn);
        mGreenButton = (Button) findViewById(R.id.green_btn);
        mEditText = (EditText) findViewById(R.id.textView);

        mRedButton.setOnClickListener(this);
        mGreenButton.setOnClickListener(this);

        if( savedInstanceState == null ) {
            // активити запущено впервые
        } else {
            // активити уже запускалось

            mColorMode = savedInstanceState.getInt(ConstantManager.COLOR_MODE_KEY);

            if( mColorMode == Color.RED ) {
                mEditText.setBackgroundColor(Color.RED);
            } else if( mColorMode == Color.GREEN ) {
                mEditText.setBackgroundColor(Color.GREEN);
            }
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
            case R.id.red_btn:
                mEditText.setBackgroundColor(Color.RED);
                mColorMode = Color.GREEN;
                break;
            case R.id.green_btn:
                mEditText.setBackgroundColor(Color.GREEN);
                mColorMode = Color.RED;
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.d(TAG, "onSaveInstanceState");
        outState.putInt(ConstantManager.COLOR_MODE_KEY, mColorMode);
    }
}
