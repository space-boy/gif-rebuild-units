package com.sunapp.gifwid.gifwid;


import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeFragment extends Fragment {

    private Button mCreateButton;
    private TextView mTextView;
    private static final String DIALOG_SEARCH = "DialogSearch";
    private static final String TAG = "HomeFrag";

    GifMeta gm1;
    GifMeta gm2;
    GifMeta gm3;

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //retrieve values from shared prefs later for the 3 active widgets

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.home_main_fragment,container,false);
        mCreateButton = (Button) v.findViewById(R.id.create_gif_button_home);

        ViewTreeObserver observer = v.getViewTreeObserver();

        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {

                //use display metrics to get size of device screen
                //then we know roughly how big to make the bitmap
                //this preDraw might not be useful in this situation

                View mInclude1 = v.findViewById(R.id.gif_view_1);
                ImageView mIncImg1 = (ImageView) mInclude1.findViewById(R.id.gif_widget_home_imageview);
                Log.i(TAG, "width: " + mIncImg1.getWidth());
                Log.i(TAG, "height: " + mIncImg1.getHeight());

                View mInclude2 = v.findViewById(R.id.gif_view_2);
                ImageView mIncImg2 = (ImageView) mInclude2.findViewById(R.id.gif_widget_home_imageview);

                View mInclude3 = v.findViewById(R.id.gif_view_3);
                ImageView mIncImg3 = (ImageView) mInclude3.findViewById(R.id.gif_widget_home_imageview);


                return true;
            }
        });

        //below is purely for testing
        View mInclude1 = v.findViewById(R.id.gif_view_1);
        ImageView mIncImg1 = (ImageView) mInclude1.findViewById(R.id.gif_widget_home_imageview);
        //mIncImg1.setImageBitmap(BitmapFactory.decodeFile("/storage/emulated/0/cardcaptor_1.gif"));

        View mInclude2 = v.findViewById(R.id.gif_view_2);
        ImageView mIncImg2 = (ImageView) mInclude2.findViewById(R.id.gif_widget_home_imageview);
        //mIncImg2.setImageBitmap(BitmapFactory.decodeFile("/storage/emulated/0/cardcaptor_1.gif"));

        View mInclude3 = v.findViewById(R.id.gif_view_3);
        ImageView mIncImg3 = (ImageView) mInclude3.findViewById(R.id.gif_widget_home_imageview);
        //mIncImg3.setImageBitmap(BitmapFactory.decodeFile("/storage/emulated/0/cardcaptor_1.gif"));

        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                SearchPickerDialog searchDialog = new SearchPickerDialog();
                searchDialog.show(fm,DIALOG_SEARCH);
                
            }
        });

        return v;
    }


}
