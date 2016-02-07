package com.sunapp.gifwid.gifwid;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.internal.NavigationMenuView;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";
    public static ArrayList<GifMeta> mGifMetaArrayList;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        mToolbar = (Toolbar) findViewById(R.id.app_b);
        mToolbar.setTitle(R.string.search_results);
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        mToolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_white_24dp);

        if(Build.VERSION.SDK_INT >= 21) {
            mToolbar.setElevation(8);
        }

        mToolbar.setNavigationOnClickListener(new NavigationMenuView.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        if(savedInstanceState == null)
            new FileSearchTask().execute();

    }

    class FileSearchTask extends AsyncTask<Void,Void,ArrayList<GifMeta>>{

        @Override
        protected ArrayList<GifMeta> doInBackground(Void... params) {
            AutoSearch as = new AutoSearch(getApplicationContext());
            return as.getGifList();
        }

        @Override
        protected void onPostExecute(ArrayList<GifMeta> gifList) {
            super.onPostExecute(gifList);

            mGifMetaArrayList = gifList;

            FragmentManager fm = getSupportFragmentManager();
            AutoSearchFragment asf = AutoSearchFragment.newInstance();
            fm.beginTransaction().add(R.id.fragment_container_search,asf).commit();
        }
    }
}
