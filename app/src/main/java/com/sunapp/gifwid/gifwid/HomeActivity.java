package com.sunapp.gifwid.gifwid;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class HomeActivity extends AppCompatActivity implements HomeFragment.UpdateMode{

    private static final String TAG = "HomeActivity";
    private Toolbar mToolbar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        mToolbar = (Toolbar) findViewById(R.id.app_b);
        mToolbar.setTitle(R.string.app_name);
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));

        if (Build.VERSION.SDK_INT >= 21) {
            mToolbar.setElevation(8);
        }

        FragmentManager fm = getSupportFragmentManager();
        if (savedInstanceState == null) {
            Fragment homeFragment = HomeFragment.newInstance();
            fm.beginTransaction().add(R.id.fragment_container_1, homeFragment).commit();
        }
    }
    @Override
    public void BeginDeleteMode() {
        if(mToolbar == null)
            return;

        mToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.delete_red_primary));
        mToolbar.setTitle(R.string.delete_mode_text);

        if(Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.delete_red_dark));
        }
    }

    @Override
    public void EndDeleteMode() {
        if(mToolbar == null)
            return;

        mToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        mToolbar.setTitle(R.string.app_name);

        if(Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
    }

    @Override
    public void HideDecodingSpinner() {
        findViewById(R.id.decoding_spinner).setVisibility(View.INVISIBLE);
    }
}
