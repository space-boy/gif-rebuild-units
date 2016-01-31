package com.sunapp.gifwid.gifwid;


public class GifMeta {

    private String mFileName;
    private int mFrames;
    private int mRefreshRate;
    private boolean mIsActive;

    //possibly override later
    public GifMeta(){

    }

    public String getFileName() {
        return mFileName;
    }

    public void setFileName(String fileName) {
        mFileName = fileName;
    }

    public int getFrames() {
        return mFrames;
    }

    public void setFrames(int frames) {
        mFrames = frames;
    }

    public boolean isActive() {
        return mIsActive;
    }

    public void setIsActive(boolean isActive) {
        mIsActive = isActive;
    }

    public int getRefreshRate() {
        return mRefreshRate;
    }

    public void setRefreshRate(int refreshRate) {
        mRefreshRate = refreshRate;
    }
}
