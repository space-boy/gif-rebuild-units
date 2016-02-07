package com.sunapp.gifwid.gifwid;

import android.content.Context;
import java.util.ArrayList;

public class GifDataStore {

    private static GifDataStore sGifDataStore;
    private Context mAppContext;
    private ArrayList<GifMeta> mGifMetaArrayList;

    public ArrayList<GifMeta> getGifMetaArrayList() {

        if(mGifMetaArrayList == null)
            mGifMetaArrayList = new ArrayList<>();

        return mGifMetaArrayList;
    }

    private GifDataStore(Context appcontext){
        mAppContext = appcontext;
    }

    public static GifDataStore get(Context c){
        if(sGifDataStore == null){
            sGifDataStore = new GifDataStore(c.getApplicationContext());
        }
        return sGifDataStore;
    }
}