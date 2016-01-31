package com.sunapp.gifwid.gifwid;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HomeFragment extends Fragment {

    private Button mCreateButton;
    private static final String DIALOG_SEARCH = "DialogSearch";

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.home_main_fragment,container,false);
        mCreateButton = (Button) v.findViewById(R.id.create_gif_button_home);

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
