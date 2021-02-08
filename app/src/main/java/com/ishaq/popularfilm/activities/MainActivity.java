package com.ishaq.popularfilm.activities;

import androidx.appcompat.app.AppCompatActivity;


import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;


import com.ishaq.popularfilm.R;

import com.ishaq.popularfilm.utils.OnNetworkListener;




public class MainActivity extends AppCompatActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener, OnNetworkListener {

    private static final String TAG = "MainActivity";
    public static Dialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }



    @Override
    public void onNetworkConnected() {

    }

    @Override
    public void onNetworkDisconnected() {

    }
}
