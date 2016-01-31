package com.sunapp.gifwid.gifwid;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";
    public static ArrayList<GifMeta> mGifMetaArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        if(mGifMetaArrayList == null)
            new FileSearchTask().execute();

//        if(savedInstanceState == null){
//            FragmentManager fm = getSupportFragmentManager();
//            AutoSearchFragment asf = AutoSearchFragment.newInstance();
//            fm.beginTransaction().add(R.id.fragment_container_search,asf).commit();
//        }

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