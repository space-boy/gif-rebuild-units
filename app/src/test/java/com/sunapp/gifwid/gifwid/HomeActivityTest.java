package com.sunapp.gifwid.gifwid;

import android.content.Intent;
import android.widget.Button;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;

@RunWith(RobolectricGradleTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml", constants = BuildConfig.class, sdk=21)
public class HomeActivityTest {

    @Before
    public void setUp() throws Exception {
        // setup
    }

    @Test
    public void clickingCreateShouldLaunchDialog() throws Exception {

        //HomeActivity activity = Robolectric.buildActivity(HomeActivity.class).create().get();
        HomeActivity activity = Robolectric.setupActivity(HomeActivity.class);
        Button mCreate = (Button) activity.findViewById(R.id.create_gif_button_home);

        try {
            mCreate.performClick();
        } catch (Exception clickFail){
            System.out.println("mCreate is null" + clickFail);
        }

        assertTrue(mCreate.performClick());
        SearchPickerDialog fragment = new SearchPickerDialog();
        startFragment(fragment);
        assertNotNull(fragment);
    }

    @Test
    public void dialogHasTwoChoices() throws Exception{
        SearchPickerDialog fragment = new SearchPickerDialog();
        startFragment(fragment);
        assertNotNull(fragment);

        assertNotNull(fragment.getDialog().findViewById(R.id.manual_search_button).performClick());
        assertNotNull(fragment.getDialog().findViewById(R.id.auto_search_button).performClick());
    }

    @Test
    public void dialogClickStartsSearchActivity() throws Exception{
        HomeActivity activity = Robolectric.setupActivity(HomeActivity.class);
        SearchPickerDialog fragment = new SearchPickerDialog();
        startFragment(fragment);
        assertNotNull(fragment);

        assertNotNull(fragment.getDialog().findViewById(R.id.manual_search_button).performClick());
        assertNotNull(fragment.getDialog().findViewById(R.id.auto_search_button).performClick());

        fragment.getDialog().findViewById(R.id.auto_search_button).performClick();
        Intent expectedIntent = new Intent(activity, SearchActivity.class);
        assertThat(shadowOf(activity).getNextStartedActivity()).isEqualTo(expectedIntent);
    }



}
