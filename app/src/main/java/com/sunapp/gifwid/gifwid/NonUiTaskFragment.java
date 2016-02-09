package com.sunapp.gifwid.gifwid;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class NonUiTaskFragment extends Fragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }
}
