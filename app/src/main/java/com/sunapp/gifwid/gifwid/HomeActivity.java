package com.sunapp.gifwid.gifwid;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        if(savedInstanceState == null){

            FragmentManager fm = getSupportFragmentManager();
            Fragment homeFragment = HomeFragment.newInstance();

            fm.beginTransaction().add(R.id.fragment_container_1,homeFragment).commit();

        }
    }
}
