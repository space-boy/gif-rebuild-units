package com.sunapp.gifwid.gifwid;

import android.support.v4.app.Fragment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertNotNull;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;

@RunWith(RobolectricGradleTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml", constants = BuildConfig.class, sdk=21)
public class SearchActivityTest {

    //test AutoSearch Fragment and Manual Fragment can Exist
    @Test
    public void fragmentCanExist() throws Exception{
        Fragment fragment = new AutoSearchFragment();
        startFragment(fragment);
        assertNotNull(fragment);
    }

    //test auto fragment OR manual fragment attached
    @Test
    public void aSearchFragmentIsAttached() throws Exception {
        SearchActivity activity = Robolectric.buildActivity(SearchActivity.class)
                .attach().create().start().resume().get();

        Fragment fragment;
        fragment = activity.getSupportFragmentManager().findFragmentById(R.id.fragment_container_search);
        assertNotNull(fragment);
    }

    //test list items clickable if auto search

    //test gridview items clickable if auto search

    //test list items clickable if manual search




}
